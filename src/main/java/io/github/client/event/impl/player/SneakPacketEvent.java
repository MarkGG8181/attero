package io.github.client.event.impl.player;

import io.github.client.event.AbstractEvent;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class SneakPacketEvent extends AbstractEvent {
    public boolean sneaking;
}