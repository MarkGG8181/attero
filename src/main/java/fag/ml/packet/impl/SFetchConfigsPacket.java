package fag.ml.packet.impl;

import fag.ml.packet.AbstractPacket;
import fag.ware.client.util.client.ConfigEntry;
import io.netty.buffer.ByteBuf;

import java.nio.file.attribute.FileTime;
import java.util.ArrayList;
import java.util.List;

public class SFetchConfigsPacket extends AbstractPacket {
    private List<ConfigEntry> configs;

    public SFetchConfigsPacket() {
        this.configs = new ArrayList<>();
    }

    public SFetchConfigsPacket(List<ConfigEntry> configs) {
        this.configs = configs;
    }

    @Override
    public void write(ByteBuf out) {
        for (;;) {}
    }

    @Override
    public void read(ByteBuf in) {
        int count = in.readInt();
        configs = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            String name = readString(in);
            FileTime createdTime = readFileTime(in);
            configs.add(new ConfigEntry(name, createdTime));
        }
    }

    public List<ConfigEntry> getConfigs() {
        return configs;
    }
}