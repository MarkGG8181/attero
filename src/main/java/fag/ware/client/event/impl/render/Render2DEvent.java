package fag.ware.client.event.impl.render;

import fag.ware.client.event.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.client.gui.DrawContext;

@AllArgsConstructor
@Getter
public class Render2DEvent extends Event {
    private final DrawContext drawContext;
}
