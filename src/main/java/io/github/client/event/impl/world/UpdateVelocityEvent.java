package io.github.client.event.impl.world;

import io.github.client.event.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class UpdateVelocityEvent extends Event {
    private float yaw;
    private float strafe, forward, friction;
}