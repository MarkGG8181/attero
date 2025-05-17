package fag.ware.client.util.interfaces;

import fag.ware.client.util.math.ColorUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.network.packet.Packet;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;

import java.awt.*;

public interface IMinecraft {
    MinecraftClient mc = MinecraftClient.getInstance();

    default void send(String message) {
        if (mc.player != null) {
            MutableText finalText = Text.empty();

            MutableText prefix = ColorUtil.createGradientText("fag", new Color(0x26A07D), Color.WHITE);
            finalText.append(prefix);

            finalText.append(Text.literal(" > " + message).setStyle(Style.EMPTY.withColor(TextColor.fromRgb(0xFFFFFF))));

            mc.player.sendMessage(finalText, false);
        }
    }

    default void sendError(String message) {
        if (mc.player != null) {
            MutableText finalText = Text.empty();

            MutableText prefix = ColorUtil.createGradientText("fag", new Color(0xFA003F), Color.WHITE);
            finalText.append(prefix);

            finalText.append(Text.literal(" > " + message).setStyle(Style.EMPTY.withColor(TextColor.fromRgb(0xFFFFFF))));

            mc.player.sendMessage(finalText, false);
        }
    }

    default void setTimer(float speed) {
        RenderTickCounter counter = mc.getRenderTickCounter();

        if (counter instanceof ITimerAccessor accessor) {
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
