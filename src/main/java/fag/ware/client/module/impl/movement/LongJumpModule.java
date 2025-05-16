package fag.ware.client.module.impl.movement;

import fag.ware.client.event.data.Subscribe;
import fag.ware.client.event.impl.UpdateEvent;
import fag.ware.client.module.AbstractModule;
import fag.ware.client.module.data.ModuleCategory;
import fag.ware.client.module.data.ModuleInfo;
import fag.ware.client.module.data.setting.impl.StringSetting;
import fag.ware.client.util.game.MovementUtil;

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
        sigmaNcpStage = 0;
    }

    @Subscribe
    public void onUpdate(UpdateEvent event)
    {
        if (mc.player == null) return; // idk where this would unironically happen but intellij kept crying

        if (mode.is("NCP"))
        {
            float speedAmpl = MovementUtil.getSpeedAmplifier2();

            float speedEffect = 1f + speedAmpl * 0.45f;
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

            double speed = (0.8f + speedAmpl * 0.2f) - sigmaNcpAir / 25;

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
        double[] mot = {0.345,0.2699,0.183,0.103,0.024,-0.008,-0.04,-0.072,-0.104,-0.13,-0.019,-0.097};

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
