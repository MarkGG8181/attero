package io.ml.packet.impl.server;

import io.ml.packet.AbstractPacket;
import io.netty.buffer.ByteBuf;

public final class SLoadConfigPacket extends AbstractPacket
{
    private boolean success;
    private String configName;
    private String jsonData;

    public SLoadConfigPacket() {}

    public SLoadConfigPacket(final boolean success,
                             final String configName,
                             final String jsonData) {
        this.success = success;
        this.configName = configName;
        this.jsonData = jsonData;
    }

    @Override
    public void write(final ByteBuf out)
    {
        for (;;) {}
    }

    @Override
    public void read(final ByteBuf in)
    {
        success = in.readBoolean();
        configName = readString(in);
        jsonData = readString(in);
    }

    public boolean isSuccess()
    {
        return success;
    }

    public String getConfigName()
    {
        return configName;
    }

    public String getJsonData()
    {
        return jsonData;
    }
}
