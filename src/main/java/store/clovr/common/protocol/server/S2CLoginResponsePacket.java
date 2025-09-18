package store.clovr.common.protocol.server;

import io.netty.buffer.ByteBuf;
import store.clovr.common.protocol.Packet;
import store.clovr.common.user.ClovrUser;

public class S2CLoginResponsePacket implements Packet {
    private boolean success;

    private ClovrUser user;

    public S2CLoginResponsePacket() {}

    public S2CLoginResponsePacket(boolean success, ClovrUser user) {
        this.success = success;
        this.user = user;
    }

    @Override
    public void write(ByteBuf buf) {
        buf.writeBoolean(success);
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
        this.success = buf.readBoolean();

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



    public boolean isSuccess() {
        return success;
    }

    public ClovrUser getUser() {
        return user;
    }
}