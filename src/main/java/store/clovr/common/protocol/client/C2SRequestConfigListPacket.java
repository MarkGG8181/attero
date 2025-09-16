package store.clovr.common.protocol.client;

import io.netty.buffer.ByteBuf;
import store.clovr.common.protocol.Packet;

public class C2SRequestConfigListPacket implements Packet {
    @Override public void write(ByteBuf buf) {}
    @Override public void read(ByteBuf buf) {}
}