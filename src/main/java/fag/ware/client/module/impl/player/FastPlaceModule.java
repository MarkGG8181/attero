package fag.ware.client.module.impl.player;

import fag.ware.client.event.data.Subscribe;
import fag.ware.client.event.impl.game.TickEvent;
import fag.ware.client.mixin.MinecraftClientAccessor;
import fag.ware.client.module.AbstractModule;
import fag.ware.client.module.data.ModuleCategory;
import fag.ware.client.module.data.ModuleInfo;

@ModuleInfo(name = "FastPlace", description = "Removes right click delay", category = ModuleCategory.PLAYER)
public class FastPlaceModule extends AbstractModule {
    @Subscribe
    public void onTick(TickEvent event) {
        ((MinecraftClientAccessor) mc).setItemUseCooldown(0);
    }
}