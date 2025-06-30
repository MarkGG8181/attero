package io.github.client.event.impl.interact;

import io.github.client.event.Event;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class MoveButtonEvent extends Event {
    public boolean forward, back, left, right, jump, sneak;
}