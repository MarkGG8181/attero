package store.clovr.common.protocol.client;

import io.netty.buffer.ByteBuf;
import store.clovr.common.protocol.Packet;

public class C2SLauncherLoginPacket implements Packet {
    public String username;
    public String password;

    public C2SLauncherLoginPacket() {}

    public C2SLauncherLoginPacket(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public void write(ByteBuf buf) {
        writeString(buf, username);
        writeString(buf, password);
    }

    @Override
    public void read(ByteBuf buf) {
        this.username = readString(buf);
        this.password = readString(buf);
    }
}