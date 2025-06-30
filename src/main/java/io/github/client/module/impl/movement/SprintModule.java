package io.github.client.module.impl.movement;

import io.github.client.event.data.Subscribe;
import io.github.client.event.impl.player.UpdateEvent;
import io.github.client.module.AbstractModule;
import io.github.client.module.data.ModuleCategory;
import io.github.client.module.data.ModuleInfo;

@ModuleInfo(name = "Sprint", category = ModuleCategory.MOVEMENT, description = "Makes you always sprint")
public class SprintModule extends AbstractModule {
    @Subscribe
    private void onUpdate(UpdateEvent event) {
        mc.options.sprintKey.setPressed(true);
    }

    public void onDisable() {
        mc.options.sprintKey.setPressed(false);
    }
}