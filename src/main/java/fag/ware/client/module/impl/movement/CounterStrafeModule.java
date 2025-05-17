package fag.ware.client.module.impl.movement;

import fag.ware.client.event.data.Subscribe;
import fag.ware.client.event.impl.TickEvent;
import fag.ware.client.module.AbstractModule;
import fag.ware.client.module.data.ModuleCategory;
import fag.ware.client.module.data.ModuleInfo;
import fag.ware.client.util.game.MovementUtil;

@SuppressWarnings("ALL")
@ModuleInfo(name = "CounterStrafe", category = ModuleCategory.MOVEMENT, description = "CS:GO Counterstrafing")
public class CounterStrafeModule extends AbstractModule {
    @Subscribe
    public void Tick(TickEvent event) {
        if (mc.player == null || mc.world == null) return;

        if (mc.player.isOnGround() && !MovementUtil.isMoving()) {
            MovementUtil.setMotionX(0);
            MovementUtil.setMotionZ(0);
        }
    }
}