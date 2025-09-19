package io.github.client.tracker.impl;

import io.github.client.Attero;
import io.github.client.event.data.Subscribe;
import io.github.client.event.impl.player.SendPacketEvent;
import io.github.client.file.impl.LoginFile;
import io.github.client.tracker.AbstractTracker;
import io.github.client.util.interfaces.IMinecraft;
import lombok.Getter;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;
import store.clovr.client.api.ClovrClient;
import store.clovr.common.protocol.ChatChannel;
import store.clovr.common.protocol.server.*;
import store.clovr.common.user.ClovrUser;
import clovr.store.Grabber;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@SuppressWarnings("ALL")
public class AuthTracker extends AbstractTracker implements IMinecraft {
    public static final AuthTracker INSTANCE = new AuthTracker();

    public LoginFile loginFile = new LoginFile(null, null);

    public boolean connected;

    @Getter
    private List<ClovrUser> onlineUsers = new CopyOnWriteArrayList<>();
    private ClovrClient client;

    public void shutdown() {
        client.disconnect();
    }

    public void authenticate(String username, String password)  {
        client = new ClovrClient();
        client.connect("154.12.236.54", 8080, username, password, Grabber.getHWID(), 3)
                .whenComplete((success, error) -> {
                    if (success != null && success) {
                        loginFile = new LoginFile(username, password);
                        loginFile.save();
                        Attero.BUS.register(this);
                        registerListeners(client);
                        MinecraftClient.getInstance().execute(() -> {
                            MinecraftClient.getInstance().setScreen(null);

                            MusicTracker.INSTANCE.initialize();
                            ModuleTracker.INSTANCE.initialize();
                            CommandTracker.INSTANCE.initialize();
                            RotationTracker.INSTANCE.initialize();
                        });
                    } else {
                        if (error != null) {
                            Attero.LOGGER.error("Failed to login: ", error);
                        }
                    }
                });
    }

    private void registerListeners(ClovrClient client) {
        client.addPacketListener(S2CUserListPacket.class, packet -> {
            this.onlineUsers = packet.getUsers();
        });

        client.addPacketListener(S2CUserJoinPacket.class, packet -> {
            this.onlineUsers.add(packet.getUser());
        });

        client.addPacketListener(S2CUserLeavePacket.class, packet -> {
            this.onlineUsers.removeIf(clovrUser -> clovrUser.getId() == packet.getUserId());
        });

        client.addPacketListener(S2CChatMessagePacket.class, packet -> {
            String message = (packet.getChannel() == ChatChannel.GLOBAL ? "[Global]" : "[Product]") +
                    " [" + packet.getSenderUsername() + "]: "
                    + packet.getMessage();
            send(ChatMessageType.IRC, true, message);
        });

        client.addPacketListener(S2CAdminBroadcastPacket.class, packet -> {
            send(ChatMessageType.BROADCAST, true, packet.getMessage());
        });

        client.addPacketListener(S2CConfigListPacket.class, packet -> {
            /*System.out.println("\n--- Available Product Configs ---");
            if (packet.configs.isEmpty()) {
                System.out.println("No configs found for this product.");
            } else {
                packet.configs.forEach(System.out::println);
                long firstConfigId = packet.configs.get(0).id;
                System.out.println("\nRequesting download for config ID: " + firstConfigId);
                client.sendPacket(new C2SRequestConfigDownloadPacket(firstConfigId));
            }
            System.out.println("---------------------------------");*/
        });

        client.addPacketListener(S2CConfigDataPacket.class, packet -> {
            /*System.out.println("\n--- Downloaded Config (ID: " + packet.configId + ") ---");
            System.out.println(packet.fileContent);
            System.out.println("------------------------------------");*/
        });
    }

    @Subscribe
    private void onPacket(SendPacketEvent event) {
        if (event.packet instanceof ChatMessageC2SPacket packet) {
            String message = packet.chatMessage();

            if (message.startsWith("#")) {
                event.cancelled = true;
                System.out.println("sending c2s");
                //client.sendPacket(new C2SChatMessagePacket(ChatChannel.GLOBAL, message.replace("#", "")));
            }
        }
    }
}