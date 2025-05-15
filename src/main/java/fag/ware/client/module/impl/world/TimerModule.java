package fag.ware.client.module.impl.world;

import fag.ware.client.event.data.Subscribe;
import fag.ware.client.event.impl.MotionEvent;
import fag.ware.client.event.impl.UpdateEvent;
import fag.ware.client.module.AbstractModule;
import fag.ware.client.module.data.ModuleCategory;
import fag.ware.client.module.data.ModuleInfo;
import fag.ware.client.module.data.setting.impl.NumberSetting;
import fag.ware.client.util.ITimerAccessor;
import net.minecraft.block.Blocks;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.util.math.BlockPos;

@ModuleInfo(name = "Timer", category = ModuleCategory.WORLD, description = "Modifies game speed")
public class TimerModule extends AbstractModule {
    private final NumberSetting gameSpeed = new NumberSetting("Game speed", 1.0, 0.1, 4);

    @Subscribe
    public void onUpdate(UpdateEvent event) {
        setTimer(gameSpeed.toFloat());
    }

    @Override
    public void onEnable() {
    }

    @Override
    public void onDisable() {
        setTimer(1.0f);
    }
}