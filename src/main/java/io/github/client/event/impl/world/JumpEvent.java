package io.github.client.event.impl.world;

import io.github.client.event.CancellableEvent;
import lombok.AllArgsConstructor;
import net.minecraft.entity.LivingEntity;

@AllArgsConstructor
public class JumpEvent extends CancellableEvent {
    public LivingEntity entity;
    public float yaw;
}