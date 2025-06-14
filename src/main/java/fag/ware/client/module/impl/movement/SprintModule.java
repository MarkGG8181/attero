package fag.ware.client.module.impl.movement;

import fag.ware.client.event.data.Subscribe;
import fag.ware.client.event.impl.player.UpdateEvent;
import fag.ware.client.module.AbstractModule;
import fag.ware.client.module.data.ModuleCategory;
import fag.ware.client.module.data.ModuleInfo;

@ModuleInfo(name = "Sprint", category = ModuleCategory.MOVEMENT, description = "Makes you always sprint")
public class SprintModule extends AbstractModule {
    @Subscribe
    public void onUpdate(UpdateEvent event) {
        mc.options.sprintKey.setPressed(true);
    }

    public void onDisable() {
        mc.options.sprintKey.setPressed(false);
    }
}