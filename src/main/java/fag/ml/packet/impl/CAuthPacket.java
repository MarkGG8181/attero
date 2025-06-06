package fag.ml.packet.impl;

import fag.ml.packet.AbstractPacket;
import io.netty.buffer.ByteBuf;

public class CAuthPacket extends AbstractPacket {
    private String username, hwid;

    public CAuthPacket() {
    }

    public CAuthPacket(String username, String hwid) {
        this.username = username;
        this.hwid = hwid;
    }

    @Override
    public void write(ByteBuf out) {
        writeString(out, username);
        writeString(out, hwid);
    }

    @Override
    public void read(ByteBuf in) {
        username = readString(in);
        hwid = readString(in);
    }

    public String getUsername() {
        return username;
    }

    public String getHwid() {
        return hwid;
    }
}