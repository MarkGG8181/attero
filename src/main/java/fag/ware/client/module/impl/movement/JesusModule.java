package fag.ware.client.module.impl.movement;

import fag.ware.client.event.data.Subscribe;
import fag.ware.client.event.impl.world.ComputeNextCollisionEvent;
import fag.ware.client.event.impl.player.MotionEvent;
import fag.ware.client.module.AbstractModule;
import fag.ware.client.module.data.ModuleCategory;
import fag.ware.client.module.data.ModuleInfo;
import fag.ware.client.module.data.setting.impl.BooleanSetting;
import fag.ware.client.module.data.setting.impl.StringSetting;
import fag.ware.client.util.game.MovementUtil;
import net.minecraft.block.Blocks;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShapes;

@SuppressWarnings("ALL")
@ModuleInfo(name = "Jesus", description = "Allows you traverse water", category = ModuleCategory.MOVEMENT)
public class JesusModule extends AbstractModule {
    private final StringSetting mode = new StringSetting("Mode", "Verus", "Verus", "Collision");
    private final BooleanSetting boost = (BooleanSetting) new BooleanSetting("Speed boost", false).hide(() -> !mode.is("Verus"));

    private boolean water;

    @Subscribe
    public void onComputeCollision(ComputeNextCollisionEvent event) {
        switch (mode.getValue()) {
            case "Collision" -> {
                if (event.getState().getFluidState().isEmpty()) return;

                if ((event.getState().getBlock() == Blocks.WATER | event.getState().getFluidState().getFluid() == Fluids.WATER) && !mc.player.isTouchingWater() && event.getPos().getY() <= mc.player.getY() - 1) {
                    event.setShape(VoxelShapes.fullCube());
                }
            }
        }
    }

    @Subscribe
    public void onMotion(MotionEvent event) {
        if (event.isPre()) {
            switch (mode.getValue()) {
                case "Verus" -> {
                    BlockPos playerBlockPos = mc.player.getBlockPos();
                    BlockPos waterBlockPos = new BlockPos(playerBlockPos.getX(), playerBlockPos.getY() - 1, playerBlockPos.getZ());

                    if (mc.player.isTouchingWater() || mc.world.getBlockState(waterBlockPos).isLiquid()) {
                        water = true;
                        mc.player.setVelocity(mc.player.getVelocity().x, 0.0d, mc.player.getVelocity().z);
                        if (MovementUtil.isMoving()) {
                            MovementUtil.setSpeed(boost.getValue() ? 2 : 0.5);
                        } else {
                            mc.player.setVelocity(0, mc.player.getVelocity().y, 0);
                        }
                    } else {
                        if (water) {
                            mc.player.setVelocity(0, mc.player.getVelocity().y, 0);
                            water = false;
                        }
                    }
                }
            }
        }
    }

    @Override
    public String getSuffix() {
        return mode.getValue();
    }
}