package fag.ware.client.module.impl.movement;

import fag.ware.client.event.data.Subscribe;
import fag.ware.client.event.impl.MotionEvent;
import fag.ware.client.module.AbstractModule;
import fag.ware.client.module.data.ModuleCategory;
import fag.ware.client.module.data.ModuleInfo;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;

@ModuleInfo(name = "Eagle", category = ModuleCategory.MOVEMENT, description = "Sneaks at the edge of a block")
public class EagleModule extends AbstractModule {
    @Subscribe
    public void onMotion(MotionEvent event) {
        if (event.isPre()) {
            mc.options.sneakKey.setPressed(mc.world.getBlockState(new BlockPos((int) mc.player.getX(), (int) (mc.player.getY() - 1D), (int) mc.player.getZ())).getBlock() == Blocks.AIR);
        }
    }

    @Override
    public void onEnable() {
    }

    @Override
    public void onDisable() {
    }
}