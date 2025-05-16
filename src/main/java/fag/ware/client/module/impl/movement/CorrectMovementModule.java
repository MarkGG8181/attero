package fag.ware.client.module.impl.movement;

import fag.ware.client.event.data.Subscribe;
import fag.ware.client.event.impl.JumpEvent;
import fag.ware.client.event.impl.MoveInputEvent;
import fag.ware.client.event.impl.UpdateVelocityEvent;
import fag.ware.client.module.AbstractModule;
import fag.ware.client.module.data.ModuleCategory;
import fag.ware.client.module.data.ModuleInfo;
import fag.ware.client.tracker.impl.CombatTracker;
import fag.ware.client.util.game.RotationUtil;

@ModuleInfo(name = "CorrectMovement", description = "Corrects your movement", category = ModuleCategory.MOVEMENT)
public class CorrectMovementModule extends AbstractModule {

    @Subscribe(priority = 999)
    public void onInput(MoveInputEvent event) {
        RotationUtil.silentStrafe(event, CombatTracker.getInstance().yaw);
    }

    @Subscribe(priority = 999)
    public void onJump(JumpEvent event) {
        if (event.getEntity() == mc.player) {
            event.setYaw(CombatTracker.getInstance().yaw);
        }
    }

    @Subscribe(priority = 999)
    public void onUpdateVelo(UpdateVelocityEvent event) {
        event.setYaw(CombatTracker.getInstance().yaw);
    }
}