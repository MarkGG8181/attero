package io.github.client.event.impl.world;

import io.github.client.event.AbstractCancellableEvent;
import lombok.AllArgsConstructor;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;

@AllArgsConstructor
public class ComputeNextCollisionEvent extends AbstractCancellableEvent {
    public BlockState state;
    public BlockPos pos;
    public VoxelShape shape;
}