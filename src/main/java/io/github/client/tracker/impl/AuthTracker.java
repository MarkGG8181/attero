package io.github.client.tracker.impl;

import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import io.github.client.Attero;
import io.github.client.event.data.Subscribe;
import io.github.client.event.impl.player.SendPacketEvent;
import io.github.client.file.impl.LoginFile;
import io.github.client.file.impl.ModulesFile;
import io.github.client.tracker.AbstractTracker;
import io.github.client.util.client.ConfigEntry;
import io.github.client.util.interfaces.IMinecraft;
import lombok.Getter;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;
import store.clovr.client.api.ClovrClient;
import store.clovr.common.protocol.ChatChannel;
import store.clovr.common.protocol.client.C2SChatMessagePacket;
import store.clovr.common.protocol.server.*;
import store.clovr.common.user.ClovrUser;
import clovr.store.Grabber;

import java.nio.file.attribute.FileTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@SuppressWarnings("ALL")
public class AuthTracker extends AbstractTracker implements IMinecraft {
    public static final AuthTracker INSTANCE = new AuthTracker();

    public LoginFile loginFile = new LoginFile(null, null);

    public boolean connected;

    public List<ConfigEntry> cloudConfigs = new ArrayList<>();
    public List<ClovrUser> onlineUsers = new CopyOnWriteArrayList<>();
    public ClovrClient client;

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
            cloudConfigs.clear();

            packet.configs.forEach(info -> {
                cloudConfigs.add(new ConfigEntry(info.name.toLowerCase().replace(" ", ""), info.id));
            });
        });

        client.addPacketListener(S2CConfigDataPacket.class, packet -> {
            try {
                ModulesFile tempFile = new ModulesFile("online-temp");
                tempFile.json = new JsonParser().parse(packet.fileContent).getAsJsonObject();
                tempFile.load();
                send("Successfully loaded online config with ID §e" + packet.configId + "§a");
            } catch (JsonSyntaxException e) {
                send("§cFailed to load online config: Invalid format");
                Attero.LOGGER.error("Failed to parse online config JSON", e);
            } catch (Exception e) {
                send("§cAn unexpected error occurred while loading the online config");
                Attero.LOGGER.error("Failed to apply online config", e);
            }
        });
    }

    @Subscribe
    private void onPacket(SendPacketEvent event) {
        if (event.packet instanceof ChatMessageC2SPacket packet) {
            String message = packet.chatMessage();

            if (message.startsWith("#")) {
                client.sendPacket(new C2SChatMessagePacket(ChatChannel.GLOBAL, message.replace("#", "")));
                event.cancelled = true;
            }
        }
    }
}