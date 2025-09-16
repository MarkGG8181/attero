package io.github.client.event.impl.render;

import io.github.client.event.AbstractCancellableEvent;
import lombok.AllArgsConstructor;
import net.minecraft.entity.Entity;

import java.awt.*;

@AllArgsConstructor
public class RenderEntitiesGlowColorEvent extends AbstractCancellableEvent {
    public final Entity entity;
    public Color color;
}