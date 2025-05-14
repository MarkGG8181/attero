package fag.ware.client.module.impl.player;

import fag.ware.client.event.data.Subscribe;
import fag.ware.client.event.impl.TickEvent;
import fag.ware.client.module.AbstractModule;
import fag.ware.client.module.data.ModuleCategory;
import fag.ware.client.module.data.ModuleInfo;
import fag.ware.client.module.data.setting.impl.NumberSetting;
import fag.ware.client.module.data.setting.impl.StringSetting;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;

@SuppressWarnings("ALL")
@ModuleInfo(name = "NoFall", category = ModuleCategory.PLAYER, description = "Makes you take 0 fall damage")
public class NoFallModule extends AbstractModule {
    private StringSetting mode = new StringSetting("Mode", "Damage", "Damage");
    private NumberSetting falldistance = new NumberSetting("Min Fall Distance", 3, 1, 5);

    @Subscribe
    public void tick(TickEvent event) {
        if (mc.player.fallDistance >= falldistance.toFloat()) {
            switch (mode.getValue()) {
                case "Damage" -> {
                    mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.OnGroundOnly(true, false));
                    mc.player.fallDistance = 0;
                }
            }
        }
    }
    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }
}
