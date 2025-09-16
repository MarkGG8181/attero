package store.clovr.common.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class PacketEncoder extends MessageToByteEncoder<Packet> {
    @Override
    protected void encode(ChannelHandlerContext ctx, Packet msg, ByteBuf out) throws Exception {
        Integer id = PacketRegistry.getPacketId(msg.getClass());
        if (id == null) {
            throw new IllegalArgumentException("Cannot encode unregistered packet: " + msg.getClass().getSimpleName());
        }
        out.writeInt(id);
        msg.write(out);
    }
}