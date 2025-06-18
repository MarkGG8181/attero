package io.ml.encoding;

import io.ml.packet.AbstractPacket;
import io.ml.packet.Registry;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class PacketDecoder extends ByteToMessageDecoder
{
    @Override
    protected void decode(final ChannelHandlerContext ctx,
                          final ByteBuf in,
                          final List<Object> out) throws Exception {
        if (in.readableBytes() < 4) return;

        in.markReaderIndex();
        final int packetId = in.readInt();

        try
        {
            final AbstractPacket packet = Registry.create(packetId);
            packet.read(in);
            out.add(packet);
        }
        catch (Exception e)
        {
            in.resetReaderIndex();
            throw e;
        }
    }
}