package fag.ml.encoding;

import fag.ml.packet.AbstractPacket;
import fag.ml.packet.Registry;
import fag.ware.client.util.auth.EncryptionUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public final class PacketEncoder extends MessageToByteEncoder<AbstractPacket>
{
    @Override
    protected void encode(final ChannelHandlerContext ctx,
                          final AbstractPacket packet,
                          final ByteBuf out) {
        final ByteBuf buf = Unpooled.buffer();

        final int packetId = Registry.getPacketId(packet);
        buf.writeInt(packetId);
        packet.write(buf);

        final byte[] bytes = new byte[buf.readableBytes()];
        buf.readBytes(bytes);

        out.writeBytes(EncryptionUtil.rsaEncrypt(bytes));
    }
}