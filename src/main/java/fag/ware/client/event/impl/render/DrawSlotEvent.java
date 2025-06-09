package fag.ware.client.event.impl.render;

import fag.ware.client.event.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.screen.slot.Slot;

@AllArgsConstructor
@Getter
public class DrawSlotEvent extends Event {
    private final DrawContext context;
    private final Slot slot;
}