package fag.ware.client.module.impl.misc;

import fag.ware.client.event.data.Subscribe;
import fag.ware.client.event.impl.TickEvent;
import fag.ware.client.module.AbstractModule;
import fag.ware.client.module.data.ModuleCategory;
import fag.ware.client.module.data.ModuleInfo;

@ModuleInfo(name = "AutoRespawn", category = ModuleCategory.MISC, description = "Self explanatory")
public class AutoRespawnModule extends AbstractModule {
    @Subscribe
    public void onTicky(TickEvent event) {
        if (mc.player == null || mc.world == null) return;
        if (mc.player.isDead()) {
            mc.player.requestRespawn();
        }
    }
    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }
}
