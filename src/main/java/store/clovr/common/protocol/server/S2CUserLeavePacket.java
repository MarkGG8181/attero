package store.clovr.common.protocol.server;

import io.netty.buffer.ByteBuf;
import store.clovr.common.protocol.Packet;

public class S2CUserLeavePacket implements Packet {
    private long userId;

    public S2CUserLeavePacket() {}

    public S2CUserLeavePacket(long userId) {
        this.userId = userId;
    }

    @Override
    public void write(ByteBuf buf) {
        buf.writeLong(userId);
    }

    @Override
    public void read(ByteBuf buf) {
        this.userId = buf.readLong();
    }

    public long getUserId() {
        return userId;
    }
}