package store.clovr.common.protocol;


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

        Packet packet = PacketRegistry.createPacket(packetId);
        if (packet == null) {
            throw new IllegalStateException("Received unknown packet ID: " + packetId);
        }

        packet.read(in);
        out.add(packet);
    }
}