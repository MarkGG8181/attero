package io.github.client.util.interfaces;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.network.packet.Packet;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

public interface IMinecraft {
    MinecraftClient mc = MinecraftClient.getInstance();

    enum ChatMessageType {
        INFO("§aao §f:: "),
        ERROR("§cerr :: §r"),
        WARNING("§ewarn :: §r"),
        IRC("§birc :: §r"),
        BROADCAST("§l§n§4broadcast :: §r");

        public final String prefix;

        ChatMessageType(String prefix) {
            this.prefix = prefix;
        }
    }

    default void send(ChatMessageType type, boolean prefix, String message, Object... args) {
        if (mc.player != null) {
            MutableText finalText = Text.empty();

            for (Object arg : args) {
                String replacement = "§e" + (arg == null ? "null" : arg.toString()) + "§r";
                message = message.replaceFirst("\\{\\}", replacement);
            }

            if (prefix) message = type.prefix + message;
            finalText.append(message);
            mc.player.sendMessage(finalText, false);
        }
    }

    default void error(String message, Object... args) {
        send(ChatMessageType.ERROR, true, message, args);
    }

    default void warn(String message, Object... args) {
        send(ChatMessageType.WARNING, true, message, args);
    }

    default void send(boolean prefix, String message, Object... args) {
        send(ChatMessageType.INFO, prefix, message, args);
    }

    default void send(String message, Object... args) {
        send(true, message, args);
    }

    default void send(String message) {
        send(message, true);
    }

    default void setTimer(float speed) {
        RenderTickCounter counter = mc.getRenderTickCounter();

        if (counter instanceof ITimer accessor) {
            accessor.setTimerSpeed(speed);
        }
    }

    default void sendPacket(Packet<?> packet) {
        mc.player.networkHandler.sendPacket(packet);
    }

    default void sendPacketNoEvent(Packet<?> packet) {
        mc.getNetworkHandler().getConnection().send(packet);
    }
}
