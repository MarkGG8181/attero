package fag.ware.client.tracker.impl;

import fag.ml.encoding.PacketDecoder;
import fag.ml.encoding.PacketEncoder;
import fag.ml.packet.AbstractPacket;
import fag.ml.packet.impl.*;
import fag.ware.client.tracker.AbstractTracker;
import fag.ware.client.util.client.ConfigEntry;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@SuppressWarnings("ALL")
public class AuthTracker extends AbstractTracker {
    private static final AuthTracker tracker = new AuthTracker();

    public static AuthTracker getInstance() {
        return tracker;
    }

    private final String host = "localhost";
    private final int port = 1337;

    private Channel channel;
    private EventLoopGroup group;
    private boolean authenticated = false;
    private final Object authLock = new Object();

    public float[] values;

    @Override
    public void initialize() {
        if (channel != null && channel.isActive()) {
            return;
        }

        group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            ch.pipeline().addLast(
                                    new LengthFieldBasedFrameDecoder(1024 * 1024, 0, 4, 0, 4),
                                    new LengthFieldPrepender(4),
                                    new PacketDecoder(),
                                    new PacketEncoder(),
                                    new ClientHandler()
                            );
                        }
                    });

            ChannelFuture f = b.connect(host, port).sync();
            channel = f.channel();
        } catch (Exception e) {
            group.shutdownGracefully();
            throw new RuntimeException("Failed to connect to auth server");
        }
    }

    public void authenticate(String username, String hwid) throws ExecutionException, InterruptedException, TimeoutException {
        CompletableFuture<Boolean> authFuture = new CompletableFuture<>();
        CompletableFuture<float[]> valuesFuture = new CompletableFuture<>();

        ClientHandler handler = channel.pipeline().get(ClientHandler.class);
        handler.setAuthListener((success, values) -> {
            synchronized (authLock) {
                authenticated = success;
                this.values = values;
                authLock.notifyAll();
            }
            authFuture.complete(success);
            valuesFuture.complete(values);
        });

        send(new CAuthPacket(username, hwid));

        try {
            boolean success = authFuture.get(5, TimeUnit.SECONDS);
            float[] values = valuesFuture.get(5, TimeUnit.SECONDS);

            System.out.println("values: " + Arrays.toString(values));

            if (success) {
                ModuleTracker.getInstance().initialize();
                CommandTracker.getInstance().initialize();
                CombatTracker.getInstance().initialize();
                PlayerTracker.getInstance().initialize();
            }
        } finally {
            handler.setAuthListener(null);
        }
    }


    public List<ConfigEntry> fetchConfigList() throws InterruptedException, ExecutionException, TimeoutException {
        CompletableFuture<List<ConfigEntry>> configFuture = new CompletableFuture<>();

        AuthTracker.ClientHandler handler = channel.pipeline().get(AuthTracker.ClientHandler.class);
        handler.setConfigListener(configFuture::complete);

        send(new CFetchConfigsPacket());

        //WHY IS IT TIMING OUT??

        try {
            return configFuture.get(5, TimeUnit.SECONDS);
        } finally {
            handler.setConfigListener(null);
        }
    }

    public void send(AbstractPacket packet) {
        if (channel != null && channel.isActive()) {
            channel.writeAndFlush(packet);
        } else {
            throw new IllegalStateException("Channel is not active");
        }
    }

    public void disconnect() throws InterruptedException {
        if (channel != null) {
            channel.close().sync();
        }

        if (group != null) {
            group.shutdownGracefully();
        }

        authenticated = false;
    }

    public boolean isAuthenticated() {
        return authenticated;
    }

    public void waitForAuthentication() throws InterruptedException {
        synchronized (authLock) {
            while (!authenticated) {
                authLock.wait();
            }
        }
    }

    static class ClientHandler extends ChannelInboundHandlerAdapter {
        private AuthListener authListener;
        private ConfigListListener configListListener;

        public void setAuthListener(AuthListener authListener) {
            this.authListener = authListener;
        }

        public void setConfigListener(ConfigListListener configListListener) {
            this.configListListener = configListListener;
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            if (msg instanceof SAuthPacket) {
                SAuthPacket packet = (SAuthPacket) msg;

                if (authListener != null) {
                    authListener.onResult(packet.isSuccess(), packet.getValues());
                }
            } else if (msg instanceof SLoadConfigPacket) {
                SLoadConfigPacket packet = (SLoadConfigPacket) msg;
                if (packet.isSuccess()) {
                    System.out.println("Config loaded: " + packet.getConfigName());
                    System.out.println(packet.getJsonData());
                } else {
                    System.out.println("Config load failed: " + packet.getJsonData());
                }
            } else if (msg instanceof SFetchConfigsPacket) {
                SFetchConfigsPacket packet = (SFetchConfigsPacket) msg;
                if (configListListener != null) {
                    configListListener.onConfigListReceived(packet.getConfigs());
                }
                System.out.println("Received config list:");
                for (ConfigEntry entry : packet.getConfigs()) {
                    System.out.println("  " + entry);
                }
            }
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
            cause.printStackTrace();
            ctx.close();
        }

        interface AuthListener {
            void onResult(boolean success, float[] values);
        }

        interface ConfigListListener {
            void onConfigListReceived(List<ConfigEntry> configs);
        }
    }
}