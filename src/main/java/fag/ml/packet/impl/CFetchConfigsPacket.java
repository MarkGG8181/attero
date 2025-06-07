package fag.ml.packet.impl;

import fag.ml.packet.AbstractPacket;
import io.netty.buffer.ByteBuf;

public class CFetchConfigsPacket extends AbstractPacket {
    public CFetchConfigsPacket() {
    }

    @Override
    public void write(ByteBuf out) {
        for (;;) {}
    }

    @Override
    public void read(ByteBuf in) {
        for (;;) {}
    }
}