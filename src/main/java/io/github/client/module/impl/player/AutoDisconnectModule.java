package io.github.client.module.impl.player;

import io.github.client.Attero;
import io.github.client.event.data.Subscribe;
import io.github.client.event.impl.game.TickEvent;
import io.github.client.module.data.ModuleCategory;
import io.github.client.module.data.ModuleInfo;
import io.github.client.module.data.setting.impl.NumberSetting;
import io.github.client.module.AbstractModule;
import io.github.client.util.java.math.ColorUtil;
import io.github.client.util.java.math.Timer;
import net.minecraft.network.packet.s2c.common.DisconnectS2CPacket;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;

import java.awt.*;

@ModuleInfo(name = "AutoDisconnect", category = ModuleCategory.PLAYER, description = "Automatically disconnects if you're low on hp.")
public class AutoDisconnectModule extends AbstractModule {
    private final NumberSetting hp = new NumberSetting("Min HP", 5, 1, 20);
    private final NumberSetting delay = new NumberSetting("Delay", 30, 0, 300);

    private final Timer timer = new Timer();

    @Subscribe
    private void onTick(TickEvent event) {
        if (mc.player == null || mc.world == null || mc.player.isDead()) return;

        if (mc.player.getHealth() >= hp.getValue().floatValue() || !timer.hasElapsed(delay.toInt(), true)) {
            return;
        }

        var finalText = Text.empty();

        var prefix = ColorUtil.createGradientText(Attero.MOD_ID, new Color(0x26A07D), Color.WHITE);
        finalText.append(prefix);

        finalText.append(Text.literal(" > Disconnected you because of low HP").setStyle(Style.EMPTY.withColor(TextColor.fromRgb(0xFFFFFF))));

        mc.execute(() -> mc.player.networkHandler.onDisconnect(new DisconnectS2CPacket(finalText)));

        toggle();
    }

    public void onDisable() {
        timer.reset();
    }
}