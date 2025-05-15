package fag.ware.client.module.impl.movement;

import fag.ware.client.event.data.Subscribe;
import fag.ware.client.event.impl.MotionEvent;
import fag.ware.client.module.AbstractModule;
import fag.ware.client.module.data.ModuleCategory;
import fag.ware.client.module.data.ModuleInfo;
import fag.ware.client.module.data.setting.impl.NumberSetting;
import fag.ware.client.util.math.Timer;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;

@ModuleInfo(name = "Eagle", category = ModuleCategory.MOVEMENT, description = "Sneaks at the edge of a block")
public class EagleModule extends AbstractModule {
    private final NumberSetting delay = new NumberSetting("Sneak Delay", 30, 0, 300);

    private final Timer timer = new Timer();

    @Subscribe
    public void onMotion(MotionEvent event) {
        if (!event.isPre()) return;

        double x = mc.player.getX();
        double y = mc.player.getY() - 0.1;
        double z = mc.player.getZ();

        boolean onEdge = false;

        double[][] offsets = {
                { 0.3, 0.3 },
                { -0.3, 0.3 },
                { 0.3, -0.3 },
                { -0.3, -0.3 }
        };

        for (double[] offset : offsets) {
            int blockX = (int) Math.floor(x + offset[0]);
            int blockY = (int) Math.floor(y);
            int blockZ = (int) Math.floor(z + offset[1]);

            BlockPos pos = new BlockPos(blockX, blockY, blockZ);
            if (mc.world.getBlockState(pos).getBlock() == Blocks.AIR) {
                onEdge = true;
                break;
            }
        }

        if (timer.hasElapsed(delay.toInt()))
            mc.options.sneakKey.setPressed(onEdge);
    }

    @Override
    public void onEnable() {
    }

    @Override
    public void onDisable() {
        mc.options.sneakKey.setPressed(false);
    }
}