package io.github.client.event.impl.world;

import io.github.client.event.AbstractCancellableEvent;
import lombok.AllArgsConstructor;
import net.minecraft.entity.LivingEntity;

@AllArgsConstructor
public class JumpEvent extends AbstractCancellableEvent {
    public LivingEntity entity;
    public float yaw;
}