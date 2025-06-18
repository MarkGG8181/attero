package io.github.client.module.impl.world;

import io.github.client.event.data.Subscribe;
import io.github.client.event.impl.player.UpdateEvent;
import io.github.client.module.AbstractModule;
import io.github.client.module.data.ModuleCategory;
import io.github.client.module.data.ModuleInfo;
import io.github.client.module.data.setting.impl.NumberSetting;
import io.github.client.util.math.MathUtil;

@ModuleInfo(name = "Timer", category = ModuleCategory.WORLD, description = "Modifies game speed")
public class TimerModule extends AbstractModule {
    private final NumberSetting gameSpeed = new NumberSetting("Game speed", 1.0, 0.1, 4);

    @Subscribe
    public void onUpdate(UpdateEvent ignoredEvent) {
        setTimer(gameSpeed.toFloat());
    }

    public void onDisable() {
        setTimer(1.0f);
    }

    @Override
    public String getSuffix() {
        return "" + MathUtil.round(gameSpeed.toDouble(), 1);
    }
}