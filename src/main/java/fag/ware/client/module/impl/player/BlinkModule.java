package fag.ware.client.module.impl.player;

import fag.ware.client.event.data.Subscribe;
import fag.ware.client.event.impl.SendPacketEvent;
import fag.ware.client.module.AbstractModule;
import fag.ware.client.module.data.ModuleCategory;
import fag.ware.client.module.data.ModuleInfo;
import net.minecraft.network.packet.Packet;

import java.util.ArrayDeque;

@ModuleInfo(name = "Blink", category = ModuleCategory.PLAYER, description = "Simulates network loss")
public class BlinkModule extends AbstractModule {
    private final ArrayDeque<Packet<?>> outPacketDeque = new ArrayDeque<>();
    private boolean active = false;

    private void releasePackets() {
        while (!outPacketDeque.isEmpty()) {
            sendPacket(outPacketDeque.poll());
        }
    }

    @Subscribe
    public void onPacket(SendPacketEvent event) {
        if (mc.player == null || mc.world == null) {
            active = false;
            return;
        }

        if (active) {
            outPacketDeque.add(event.getPacket());
            event.setCancelled(true);
        }
    }

    @Override
    public void onEnable() {
        outPacketDeque.clear();
        active = true;
    }

    @Override
    public void onDisable() {
        active = false;
        releasePackets();
    }
}