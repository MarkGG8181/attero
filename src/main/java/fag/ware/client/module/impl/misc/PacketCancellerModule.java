package fag.ware.client.module.impl.misc;

import fag.ware.client.event.data.Subscribe;
import fag.ware.client.event.impl.ReceivePacketEvent;
import fag.ware.client.event.impl.SendPacketEvent;
import fag.ware.client.module.AbstractModule;
import fag.ware.client.module.data.ModuleCategory;
import fag.ware.client.module.data.ModuleInfo;
import fag.ware.client.module.data.setting.impl.MultiStringSetting;
import fag.ware.client.util.game.NetworkUtil;

@ModuleInfo(name = "PacketCanceller", description = "Cancels packets", category = ModuleCategory.MISC)
public class PacketCancellerModule extends AbstractModule {
    private final MultiStringSetting incoming = new MultiStringSetting("Incoming", new String[]{}, NetworkUtil.getS2CPacketArray());
    private final MultiStringSetting outgoing = new MultiStringSetting("Outgoing", new String[]{}, NetworkUtil.getC2SPacketArray());

    @Subscribe
    public void onReceivePacket(ReceivePacketEvent event) {
        if (incoming.enabled(event.getPacket().getClass().getSimpleName())) {
            event.setCancelled(true);
        }
    }

    @Subscribe
    public void onSendPacket(SendPacketEvent event) {
        if (outgoing.enabled(event.getPacket().getClass().getSimpleName())) {
            event.setCancelled(true);
        }
    }
}