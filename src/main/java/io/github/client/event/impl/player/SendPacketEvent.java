package io.github.client.event.impl.player;

import io.github.client.event.CancellableEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.network.packet.Packet;

@AllArgsConstructor
@Getter
public class SendPacketEvent extends CancellableEvent {
    private final Packet<?> packet;
}