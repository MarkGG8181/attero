package io.github.client.event.impl.render;

import io.github.client.event.AbstractCancellableEvent;
import lombok.AllArgsConstructor;
import net.minecraft.entity.Entity;

@AllArgsConstructor
public class RenderEntityEvent extends AbstractCancellableEvent {
    public final Entity entity;
}