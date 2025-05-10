package fag.ware.client.tracker.impl;

import fag.ware.client.Fagware;
import fag.ware.client.event.impl.TickEvent;
import fag.ware.client.tracker.Tracker;
import fag.ware.client.util.IMinecraft;
import net.minecraft.entity.player.PlayerEntity;


/**
 * @Author Graph
 *Not sure if i made this correctly but oh well
 */
public class PlayerTracker extends Tracker<PlayerEntity> implements IMinecraft {
    public static int offGroundTicks;
    @Override
    public void initialize() {
        Fagware.BUS.register(this);
    }
    // Null check
    public void Tick(TickEvent event) {
        if (mc.player == null || mc.world == null) return;
    }

    public static int getOffGroundTicks() {
        if (mc.player.isOnGround()) {
            offGroundTicks = 0;
        } else {
            offGroundTicks++;
        }
        return offGroundTicks;
    }
}
