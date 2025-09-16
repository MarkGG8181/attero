package io.github.client.event.impl.interact;

import io.github.client.event.AbstractEvent;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class MoveButtonEvent extends AbstractEvent {
    public boolean forward, back, left, right, jump, sneak;
}