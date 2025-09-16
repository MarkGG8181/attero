package io.github.client.event.impl.interact;

import io.github.client.event.AbstractEvent;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class MoveInputEvent extends AbstractEvent {
    public float forward, strafe;
    public boolean jumping, sneaking;
    public float sneakFactor;
}