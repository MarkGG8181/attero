package store.clovr.common.protocol.client;

import io.netty.buffer.ByteBuf;
import store.clovr.common.protocol.ChatChannel;
import store.clovr.common.protocol.Packet;

public class C2SChatMessagePacket implements Packet {
    private ChatChannel channel;
    private String message;

    public C2SChatMessagePacket() {}

    public C2SChatMessagePacket(ChatChannel channel, String message) {
        this.channel = channel;
        this.message = message;
    }

    @Override
    public void write(ByteBuf buf) {
        buf.writeByte(channel.ordinal());
        writeString(buf, message);
    }

    @Override
    public void read(ByteBuf buf) {
        this.channel = ChatChannel.values()[buf.readByte()]; // Read the enum
        this.message = readString(buf);
    }

    public ChatChannel getChannel() { return channel; }
    public String getMessage() { return message; }
}