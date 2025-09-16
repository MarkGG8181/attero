package store.clovr.common.protocol.client;

import io.netty.buffer.ByteBuf;
import store.clovr.common.protocol.Packet;
import store.clovr.common.protocol.launcher.FileType;

public class C2SRequestFilePacket implements Packet {
    public long productId;
    public FileType fileType;

    public C2SRequestFilePacket() {}

    public C2SRequestFilePacket(long productId, FileType fileType) {
        this.productId = productId;
        this.fileType = fileType;
    }

    @Override
    public void write(ByteBuf buf) {
        buf.writeLong(productId);
        buf.writeByte(fileType.ordinal());
    }

    @Override
    public void read(ByteBuf buf) {
        this.productId = buf.readLong();
        this.fileType = FileType.values()[buf.readByte()];
    }
}