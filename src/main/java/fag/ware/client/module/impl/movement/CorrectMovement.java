package fag.ware.client.module.impl.movement;

import fag.ware.client.Fagware;
import fag.ware.client.event.data.Subscribe;
import fag.ware.client.event.impl.JumpEvent;
import fag.ware.client.event.impl.MoveInputEvent;
import fag.ware.client.event.impl.UpdateVelocityEvent;
import fag.ware.client.module.Module;
import fag.ware.client.module.data.ModuleCategory;
import fag.ware.client.module.data.ModuleInfo;
import fag.ware.client.util.player.RotationUtil;

@ModuleInfo(name = "CorrectMovement", description = "Corrects your movement", category = ModuleCategory.MOVEMENT)
public class CorrectMovement extends Module {

    @Subscribe(priority = 999)
    public void onInput(MoveInputEvent event) {
        RotationUtil.silentStrafe(event, Fagware.INSTANCE.combatTracker.yaw);
    }

    @Subscribe(priority = 999)
    public void onInput(JumpEvent event) {
        if (event.getEntity() == mc.player) {
            event.setYaw(Fagware.INSTANCE.combatTracker.yaw);
        }
    }

    @Subscribe(priority = 999)
    public void onInput(UpdateVelocityEvent event) {
        event.setYaw(Fagware.INSTANCE.combatTracker.yaw);
    }

    @Override
    public void onEnable() {
    }

    @Override
    public void onDisable() {
    }
}