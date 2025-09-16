package store.clovr.common.protocol.client;

import io.netty.buffer.ByteBuf;
import store.clovr.common.protocol.Packet;

public class C2SRequestConfigDownloadPacket implements Packet {
    public long configId;
    public C2SRequestConfigDownloadPacket() {}
    public C2SRequestConfigDownloadPacket(long configId) { this.configId = configId; }
    @Override public void write(ByteBuf buf) { buf.writeLong(configId); }
    @Override public void read(ByteBuf buf) { this.configId = buf.readLong(); }
}