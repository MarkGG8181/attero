package io.github.client.module.impl.player;

import io.github.client.event.data.Subscribe;
import io.github.client.event.impl.player.SendPacketEvent;
import io.github.client.module.AbstractModule;
import io.github.client.module.data.ModuleCategory;
import io.github.client.module.data.ModuleInfo;
import net.minecraft.network.packet.Packet;

import java.util.ArrayDeque;

@ModuleInfo(name = "Blink", category = ModuleCategory.PLAYER, description = "Simulates network loss")
public class BlinkModule extends AbstractModule {
    private final ArrayDeque<Packet<?>> outPacketDeque = new ArrayDeque<>();
    private boolean active = false;

    @Subscribe
    private void onPacket(SendPacketEvent event) {
        if (mc.player == null || mc.world == null) {
            active = false;
            return;
        }

        if (active) {
            outPacketDeque.add(event.packet);
            event.cancelled = true;
        }
    }

    private void releasePackets() {
        while (!outPacketDeque.isEmpty()) {
            sendPacket(outPacketDeque.poll());
        }
    }

    public void onEnable() {
        outPacketDeque.clear();
        active = true;
    }

    public void onDisable() {
        active = false;
        releasePackets();
    }
}