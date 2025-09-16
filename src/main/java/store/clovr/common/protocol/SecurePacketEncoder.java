package store.clovr.common.protocol; // Assuming this is in your common module

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import store.clovr.common.auth.AuthenticatedCryptor;

import java.util.List;

public class SecurePacketEncoder extends MessageToMessageEncoder<ByteBuf> {
    private final AuthenticatedCryptor cryptor;

    public SecurePacketEncoder(AuthenticatedCryptor cryptor) {
        this.cryptor = cryptor;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        byte[] plaintext = new byte[msg.readableBytes()];
        msg.readBytes(plaintext);

        byte[] encrypted = cryptor.encrypt(plaintext);

        out.add(ctx.alloc().buffer().writeBytes(encrypted));
    }
}