package io.ml.packet.impl.server;

import io.ml.packet.AbstractPacket;
import io.netty.buffer.ByteBuf;

public final class SAuthPacket extends AbstractPacket
{
    private boolean success;
    private String status;
    private float[] values;

    public SAuthPacket() {}

    public SAuthPacket(final boolean success,
                       final String status,
                       final float[] values) {
        this.success = success;
        this.status = status;
        this.values = values;
    }

    @Override
    public void write(final ByteBuf out)
    {
        for (;;) {}
    }

    @Override
    public void read(ByteBuf in)
    {
        success = in.readBoolean();
        status = readString(in);

        final int length = in.readInt();
        values = new float[length];
        for (int i = 0; i < length; i++)
        {
            values[i] = in.readFloat();
        }
    }

    public boolean isSuccess()
    {
        return success;
    }

    public String getStatus()
    {
        return status;
    }

    public float[] getValues()
    {
        return values;
    }
}