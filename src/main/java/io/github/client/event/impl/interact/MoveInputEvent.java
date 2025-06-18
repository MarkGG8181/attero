package io.github.client.event.impl.interact;

import io.github.client.event.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class MoveInputEvent extends Event {
    private float forward, strafe;
    private boolean jumping, sneaking;
    private float sneakFactor;
}