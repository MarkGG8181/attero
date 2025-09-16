package io.github.client.event.impl.render;

import io.github.client.event.AbstractCancellableEvent;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class HandSwingDurationEvent extends AbstractCancellableEvent {
    public int speed;
}