package io.github.client.module.impl.misc;

import io.github.client.event.data.Subscribe;
import io.github.client.event.impl.player.SendPacketEvent;
import io.github.client.module.AbstractModule;
import io.github.client.module.data.ModuleCategory;
import io.github.client.module.data.ModuleInfo;
import io.github.client.module.data.setting.impl.StringSetting;
import net.minecraft.network.packet.c2s.common.CommonPongC2SPacket;

@ModuleInfo(name = "Disabler", description = "Disables anticheats or parts of them", category = ModuleCategory.MISC)
public class DisablerModule extends AbstractModule {
    private final StringSetting mode = new StringSetting("Mode", "Intave Cloud", "Intave Cloud");

    @Subscribe
    private void onSendPacket(SendPacketEvent event) {
        switch (mode.getValue()) {
            case "Intave Cloud" -> {
                if (event.getPacket() instanceof CommonPongC2SPacket) {
                    event.cancelled = true;
                }
            }
        }
    }

    @Override
    public String getSuffix() {
        return mode.getValue();
    }
}