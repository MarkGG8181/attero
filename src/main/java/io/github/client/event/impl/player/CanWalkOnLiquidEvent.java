package io.github.client.event.impl.player;

import io.github.client.event.AbstractEvent;
import lombok.AllArgsConstructor;
import net.minecraft.fluid.FluidState;

@AllArgsConstructor
public class CanWalkOnLiquidEvent extends AbstractEvent {
    public final FluidState fluidState;
    public boolean walkOnFluid;
}