package store.clovr.common.protocol.server;


import io.netty.buffer.ByteBuf;
import store.clovr.common.protocol.Packet;
import store.clovr.common.user.ClovrUser;

public class S2CUserJoinPacket implements Packet {
    private ClovrUser user;

    public S2CUserJoinPacket() {}

    public S2CUserJoinPacket(ClovrUser user) {
        this.user = user;
    }

    @Override
    public void write(ByteBuf buf) {
        buf.writeLong(user.getId());
        writeString(buf, user.getUsername());
        writeString(buf, user.getMinecraftUsername());
        buf.writeBoolean(user.isDeveloper());
    }

    @Override
    public void read(ByteBuf buf) {
        this.user = new ClovrUser(buf.readLong(), readString(buf), readString(buf), buf.readBoolean());
    }

    public ClovrUser getUser() {
        return user;
    }
}