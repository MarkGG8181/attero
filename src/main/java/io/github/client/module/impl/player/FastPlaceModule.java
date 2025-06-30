package io.github.client.module.impl.player;

import io.github.client.event.data.Subscribe;
import io.github.client.event.impl.game.TickEvent;
import io.github.client.module.AbstractModule;
import io.github.client.module.data.ModuleCategory;
import io.github.client.module.data.ModuleInfo;

@ModuleInfo(name = "FastPlace", description = "Removes right click delay", category = ModuleCategory.PLAYER)
public class FastPlaceModule extends AbstractModule {
    @Subscribe
    private void onTick(TickEvent ignoredEvent) {
        mc.itemUseCooldown = 0;
    }
}