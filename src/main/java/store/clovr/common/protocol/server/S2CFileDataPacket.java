package store.clovr.common.protocol.server;

import io.netty.buffer.ByteBuf;
import store.clovr.common.protocol.Packet;
import store.clovr.common.protocol.launcher.FileType;

public class S2CFileDataPacket implements Packet {
    public FileType fileType;
    public byte[] fileData;

    public S2CFileDataPacket() {}

    public S2CFileDataPacket(FileType fileType, byte[] fileData) {
        this.fileType = fileType;
        this.fileData = fileData;
    }

    @Override
    public void write(ByteBuf buf) {
        buf.writeByte(fileType.ordinal());
        buf.writeInt(fileData.length);
        buf.writeBytes(fileData);
    }

    @Override
    public void read(ByteBuf buf) {
        this.fileType = FileType.values()[buf.readByte()];
        int length = buf.readInt();
        this.fileData = new byte[length];
        buf.readBytes(fileData);
    }
}