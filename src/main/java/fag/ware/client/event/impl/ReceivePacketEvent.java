package fag.ware.client.event.impl;

import fag.ware.client.event.CancellableEvent;
import lombok.AllArgsConstructor;
import net.minecraft.network.packet.Packet;

@AllArgsConstructor
public class ReceivePacketEvent extends CancellableEvent {
    private final Packet<?> packet;
}
