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
        if (user != null) {
            buf.writeLong(user.getId());
            writeString(buf, user.getUsername());
            writeString(buf, user.getMinecraftUsername());
            buf.writeBoolean(user.isDeveloper());
            writeString(buf, user.getLoggedInProductName());
            buf.writeLong(user.getLoggedInProductId());
        } else {
            buf.writeLong(0L);
            writeString(buf, "");
            writeString(buf, "");
            buf.writeBoolean(false);
            writeString(buf, "");
            buf.writeLong(0L);
        }
    }

    @Override
    public void read(ByteBuf buf) {
        if (!buf.isReadable(8)) { // check before reading long
            throw new IllegalStateException("Not enough bytes to read user ID");
        }

        long id = buf.readLong();
        String username = readString(buf);
        String minecraftUsername = readString(buf);
        boolean developer = buf.readBoolean();
        String loggedInProductName = readString(buf);
        long loggedInProductId = buf.readLong();

        this.user = new ClovrUser(id, username, minecraftUsername, developer, loggedInProductName, loggedInProductId);
    }

    public ClovrUser getUser() {
        return user;
    }
}