package fag.ware.client.event.impl;

import fag.ware.client.event.CancellableEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;

@AllArgsConstructor
@Getter
@Setter
public class ComputeNextCollisionEvent extends CancellableEvent {
    private BlockState state;
    private BlockPos pos;
    private VoxelShape shape;
}