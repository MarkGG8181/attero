package io.github.client.event.impl.render;

import io.github.client.event.Event;
import lombok.AllArgsConstructor;
import net.minecraft.client.gui.DrawContext;

@AllArgsConstructor
public class Render2DEvent extends Event {
    public final DrawContext context;
}