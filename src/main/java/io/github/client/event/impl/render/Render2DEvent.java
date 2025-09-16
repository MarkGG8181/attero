package io.github.client.event.impl.render;

import io.github.client.event.AbstractEvent;
import lombok.AllArgsConstructor;
import net.minecraft.client.gui.DrawContext;

@AllArgsConstructor
public class Render2DEvent extends AbstractEvent {
    public final DrawContext context;
}