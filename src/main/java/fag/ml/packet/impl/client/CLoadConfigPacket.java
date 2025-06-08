package fag.ml.packet.impl.client;

import fag.ml.packet.AbstractPacket;
import io.netty.buffer.ByteBuf;

public final class CLoadConfigPacket extends AbstractPacket
{
    private String configName;

    public CLoadConfigPacket() {}

    public CLoadConfigPacket(final String configName)
    {
        this.configName = configName;
    }

    @Override
    public void write(final ByteBuf out)
    {
        writeString(out, configName);
    }

    @Override
    public void read(final ByteBuf in)
    {
        for (;;) {}
    }

    public String getConfigName()
    {
        return configName;
    }
}