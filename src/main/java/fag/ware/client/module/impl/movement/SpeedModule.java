package fag.ware.client.module.impl.movement;

import fag.ware.client.event.data.Subscribe;
import fag.ware.client.event.impl.MotionEvent;
import fag.ware.client.module.AbstractModule;
import fag.ware.client.module.data.ModuleCategory;
import fag.ware.client.module.data.ModuleInfo;
import fag.ware.client.module.data.setting.impl.StringSetting;
import fag.ware.client.util.player.MovementUtil;

@ModuleInfo(name = "Speed", category = ModuleCategory.MOVEMENT, description = "Makes you fast")
public class SpeedModule extends AbstractModule {
    private final StringSetting mode = new StringSetting("Mode", "Strafe", "Strafe", "Legit");

    @Subscribe
    public void onMotion(MotionEvent event) {
        if (event.isPre()) {
            switch (mode.getValue()) {
                case "Strafe" -> {
                    mc.options.jumpKey.setPressed(MovementUtil.isMoving());
                    mc.options.sprintKey.setPressed(true);
                    MovementUtil.setSpeed(0.27);
                }

                case "Legit" -> {
                    mc.options.jumpKey.setPressed(MovementUtil.isMoving());
                    mc.options.sprintKey.setPressed(true);
                }
            }
        }
    }

    @Override
    public void onDisable() {
        mc.options.jumpKey.setPressed(false);
        mc.options.sprintKey.setPressed(false);
    }
}