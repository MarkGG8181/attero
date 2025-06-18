package io.github.client.module.impl.movement;

import io.github.client.event.data.Subscribe;
import io.github.client.event.impl.world.JumpEvent;
import io.github.client.event.impl.interact.MoveInputEvent;
import io.github.client.event.impl.world.UpdateVelocityEvent;
import io.github.client.module.AbstractModule;
import io.github.client.module.data.ModuleCategory;
import io.github.client.module.data.ModuleInfo;
import io.github.client.tracker.impl.CombatTracker;
import io.github.client.util.game.RotationUtil;

@ModuleInfo(name = "CorrectMovement", description = "Corrects your movement", category = ModuleCategory.MOVEMENT)
public class CorrectMovementModule extends AbstractModule {
    @Subscribe(priority = 999)
    public void onInput(MoveInputEvent event) {
        RotationUtil.correctMovement(event, CombatTracker.getInstance().yaw);
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