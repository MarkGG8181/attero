package fag.ware.client.module.impl.world;

import fag.ware.client.event.data.Subscribe;
import fag.ware.client.event.impl.player.UpdateEvent;
import fag.ware.client.module.AbstractModule;
import fag.ware.client.module.data.ModuleCategory;
import fag.ware.client.module.data.ModuleInfo;
import fag.ware.client.module.data.setting.impl.NumberSetting;
import fag.ware.client.util.math.MathUtil;

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