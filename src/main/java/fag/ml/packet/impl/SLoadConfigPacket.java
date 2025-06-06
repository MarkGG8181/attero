package fag.ml.packet.impl;

import fag.ml.packet.AbstractPacket;
import io.netty.buffer.ByteBuf;

public class SLoadConfigPacket extends AbstractPacket {
    private boolean success;
    private String configName;
    private String jsonData;

    public SLoadConfigPacket() {
    }

    public SLoadConfigPacket(boolean success, String configName, String jsonData) {
        this.success = success;
        this.configName = configName;
        this.jsonData = jsonData;
    }

    @Override
    public void write(ByteBuf out) {
        out.writeBoolean(success);
        writeString(out, configName);
        writeString(out, jsonData);
    }

    @Override
    public void read(ByteBuf in) {
        success = in.readBoolean();
        configName = readString(in);
        jsonData = readString(in);
    }

    public boolean isSuccess() {
        return success;
    }

    public String getConfigName() {
        return configName;
    }

    public String getJsonData() {
        return jsonData;
    }
}
