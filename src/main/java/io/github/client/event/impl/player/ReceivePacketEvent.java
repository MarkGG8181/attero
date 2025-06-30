package io.github.client.event.impl.player;

import io.github.client.event.CancellableEvent;
import lombok.AllArgsConstructor;
import net.minecraft.network.packet.Packet;

@AllArgsConstructor
public class ReceivePacketEvent extends CancellableEvent {
    public final Packet<?> packet;
}