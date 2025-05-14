package fag.ware.client.module.impl.player;

import fag.ware.client.event.data.Subscribe;
import fag.ware.client.event.impl.TickEvent;
import fag.ware.client.module.data.ModuleCategory;
import fag.ware.client.module.data.ModuleInfo;
import fag.ware.client.module.data.setting.impl.NumberSetting;
import fag.ware.client.module.AbstractModule;
import net.minecraft.network.packet.s2c.common.DisconnectS2CPacket;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

@ModuleInfo(name = "AutoLog", category = ModuleCategory.PLAYER, description = "Automatically logs out if you're low on hp.")
public class AutoLogModule extends AbstractModule {
    public NumberSetting hp = new NumberSetting("Min HP", 5, 1, 20);

    // Horrid.
    @Subscribe
    public void Tick(TickEvent event) {
        if (mc.player == null || mc.world == null || mc.player.isDead()) return;
        if (mc.player.getHealth() < hp.getValue().floatValue()) {
            disconnect("Low HP");
        }
    }
    public void disconnect(Object reason) {
        toggle();
        MutableText text = Text.literal("[AutoLog] ");

        if (reason instanceof String) {
            text.append(Text.literal((String) reason));
        } else if (reason instanceof Text) {
            text.append((Text) reason);
        }

        mc.execute(() -> mc.player.networkHandler.onDisconnect(new DisconnectS2CPacket(text)));
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }
}
