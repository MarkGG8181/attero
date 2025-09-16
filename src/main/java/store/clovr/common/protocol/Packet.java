package store.clovr.common.protocol;

import io.netty.buffer.ByteBuf;

import java.nio.charset.StandardCharsets;

public interface Packet {
    void write(ByteBuf buf);

    void read(ByteBuf buf);

    default void writeString(ByteBuf buf, String s) {
        byte[] bytes = s.getBytes(StandardCharsets.UTF_8);
        buf.writeInt(bytes.length);
        buf.writeBytes(bytes);
    }

    default String readString(ByteBuf buf) {
        int length = buf.readInt();
        byte[] bytes = new byte[length];
        buf.readBytes(bytes);
        return new String(bytes, StandardCharsets.UTF_8);
    }
}