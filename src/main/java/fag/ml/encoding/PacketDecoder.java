package fag.ml.encoding;

import fag.ml.packet.AbstractPacket;
import fag.ml.packet.Registry;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class PacketDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (in.readableBytes() < 4) {
            return;
        }

        in.markReaderIndex();
        int packetId = in.readInt();

        try {
            AbstractPacket packet = Registry.create(packetId);
            packet.read(in);
            out.add(packet);
        } catch (Exception e) {
            in.resetReaderIndex();
            throw e;
        }
    }
}