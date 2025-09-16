package store.clovr.common.protocol.server;

import io.netty.buffer.ByteBuf;
import store.clovr.common.protocol.Packet;

public class S2CConfigDataPacket implements Packet {
    public long configId;
    public String fileContent;
    public S2CConfigDataPacket() {}
    public S2CConfigDataPacket(long configId, String fileContent) { this.configId = configId; this.fileContent = fileContent; }
    @Override public void write(ByteBuf buf) { buf.writeLong(configId); writeString(buf, fileContent); }
    @Override public void read(ByteBuf buf) { this.configId = buf.readLong(); this.fileContent = readString(buf); }
}