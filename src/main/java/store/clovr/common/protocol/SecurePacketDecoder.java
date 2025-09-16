package store.clovr.common.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import store.clovr.common.auth.AuthenticatedCryptor;

import java.util.List;

public class SecurePacketDecoder extends MessageToMessageDecoder<ByteBuf> {
    private final AuthenticatedCryptor cryptor;
    public SecurePacketDecoder(AuthenticatedCryptor cryptor) { this.cryptor = cryptor; }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        byte[] encrypted = new byte[msg.readableBytes()];
        msg.readBytes(encrypted);
        byte[] decrypted = cryptor.decrypt(encrypted);
        out.add(ctx.alloc().buffer().writeBytes(decrypted));
    }
}