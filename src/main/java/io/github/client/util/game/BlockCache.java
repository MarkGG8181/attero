package io.github.client.util.game;

import io.github.client.util.interfaces.IMinecraft;
import net.minecraft.block.AirBlock;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

public record BlockCache(BlockPos pos, Direction facing) implements IMinecraft {
    public Vec3d getVec3d() {
        return new Vec3d(pos().getX(), pos().getY(), pos().getZ());
    }

    public static BlockCache getCache(BlockPos pos) {
        if (!(mc.world.getBlockState(pos).getBlock() instanceof AirBlock))
            return null;

        for (int x = 0; x < 4; x++) {
            for (int z = 0; z < 4; z++) {
                for (int i = 1; i > -3; i -= 2) {
                    BlockPos checkPos = pos.add(x * i, 0, z * i);
                    if (mc.world.getBlockState(checkPos).getBlock() instanceof AirBlock) {
                        for (Direction direction : Direction.values()) {
                            BlockPos block = checkPos.offset(direction);
                            BlockState material = mc.world.getBlockState(block).getBlock().getDefaultState();

                            if (material.isSolid() && !material.isLiquid()) {
                                return new BlockCache(block, direction.getOpposite());
                            }
                        }
                    }
                }
            }
        }

        return null;
    }
}