package io.github.client.event.impl.world;

import io.github.client.event.AbstractEvent;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UpdateVelocityEvent extends AbstractEvent {
    public float yaw;
    public double strafe, forward;
    public float friction;
}