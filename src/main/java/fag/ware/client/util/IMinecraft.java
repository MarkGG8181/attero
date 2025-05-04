package fag.ware.client.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public interface IMinecraft {
    MinecraftClient mc = MinecraftClient.getInstance();

    default void send(String mesage) {
        if (mc.player != null)
            mc.player.sendMessage(Text.of(mesage), false);
    }
}
