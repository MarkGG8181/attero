package io.github.client.module.impl.movement;

import io.github.client.event.data.Subscribe;
import io.github.client.event.impl.player.MotionEvent;
import io.github.client.module.AbstractModule;
import io.github.client.module.data.ModuleCategory;
import io.github.client.module.data.ModuleInfo;
import io.github.client.module.data.setting.impl.StringSetting;
import io.github.client.util.game.MovementUtil;

@SuppressWarnings("ALL")
@ModuleInfo(name = "Speed", category = ModuleCategory.MOVEMENT, description = "Makes you fast")
public class SpeedModule extends AbstractModule {
    private final StringSetting mode = new StringSetting("Mode", "Strafe", "Strafe", "Legit", "NCP Y-Port");

    @Subscribe
    private void onMotion(MotionEvent event) {
        if (event.isPre()) {
            switch (mode.getValue()) {
                case "Strafe" -> {
                    mc.options.jumpKey.setPressed(MovementUtil.isMoving());
                    mc.options.sprintKey.setPressed(true);
                    MovementUtil.strafe();
                }

                case "Legit" -> {
                    mc.options.jumpKey.setPressed(MovementUtil.isMoving());
                    mc.options.sprintKey.setPressed(true);
                }
            }
        } else {
            switch (mode.getValue()) {
                case "NCP Y-Port" -> {
                    var speedAmplifier = MovementUtil.getSpeedAmplifier();

                    MovementUtil.strafe();

                    MovementUtil.multiplyMotion(
                            0.0045 + 1 + speedAmplifier * 0.02f,
                            1.0f,
                            0.0045 + 1 + speedAmplifier * 0.02f
                    );

                    var pushDown = true;

                    if (mc.player.isOnGround()
                            && MovementUtil.isMoving()) {
                        var boost = (float) (speedAmplifier * 0.065f);
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