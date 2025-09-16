package store.clovr.common.protocol.client;

import io.netty.buffer.ByteBuf;
import store.clovr.common.protocol.Packet;

public class C2SUpdateMinecraftUsernamePacket implements Packet {
    private String newMinecraftUsername;

    public C2SUpdateMinecraftUsernamePacket() {}

    public C2SUpdateMinecraftUsernamePacket(String newMinecraftUsername) {
        this.newMinecraftUsername = newMinecraftUsername;
    }

    @Override
    public void write(ByteBuf buf) {
        writeString(buf, newMinecraftUsername);
    }

    @Override
    public void read(ByteBuf buf) {
        this.newMinecraftUsername = readString(buf);
    }

    public String getNewMinecraftUsername() {
        return newMinecraftUsername;
    }
}