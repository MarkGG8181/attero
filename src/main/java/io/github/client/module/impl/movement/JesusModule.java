package io.github.client.module.impl.movement;

import io.github.client.event.data.Subscribe;
import io.github.client.event.impl.world.ComputeNextCollisionEvent;
import io.github.client.event.impl.player.MotionEvent;
import io.github.client.module.AbstractModule;
import io.github.client.module.data.ModuleCategory;
import io.github.client.module.data.ModuleInfo;
import io.github.client.module.data.setting.impl.BooleanSetting;
import io.github.client.module.data.setting.impl.StringSetting;
import io.github.client.util.game.MovementUtil;
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
                if (event.getState().getFluidState().isEmpty() || mc.options.sneakKey.isPressed() || mc.options.sneakKey.wasPressed()) return;

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
                    if (mc.options.sneakKey.isPressed() || mc.options.sneakKey.wasPressed()) return;

                    var playerBlockPos = mc.player.getBlockPos();
                    var waterBlockPos = new BlockPos(playerBlockPos.getX(), playerBlockPos.getY() - 1, playerBlockPos.getZ());

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