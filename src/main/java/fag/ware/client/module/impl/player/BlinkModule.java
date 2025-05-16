package fag.ware.client.module.impl.player;

import fag.ware.client.event.data.Subscribe;
import fag.ware.client.event.impl.ReceivePacketEvent;
import fag.ware.client.event.impl.SendPacketEvent;
import fag.ware.client.module.AbstractModule;
import fag.ware.client.module.data.ModuleCategory;
import fag.ware.client.module.data.ModuleInfo;
import fag.ware.client.module.data.setting.impl.BooleanSetting;

@SuppressWarnings("ALL")
@ModuleInfo(name = "Blink", category = ModuleCategory.PLAYER, description = "Freezes your packets")
public class BlinkModule extends AbstractModule {
    public BooleanSetting INCOMING = new BooleanSetting("Incoming", true);
    public BooleanSetting OUTGOING = new BooleanSetting("Outgoing", false);


    @Subscribe
    public void onReceivePacket(ReceivePacketEvent event) {
        if (mc.player == null || mc.world == null) return;
        if (INCOMING.getValue()) {
            event.setCancelled(true);
        }
    }

    @Subscribe
    public void onSendPacket(SendPacketEvent event) {
        if (mc.player == null || mc.world == null) return;
        if (OUTGOING.getValue()) {
            event.setCancelled(true);
        }
    }
}
