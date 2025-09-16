package store.clovr.common.protocol.server;

import io.netty.buffer.ByteBuf;
import store.clovr.common.protocol.Packet;

public class S2CLoginResponsePacket implements Packet {
    private boolean success;

    public S2CLoginResponsePacket() {}

    public S2CLoginResponsePacket(boolean success) {
        this.success = success;
    }

    @Override
    public void write(ByteBuf buf) {
        buf.writeBoolean(success);
    }

    @Override
    public void read(ByteBuf buf) {
        this.success = buf.readBoolean();
    }

    public boolean isSuccess() {
        return success;
    }
}