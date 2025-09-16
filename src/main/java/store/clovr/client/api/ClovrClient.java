package store.clovr.client.api;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import store.clovr.client.api.listeners.PacketListener;
import store.clovr.common.protocol.Packet;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class ClovrClient {
    private final Map<Class<? extends Packet>, List<PacketListener>> packetListeners = new ConcurrentHashMap<>();
    private final EventLoopGroup workerGroup;
    private Channel channel;

    public ClovrClient() {
        this.workerGroup = new NioEventLoopGroup();
    }

    public CompletableFuture<Boolean> connect(String host, int port, String username, String password, String hwid, long clientId) {
        CompletableFuture<Boolean> loginFuture = new CompletableFuture<>();
        try {
            Bootstrap b = new Bootstrap();
            b.group(workerGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(
                                    new LengthFieldPrepender(4),
                                    new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4),
                                    new ClientApiHandler(ClovrClient.this, loginFuture, username, password, hwid, clientId)
                            );
                        }
                    });

            b.connect(host, port).addListener((ChannelFuture future) -> {
                if (future.isSuccess()) {
                    this.channel = future.channel();
                } else {
                    loginFuture.completeExceptionally(future.cause());
                }
            });

        } catch (Exception e) {
            loginFuture.completeExceptionally(e);
        }
        return loginFuture;
    }

    public void sendPacket(Packet packet) {
        if (channel != null && channel.isActive()) {
            channel.writeAndFlush(packet);
        }
    }

    public <T extends Packet> void addPacketListener(Class<T> packetClass, PacketListener<T> listener) {
        packetListeners.computeIfAbsent(packetClass, k -> new ArrayList<>()).add(listener);
    }

    protected void dispatchPacket(Packet packet) {
        List<PacketListener> listeners = packetListeners.get(packet.getClass());
        if (listeners != null) {
            for (PacketListener listener : listeners) {
                listener.onPacketReceived(packet);
            }
        }
    }

    public void disconnect() {
        if (channel != null) {
            channel.close();
        }
        workerGroup.shutdownGracefully();
    }
}