package io.github.client.module.impl.movement;

import io.github.client.event.data.Subscribe;
import io.github.client.event.impl.world.JumpEvent;
import io.github.client.event.impl.interact.MoveInputEvent;
import io.github.client.event.impl.world.UpdateVelocityEvent;
import io.github.client.module.AbstractModule;
import io.github.client.module.data.ModuleCategory;
import io.github.client.module.data.ModuleInfo;
import io.github.client.tracker.impl.RotationTracker;
import io.github.client.util.game.RotationUtil;

@ModuleInfo(name = "CorrectMovement", description = "Corrects your movement", category = ModuleCategory.MOVEMENT)
public class CorrectMovementModule extends AbstractModule {
    @Subscribe
    private void onInput(MoveInputEvent event) {
        RotationUtil.correctMovement(event, RotationTracker.yaw);
    }

    @Subscribe
    private void onJump(JumpEvent event) {
        if (event.entity == mc.player) {
            event.yaw = RotationTracker.yaw;
        }
    }

    @Subscribe
    private void onUpdateVelo(UpdateVelocityEvent event) {
        event.yaw = RotationTracker.yaw;
    }
}