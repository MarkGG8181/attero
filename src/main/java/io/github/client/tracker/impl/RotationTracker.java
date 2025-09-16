package io.github.client.tracker.impl;

import io.github.client.event.data.Subscribe;
import io.github.client.event.impl.player.MotionEvent;
import io.github.client.event.impl.player.ReceivePacketEvent;
import io.github.client.event.impl.render.FlipFrameEvent;
import io.github.client.module.data.rotate.AbstractRotator;
import io.github.client.tracker.AbstractTracker;
import io.github.client.util.interfaces.IMinecraft;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.network.packet.s2c.play.EntityPositionS2CPacket;
import net.minecraft.network.packet.s2c.play.EntityS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;

import java.util.Comparator;

@SuppressWarnings("ALL")
/**
 * @author markuss
 * @since 30/06/2025
 */
public class RotationTracker extends AbstractTracker<AbstractRotator> implements IMinecraft {
    public static final RotationTracker INSTANCE = new RotationTracker();

    public static float prevYaw, prevPitch;
    public static float yaw, pitch;
    public static boolean rotating;

    private static float nextYaw, nextPitch;
    private static boolean hasNextRotation = false;

    @Subscribe
    private void onFlipFrame(final FlipFrameEvent flipFrameEvent) {
        if (mc.player == null || mc.world == null) {
            hasNextRotation = false;
            nextYaw = -1;
            nextPitch = -1;

            return;
        }

        var best = list.stream()
                .filter(AbstractRotator::isEnabled)
                .max(Comparator.comparingInt(a -> a.priority))
                .orElse(null);

        for (var rotator : list) {
            if (rotator == best) continue;
            rotator.canPerform = false;
        };

        if (best != null) {
            best.canPerform = true;
            float[] target = best.shouldRotate();
            if (target != null) {
                hasNextRotation = true;
                nextYaw = target[0];
                nextPitch = target[1];

                return;
            }
        }

        hasNextRotation = false;
        nextYaw = -1;
        nextPitch = -1;
    }

    @Subscribe
    private void onMotion(MotionEvent event) {
        if (event.isPre()) {
            if (hasNextRotation && mc.currentScreen instanceof InventoryScreen) {
                hasNextRotation = false;
            }

            if (hasNextRotation) {
                prevYaw = yaw;
                prevPitch = pitch;

                event.yaw = nextYaw;
                event.pitch = nextPitch;

                yaw = event.yaw;
                pitch = event.pitch;

                rotating = true;
            } else {
                rotating = false;

                // Not really needed as event isn't modified in this case
                // but still changed as to not desync prevYaw & prevPitch
                prevYaw = yaw;
                prevPitch = pitch;

                yaw = event.yaw;
                pitch = event.pitch;
            }
        }
    }

    @Subscribe
    private void onPacket(final ReceivePacketEvent receivePacketEvent) {
        switch (receivePacketEvent.packet) {
            case PlayerPositionLookS2CPacket packet -> hasNextRotation = false;

            case EntityPositionS2CPacket packet -> {
                if (mc.player == null || mc.world == null)
                    return;

                if (mc.world.getEntityById(packet.entityId()) == mc.player)
                    hasNextRotation = false;
            }

            case EntityS2CPacket packet -> {
                if (mc.player == null || mc.world == null)
                    return;

                if (packet.getEntity(mc.world) == mc.player)
                    hasNextRotation = false;
            }

            default -> { }
        }
    }
}