package io.github.client.event.impl.render;

import io.github.client.event.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.client.gui.DrawContext;

@AllArgsConstructor
@Getter
public class Render2DEvent extends Event {
    private final DrawContext drawContext;
}