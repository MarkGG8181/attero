package fag.ware.client.module.impl.movement;

import fag.ware.client.event.data.Subscribe;
import fag.ware.client.event.impl.UpdateEvent;
import fag.ware.client.module.AbstractModule;
import fag.ware.client.module.data.ModuleCategory;
import fag.ware.client.module.data.ModuleInfo;
import fag.ware.client.module.data.setting.impl.StringSetting;
import fag.ware.client.util.player.MovementUtil;

/**
 * All credits to the NCP longjump go to sigma client (version 4.11)
 */
@ModuleInfo(name = "Longjump", description = "Fly but shorter", category = ModuleCategory.MOVEMENT)
public class LongJumpModule extends AbstractModule
{
    private final StringSetting mode = new StringSetting("Mode", "NCP", "NCP");
    private int sigmaNcpStage;
    private float sigmaNcpAir;
    private int sigmaNcpGroundTicks;

    @Override
    public void onEnable()
    {
        sigmaNcpAir = 0;
        sigmaNcpStage = 1;
    }

    @Subscribe
    public void onUpdate(UpdateEvent event)
    {
        if (mc.player == null) return; // idk where this would unironically happen but intellij kept crying

        if (mode.is("NCP"))
        {
            float speedEffect = 1f + MovementUtil.getSpeedEffect() * 0.45f;
            if (mc.player.isOnGround())
            {
                if (MovementUtil.isMoving())
                {
                    if (sigmaNcpGroundTicks > 0)
                    {
                        sigmaNcpGroundTicks = 0;
                        this.toggle();
                        return;
                    }

                    sigmaNcpStage = 1;
                    sigmaNcpGroundTicks = 1;
                    mc.player.jump();
                }

                sigmaNcpAir = 0;
            }

            double speed = (0.8f + MovementUtil.getSpeedEffect() * 0.2f) - sigmaNcpAir / 25;
            if (speed < MovementUtil.defaultSpeed())
            {
                speed = MovementUtil.defaultSpeed();
            }
            if (sigmaNcpStage < 4)
            {
                speed = MovementUtil.defaultSpeed();
            }

            MovementUtil.setSpeed(speed);
            MovementUtil.setMotionY(getMotion(sigmaNcpStage));

            if (sigmaNcpStage > 0)
            {
                sigmaNcpStage++;
            }

            sigmaNcpAir += speedEffect;
        }
    }

    double getMotion(int stage)
    {
        double[] mot = {0.396,-0.122,-0.1,0.423, 0.35,0.28,0.217,0.15, 0.025,-0.00625,-0.038,-0.0693,-0.102,-0.13,
                -0.018,-0.1,-0.117,-0.14532,-0.1334, -0.1581, -0.183141, -0.170695, -0.195653, -0.221, -0.209, -0.233, -0.25767,
                -0.314917, -0.371019, -0.426};

        stage--;

        if(stage >= 0 && stage < mot.length)
        {
            return mot[stage];
        }
        else
        {
            assert mc.player != null; // NIGGA IT ISNT NULL
            return mc.player.getVelocity().y;
        }
    }
}
