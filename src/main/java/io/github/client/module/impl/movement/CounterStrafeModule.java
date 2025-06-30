package io.github.client.module.impl.movement;

import io.github.client.event.data.Subscribe;
import io.github.client.event.impl.game.TickEvent;
import io.github.client.module.AbstractModule;
import io.github.client.module.data.ModuleCategory;
import io.github.client.module.data.ModuleInfo;

@SuppressWarnings("ALL")
@ModuleInfo(name = "CounterStrafe", category = ModuleCategory.MOVEMENT, description = "CS:GO Counterstrafing")
public class CounterStrafeModule extends AbstractModule {
    @Subscribe
    private void Tick(TickEvent event) {
        if (mc.player == null || mc.world == null) return;

        if (mc.player.isOnGround() && !isMoving()) {
            mc.player.setVelocity(0, mc.player.getVelocity().y, 0);
        }
    }

    private boolean isMoving() {
        return mc.options.forwardKey.isPressed()
                || mc.options.backKey.isPressed()
                || mc.options.leftKey.isPressed()
                || mc.options.rightKey.isPressed();
    }
}