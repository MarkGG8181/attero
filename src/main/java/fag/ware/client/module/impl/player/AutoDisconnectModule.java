package fag.ware.client.module.impl.player;

import fag.ware.client.event.data.Subscribe;
import fag.ware.client.event.impl.TickEvent;
import fag.ware.client.module.data.ModuleCategory;
import fag.ware.client.module.data.ModuleInfo;
import fag.ware.client.module.data.setting.impl.NumberSetting;
import fag.ware.client.module.AbstractModule;
import fag.ware.client.util.math.ColorUtil;
import fag.ware.client.util.math.Timer;
import net.minecraft.network.packet.s2c.common.DisconnectS2CPacket;
import net.minecraft.text.MutableText;
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
    public void onTick(TickEvent event) {
        if (mc.player == null || mc.world == null || mc.player.isDead()) return;
        if (mc.player.getHealth() >= hp.getValue().floatValue() || !timer.hasElapsed(delay.toInt(), true)) return;

        toggle();
        MutableText finalText = Text.empty();

        MutableText prefix = ColorUtil.createGradientText("fagware", new Color(0x26A07D), Color.WHITE);
        finalText.append(prefix);

        finalText.append(Text.literal(" > Disconnected you because of low HP").setStyle(Style.EMPTY.withColor(TextColor.fromRgb(0xFFFFFF))));

        mc.execute(() -> mc.player.networkHandler.onDisconnect(new DisconnectS2CPacket(finalText)));
    }

    @Override
    public void onEnable() {
    }

    @Override
    public void onDisable() {
        timer.reset();
    }
}