package store.clovr.client.api;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.bouncycastle.crypto.params.X25519PrivateKeyParameters;
import org.bouncycastle.crypto.params.X25519PublicKeyParameters;
import store.clovr.common.auth.AuthenticatedCryptor;
import store.clovr.common.protocol.*;
import store.clovr.common.protocol.client.C2SLoginPacket;
import store.clovr.common.protocol.server.S2CLoginResponsePacket;
import store.clovr.common.util.SecurityUtils;

import java.util.concurrent.CompletableFuture;

public class ClientApiHandler extends SimpleChannelInboundHandler<Object> {

    private final ClovrClient client;
    private final CompletableFuture<Boolean> loginFuture;
    private final String username;
    private final String password;
    private final String hwid;
    private final long clientId;

    private boolean handshakeComplete = false;
    private X25519PrivateKeyParameters clientPrivateKey;

    public ClientApiHandler(ClovrClient client, CompletableFuture<Boolean> loginFuture, String username, String password, String hwid, long clientId) {
        this.client = client;
        this.loginFuture = loginFuture;
        this.username = username;
        this.password = password;
        this.hwid = hwid;
        this.clientId = clientId;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        clientPrivateKey = SecurityUtils.generateKeyPair();
        ctx.writeAndFlush(ctx.alloc().buffer(SecurityUtils.PUBLIC_KEY_SIZE)
                .writeBytes(clientPrivateKey.generatePublicKey().getEncoded()));
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (!handshakeComplete) {
            if (!(msg instanceof ByteBuf)) return;
            handleHandshake(ctx, (ByteBuf) msg);
            return;
        }

        if (msg instanceof Packet) {
            if (!loginFuture.isDone()) {
                if (msg instanceof S2CLoginResponsePacket) {
                    S2CLoginResponsePacket response = (S2CLoginResponsePacket) msg;
                    loginFuture.complete(response.isSuccess());
                } else {
                    loginFuture.complete(false);
                    ctx.close();
                }
            } else {
                client.dispatchPacket((Packet) msg);
            }
        }
    }

    private void handleHandshake(ChannelHandlerContext ctx, ByteBuf msg) {
        byte[] serverPublicKeyBytes = new byte[SecurityUtils.PUBLIC_KEY_SIZE];
        msg.readBytes(serverPublicKeyBytes);
        X25519PublicKeyParameters serverPublicKey = new X25519PublicKeyParameters(serverPublicKeyBytes);

        byte[] sharedSecret = SecurityUtils.getSharedSecret(clientPrivateKey, serverPublicKey);
        byte[] sessionKey = SecurityUtils.deriveKey(sharedSecret, "server-client-key");
        AuthenticatedCryptor cryptor = new AuthenticatedCryptor(sessionKey);

        ctx.pipeline().addBefore(ctx.name(), "secureEncoder", new SecurePacketEncoder(cryptor));
        ctx.pipeline().addBefore(ctx.name(), "packetEncoder", new PacketEncoder());
        ctx.pipeline().addBefore(ctx.name(), "secureDecoder", new SecurePacketDecoder(cryptor));
        ctx.pipeline().addBefore(ctx.name(), "packetDecoder", new PacketDecoder());
        handshakeComplete = true;

        ctx.writeAndFlush(new C2SLoginPacket(username, password, hwid, clientId));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        if (!loginFuture.isDone()) {
            loginFuture.completeExceptionally(cause);
        }
        cause.printStackTrace();
        ctx.close();
    }
}