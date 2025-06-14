package fag.ware.client.module.impl.misc;

import fag.ware.client.event.data.Subscribe;
import fag.ware.client.event.impl.game.TickEvent;
import fag.ware.client.module.AbstractModule;
import fag.ware.client.module.data.ModuleCategory;
import fag.ware.client.module.data.ModuleInfo;
import fag.ware.client.module.data.setting.impl.NumberSetting;
import fag.ware.client.util.math.Timer;

@ModuleInfo(name = "AutoRespawn", category = ModuleCategory.MISC, description = "Respawns automatically on death")
public class AutoRespawnModule extends AbstractModule {
    private final NumberSetting delay = new NumberSetting("Delay", 30, 0, 300);

    private final Timer timer = new Timer();

    @Subscribe
    public void onTick(TickEvent event) {
        if (mc.player == null || mc.world == null) return;

        if (mc.player.isDead() && timer.hasElapsed(delay.toInt(), true)) {
            mc.player.requestRespawn();
        }
    }

    public void onDisable() {
        timer.reset();
    }
}