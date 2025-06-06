package fag.ml.encoding;

import fag.ml.packet.AbstractPacket;
import fag.ml.packet.Registry;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class PacketEncoder extends MessageToByteEncoder<AbstractPacket> {
    @Override
    protected void encode(ChannelHandlerContext ctx, AbstractPacket packet, ByteBuf out) {
        int packetId = Registry.getPacketId(packet);
        out.writeInt(packetId);
        packet.write(out);
    }
}