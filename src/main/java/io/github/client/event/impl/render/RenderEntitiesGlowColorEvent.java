package io.github.client.event.impl.render;

import io.github.client.event.CancellableEvent;
import lombok.AllArgsConstructor;
import net.minecraft.entity.Entity;

import java.awt.*;

@AllArgsConstructor
public class RenderEntitiesGlowColorEvent extends CancellableEvent {
    public final Entity entity;
    public Color color;
}