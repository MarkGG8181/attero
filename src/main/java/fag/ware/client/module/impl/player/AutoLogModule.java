package fag.ware.client.module.impl.player;

import fag.ware.client.event.data.Subscribe;
import fag.ware.client.event.impl.TickEvent;
import fag.ware.client.module.data.ModuleCategory;
import fag.ware.client.module.data.ModuleInfo;
import fag.ware.client.module.data.setting.impl.NumberSetting;
import fag.ware.client.module.AbstractModule;
import fag.ware.client.util.math.Timer;
import net.minecraft.network.packet.s2c.common.DisconnectS2CPacket;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

@ModuleInfo(name = "AutoLog", category = ModuleCategory.PLAYER, description = "Automatically logs out if you're low on hp.")
public class AutoLogModule extends AbstractModule {
    private final NumberSetting hp = new NumberSetting("Min HP", 5, 1, 20);
    private final NumberSetting delay = new NumberSetting("Delay", 30, 0, 300);

    private final Timer timer = new Timer();

    @Subscribe
    public void onTick(TickEvent event) {
        if (mc.player == null || mc.world == null || mc.player.isDead()) return;
        if (mc.player.getHealth() >= hp.getValue().floatValue() || !timer.hasElapsed(delay.toInt(), true)) return;

        toggle();
        Text reason = Text.literal("[AutoLog] Low HP");
        mc.execute(() -> mc.player.networkHandler.onDisconnect(new DisconnectS2CPacket(reason)));
    }

    @Override
    public void onEnable() {
    }

    @Override
    public void onDisable() {
        timer.reset();
    }
}