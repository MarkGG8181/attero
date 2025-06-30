package io.github.client.event.impl.world;

import io.github.client.event.Event;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UpdateVelocityEvent extends Event {
    public float yaw;
    public float strafe, forward, friction;
}