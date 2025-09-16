package store.clovr.common.protocol.server;

import io.netty.buffer.ByteBuf;
import store.clovr.common.protocol.ChatChannel;
import store.clovr.common.protocol.Packet;

public class S2CChatMessagePacket implements Packet {
    private ChatChannel channel;
    private String senderUsername;
    private String message;

    public S2CChatMessagePacket() {}

    public S2CChatMessagePacket(ChatChannel channel, String senderUsername, String message) {
        this.channel = channel;
        this.senderUsername = senderUsername;
        this.message = message;
    }

    @Override
    public void write(ByteBuf buf) {
        buf.writeByte(channel.ordinal());
        writeString(buf, senderUsername);
        writeString(buf, message);
    }

    @Override
    public void read(ByteBuf buf) {
        this.channel = ChatChannel.values()[buf.readByte()];
        this.senderUsername = readString(buf);
        this.message = readString(buf);
    }

    public ChatChannel getChannel() { return channel; }
    public String getSenderUsername() { return senderUsername; }
    public String getMessage() { return message; }
}