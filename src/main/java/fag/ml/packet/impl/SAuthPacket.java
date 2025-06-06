package fag.ml.packet.impl;

import fag.ml.packet.AbstractPacket;
import io.netty.buffer.ByteBuf;

public class SAuthPacket extends AbstractPacket {
    private boolean success;
    private String status;
    private float[] values;

    public SAuthPacket() {
    }

    public SAuthPacket(boolean success, String status, float[] values) {
        this.success = success;
        this.status = status;
        this.values = values;
    }

    @Override
    public void write(ByteBuf out) {
        out.writeBoolean(success);
        writeString(out, status);

        out.writeInt(values.length);
        for (float value : values) {
            out.writeFloat(value);
        }
    }

    @Override
    public void read(ByteBuf in) {
        success = in.readBoolean();
        status = readString(in);

        int length = in.readInt();
        values = new float[length];
        for (int i = 0; i < length; i++) {
            values[i] = in.readFloat();
        }
    }

    public boolean isSuccess() {
        return success;
    }

    public String getStatus() {
        return status;
    }

    public float[] getValues() {
        return values;
    }
}