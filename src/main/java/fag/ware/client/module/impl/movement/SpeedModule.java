package fag.ware.client.module.impl.movement;

import fag.ware.client.event.data.Subscribe;
import fag.ware.client.event.impl.MotionEvent;
import fag.ware.client.module.Module;
import fag.ware.client.module.data.ModuleCategory;
import fag.ware.client.module.data.ModuleInfo;
import fag.ware.client.module.data.setting.impl.StringSetting;
import fag.ware.client.util.player.MovementUtil;

@ModuleInfo(name = "Speed", category = ModuleCategory.MOVEMENT, description = "Makes you fast")
public class SpeedModule extends Module {
    public final StringSetting speedMode = new StringSetting("Mode", "Strafe", "Strafe", "Legit");

    @Subscribe
    public void onMotion(MotionEvent event) {
        mc.options.jumpKey.setPressed(true);
        mc.options.sprintKey.setPressed(true);

        if (event.isPre()) {
            switch (speedMode.getValue()) {
                case "Strafe" -> MovementUtil.setSpeed(0.27);

                case "Legit" -> {
                }
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
