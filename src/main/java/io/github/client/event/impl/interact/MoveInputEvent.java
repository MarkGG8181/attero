package io.github.client.event.impl.interact;

import io.github.client.event.Event;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class MoveInputEvent extends Event {
    public float forward, strafe;
    public boolean jumping, sneaking;
    public float sneakFactor;
}