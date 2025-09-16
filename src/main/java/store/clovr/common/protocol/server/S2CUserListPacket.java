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
            buf.writeLong(user.getId());
            writeString(buf, user.getUsername());
            writeString(buf, user.getMinecraftUsername());
            buf.writeBoolean(user.isDeveloper());
        }
    }

    @Override
    public void read(ByteBuf buf) {
        int size = buf.readInt();
        this.users = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            long id = buf.readLong();
            String username = readString(buf);
            String mcUsername = readString(buf);
            boolean isDev = buf.readBoolean();
            this.users.add(new ClovrUser(id, username, mcUsername, isDev));
        }
    }

    public List<ClovrUser> getUsers() {
        return users;
    }
}