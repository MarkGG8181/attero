package io.github.client.module.impl.misc;

import io.github.client.event.data.Subscribe;
import io.github.client.event.impl.game.TickEvent;
import io.github.client.module.AbstractModule;
import io.github.client.module.data.ModuleCategory;
import io.github.client.module.data.ModuleInfo;
import io.github.client.module.data.setting.impl.NumberSetting;
import io.github.client.util.java.math.Timer;

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