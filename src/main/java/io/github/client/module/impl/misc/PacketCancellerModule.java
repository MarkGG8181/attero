package io.github.client.module.impl.misc;

import io.github.client.event.data.Subscribe;
import io.github.client.event.impl.player.ReceivePacketEvent;
import io.github.client.event.impl.player.SendPacketEvent;
import io.github.client.module.AbstractModule;
import io.github.client.module.data.ModuleCategory;
import io.github.client.module.data.ModuleInfo;
import io.github.client.module.data.setting.impl.MultiStringSetting;
import io.github.client.util.game.NetworkUtil;

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