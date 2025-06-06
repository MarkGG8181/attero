package fag.ml.packet.impl;

import fag.ml.packet.AbstractPacket;
import io.netty.buffer.ByteBuf;

public class CLoadConfigPacket extends AbstractPacket {
    private String configName;

    public CLoadConfigPacket() {
    }

    public CLoadConfigPacket(String configName) {
        this.configName = configName;
    }

    @Override
    public void write(ByteBuf out) {
        writeString(out, configName);
    }

    @Override
    public void read(ByteBuf in) {
        configName = readString(in);
    }

    public String getConfigName() {
        return configName;
    }
}