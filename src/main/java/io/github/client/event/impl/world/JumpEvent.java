package io.github.client.event.impl.world;

import io.github.client.event.CancellableEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.entity.LivingEntity;

@AllArgsConstructor
@Getter
@Setter
public class JumpEvent extends CancellableEvent {
    private LivingEntity entity;
    private float yaw;
}