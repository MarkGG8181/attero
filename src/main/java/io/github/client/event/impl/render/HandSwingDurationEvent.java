package io.github.client.event.impl.render;

import io.github.client.event.CancellableEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class HandSwingDurationEvent extends CancellableEvent {
    private int speed;
}