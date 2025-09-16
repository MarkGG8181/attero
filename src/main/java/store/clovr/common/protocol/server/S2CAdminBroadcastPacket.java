package store.clovr.common.protocol.server;


import io.netty.buffer.ByteBuf;
import store.clovr.common.protocol.Packet;

public class S2CAdminBroadcastPacket implements Packet {
    private String message;

    public S2CAdminBroadcastPacket() {}

    public S2CAdminBroadcastPacket(String message) {
        this.message = message;
    }

    @Override
    public void write(ByteBuf buf) {
        writeString(buf, message);
    }

    @Override
    public void read(ByteBuf buf) {
        this.message = readString(buf);
    }

    public String getMessage() {
        return message;
    }
}