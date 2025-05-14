package fag.ware.client.tracker.impl;

import fag.ware.client.Fagware;
import fag.ware.client.event.impl.TickEvent;
import fag.ware.client.tracker.AbstractTracker;
import fag.ware.client.util.IMinecraft;
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

    @Override
    public void initialize() {
        Fagware.BUS.register(this);
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