package store.clovr.common.protocol.server;

import io.netty.buffer.ByteBuf;
import store.clovr.common.protocol.Packet;
import store.clovr.common.user.ClovrUser;
import java.util.ArrayList;
import java.util.List;

public class S2CUserListPacket implements Packet {
    private List<ClovrUser> users;

    public S2CUserListPacket() {}

    public S2CUserListPacket(List<ClovrUser> users) {
        this.users = users;
    }

    @Override
    public void write(ByteBuf buf) {
        buf.writeInt(users.size());
        for (ClovrUser user : users) {
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
    }

    @Override
    public void read(ByteBuf buf) {
        int size = buf.readInt();
        this.users = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            long id = buf.readLong();
            String username = readString(buf);
            String minecraftUsername = readString(buf);
            boolean developer = buf.readBoolean();
            String loggedInProductName = readString(buf);
            long loggedInProductId = buf.readLong();

            this.users.add(new ClovrUser(id, username, minecraftUsername, developer, loggedInProductName, loggedInProductId));
        }
    }

    public List<ClovrUser> getUsers() {
        return users;
    }
}