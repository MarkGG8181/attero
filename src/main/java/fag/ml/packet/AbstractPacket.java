package fag.ml.packet;

import io.netty.buffer.ByteBuf;

import java.nio.file.attribute.FileTime;

public abstract class AbstractPacket {
    public abstract void write(final ByteBuf out);
    public abstract void read(final ByteBuf in);

    public final void writeString(final ByteBuf out,
                                  final String string) {
        final byte[] bytes = string.getBytes();
        out.writeInt(bytes.length);
        out.writeBytes(bytes);
    }

    public final String readString(final ByteBuf in)
    {
        final int length = in.readInt();
        final byte[] bytes = new byte[length];
        in.readBytes(bytes);
        return new String(bytes);
    }

    public void writeFileTime(ByteBuf out, FileTime fileTime) {
        out.writeLong(fileTime.toMillis());
    }

    public FileTime readFileTime(ByteBuf in) {
        return FileTime.fromMillis(in.readLong());
    }
}