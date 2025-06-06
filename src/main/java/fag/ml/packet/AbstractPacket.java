package fag.ml.packet;

import io.netty.buffer.ByteBuf;

public abstract class AbstractPacket {
    public abstract void write(ByteBuf out);
    public abstract void read(ByteBuf in);

    public void writeString(ByteBuf out, String string) {
        byte[] bytes = string.getBytes();
        out.writeInt(bytes.length);
        out.writeBytes(bytes);
    }

    public String readString(ByteBuf in) {
        int length = in.readInt();
        byte[] bytes = new byte[length];
        in.readBytes(bytes);
        return new String(bytes);
    }
}