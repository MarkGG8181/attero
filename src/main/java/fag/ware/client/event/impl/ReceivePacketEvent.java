package fag.ware.client.event.impl;

import fag.ware.client.event.CancellableEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.network.packet.Packet;

@AllArgsConstructor
@Getter
public class ReceivePacketEvent extends CancellableEvent {
    private final Packet<?> packet;
}
