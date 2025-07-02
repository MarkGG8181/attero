package io.github.client.event.impl.player;

import io.github.client.event.Event;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class SneakPacketEvent extends Event {
    public boolean sneaking;
}