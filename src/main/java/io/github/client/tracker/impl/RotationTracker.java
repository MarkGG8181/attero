package io.github.client.tracker.impl;

import io.github.client.event.data.Subscribe;
import io.github.client.event.impl.player.MotionEvent;
import io.github.client.module.data.rotate.AbstractRotator;
import io.github.client.tracker.AbstractTracker;
import io.github.client.util.java.interfaces.IMinecraft;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;

import java.util.Comparator;

@SuppressWarnings("ALL")
public class RotationTracker extends AbstractTracker<AbstractRotator> implements IMinecraft {
    public static final RotationTracker INSTANCE = new RotationTracker();

    public static float prevYaw, prevPitch;
    public static float yaw, pitch;
    public static boolean rotating;

    @Subscribe
    private void onMotion(MotionEvent event) {
        if (event.isPre()) {
            if (rotating && mc.currentScreen instanceof InventoryScreen) {
                rotating = false;
                return;
            }

            AbstractRotator best = list.stream()
                    .filter(AbstractRotator::isEnabled)
                    .filter(r -> r.shouldRotate() != null)
                    .max(Comparator.comparingInt(a -> a.priority))
                    .orElse(null);

            if (best != null) {
                float[] target = best.shouldRotate();
                if (target != null) {
                    rotating = true;

                    prevYaw = yaw;
                    prevPitch = pitch;

                    yaw = target[0];
                    pitch = target[1];

                    event.yaw = yaw;
                    event.pitch = pitch;
                    return;
                }
            }

            rotating = false;
        }
    }
}