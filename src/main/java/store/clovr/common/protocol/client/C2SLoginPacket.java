package store.clovr.common.protocol.client;

import io.netty.buffer.ByteBuf;
import store.clovr.common.protocol.Packet;

public class C2SLoginPacket implements Packet {
    private String username;
    private String password;
    private String hwid;
    private long clientId;

    public C2SLoginPacket() {}

    public C2SLoginPacket(String username, String password, String hwid, long clientId) {
        this.username = username;
        this.password = password;
        this.hwid = hwid;
        this.clientId = clientId;
    }

    @Override
    public void write(ByteBuf buf) {
        writeString(buf, username);
        writeString(buf, password);
        writeString(buf, hwid);
        buf.writeLong(clientId);
    }

    @Override
    public void read(ByteBuf buf) {
        this.username = readString(buf);
        this.password = readString(buf);
        this.hwid = readString(buf);
        this.clientId = buf.readLong();
    }

    // Getters
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public long getClientId() { return clientId; }
    public String getHwid() { return hwid; }
}