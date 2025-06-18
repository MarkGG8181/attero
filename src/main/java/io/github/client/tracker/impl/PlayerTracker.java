package io.github.client.tracker.impl;

import io.github.client.Attero;
import io.github.client.event.impl.game.TickEvent;
import io.github.client.tracker.AbstractTracker;
import io.github.client.util.interfaces.IMinecraft;
import net.minecraft.entity.player.PlayerEntity;

/**
 * @author Graph
 * Not sure if I made this correctly, but oh well
 * mark: fixed your code a lil
 * marie: added onGroundTicks
 */
public class PlayerTracker extends AbstractTracker<PlayerEntity> implements IMinecraft {
    public static int offGroundTicks;
    public static int onGroundTicks;

    private static final PlayerTracker tracker = new PlayerTracker();

    public static PlayerTracker getInstance() {
        return tracker;
    }

    @Override
    public void initialize() {
        Attero.BUS.register(this);
    }

    // Null check
    public void Tick(TickEvent event) {
        if (mc.player == null || mc.world == null) return;

        if (mc.player.isOnGround()) {
            offGroundTicks = 0;
            onGroundTicks++;
        } else {
            onGroundTicks = 0;
            offGroundTicks++;
        }
    }
}