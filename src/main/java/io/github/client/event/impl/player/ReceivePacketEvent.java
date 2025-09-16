package io.github.client.event.impl.player;

import io.github.client.event.AbstractCancellableEvent;
import lombok.AllArgsConstructor;
import net.minecraft.network.packet.Packet;

@AllArgsConstructor
public class ReceivePacketEvent extends AbstractCancellableEvent {
    public final Packet<?> packet;
}