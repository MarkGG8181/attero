package fag.ware.client.module.impl.movement;

import fag.ware.client.event.data.Subscribe;
import fag.ware.client.event.impl.MotionEvent;
import fag.ware.client.event.impl.UpdateEvent;
import fag.ware.client.module.Module;
import fag.ware.client.module.data.ModuleCategory;
import fag.ware.client.module.data.ModuleInfo;
import fag.ware.client.util.player.MovementUtil;

@ModuleInfo(name = "Speed", category = ModuleCategory.MOVEMENT, description = "Makes you always sprint")
public class SpeedModule extends Module {

    @Subscribe
    public void onMotion(MotionEvent event) {
        mc.options.jumpKey.setPressed(true);
        mc.options.sprintKey.setPressed(true);
        
        if (event.isPre()) {
            if (event.isOnGround()) {
                MovementUtil.setSpeed(0.20);
            }
        }
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {
        mc.options.jumpKey.setPressed(false);
        mc.options.sprintKey.setPressed(false);
    }
}
