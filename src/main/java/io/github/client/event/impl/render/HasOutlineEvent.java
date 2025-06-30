package io.github.client.event.impl.render;

import io.github.client.event.CancellableEvent;
import lombok.AllArgsConstructor;
import net.minecraft.entity.Entity;

@AllArgsConstructor
public class HasOutlineEvent extends CancellableEvent {
    public final Entity entity;
}