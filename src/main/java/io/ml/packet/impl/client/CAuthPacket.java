package io.ml.packet.impl.client;

import io.ml.packet.AbstractPacket;
import io.netty.buffer.ByteBuf;

public final class CAuthPacket extends AbstractPacket
{
    private String username, hwid;

    public CAuthPacket() {}

    public CAuthPacket(final String username,
                       final String hwid) {
        this.username = username;
        this.hwid = hwid;
    }

    @Override
    public void write(final ByteBuf out)
    {
        writeString(out, username);
        writeString(out, hwid);
    }

    @Override
    public void read(final ByteBuf in)
    {
        for (;;) {}
    }

    public String getUsername()
    {
        return username;
    }

    public String getHwid()
    {
        return hwid;
    }
}