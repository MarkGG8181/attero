package fag.ware.client.module.impl.movement;

import fag.ware.client.event.data.Subscribe;
import fag.ware.client.event.impl.MotionEvent;
import fag.ware.client.module.AbstractModule;
import fag.ware.client.module.data.ModuleCategory;
import fag.ware.client.module.data.ModuleInfo;
import fag.ware.client.module.data.setting.impl.StringSetting;
import fag.ware.client.util.game.MovementUtil;

@SuppressWarnings("ALL")
@ModuleInfo(name = "Speed", category = ModuleCategory.MOVEMENT, description = "Makes you fast")
public class SpeedModule extends AbstractModule {
    private final StringSetting mode = new StringSetting("Mode", "Strafe", "Strafe", "Legit", "NCP OnGround");

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
        } else {
            switch (mode.getValue()) {
                case "NCP OnGround" -> {
                    double speedAmplifier = MovementUtil.getSpeedAmplifier();

                    MovementUtil.strafe();

                    MovementUtil.multiplyMotion(
                            0.0045 + 1 + speedAmplifier * 0.02f,
                            1.0f,
                            0.0045 + 1 + speedAmplifier * 0.02f
                    );

                    boolean pushDown = true;

                    if (mc.player.isOnGround()
                            && MovementUtil.isMoving()) {
                        float boost = (float) (speedAmplifier * 0.065f);
                        mc.player.jump();
                        if (!pushDown) return;
                        setTimer(1.405f);
                        MovementUtil.setSpeed(((mc.player.age % 10 > 7) ? 0.4f : 0.325f) + boost);
                        MovementUtil.multiplyMotion(
                                0.01 + 1 + speedAmplifier * 0.175f,
                                1.0f,
                                0.01 + 1 + speedAmplifier * 0.175f
                        );
                    } else if (!mc.player.isOnGround()
                            && MovementUtil.getMotionY() > 0.3) {
                        setTimer(0.85f);
                        MovementUtil.setMotionY(-0.42);
                        MovementUtil.offsetPosition(0, -0.4, 0);
                    }

                    MovementUtil.strafe();
                }
            }
        }
    }

    @Override
    public void onDisable() {
        mc.options.jumpKey.setPressed(false);
        mc.options.sprintKey.setPressed(false);
        setTimer(1.0f);
    }

    @Override
    public String getSuffix() {
        return mode.getValue();
    }
}