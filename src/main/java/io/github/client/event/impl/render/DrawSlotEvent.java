package io.github.client.event.impl.render;

import io.github.client.event.AbstractEvent;
import lombok.AllArgsConstructor;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.screen.slot.Slot;

@AllArgsConstructor
public class DrawSlotEvent extends AbstractEvent {
    public final DrawContext context;
    public final Slot slot;
}