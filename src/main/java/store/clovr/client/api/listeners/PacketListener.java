package store.clovr.client.api.listeners;

import store.clovr.common.protocol.Packet;

@FunctionalInterface
public interface PacketListener<T extends Packet> {
    void onPacketReceived(T packet);
}