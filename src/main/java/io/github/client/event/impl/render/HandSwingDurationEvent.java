package io.github.client.event.impl.render;

import io.github.client.event.CancellableEvent;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class HandSwingDurationEvent extends CancellableEvent {
    public int speed;
}