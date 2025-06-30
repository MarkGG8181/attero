package io.github.client.module.impl.combat;

import io.github.client.event.data.Subscribe;
import io.github.client.event.impl.player.ReceivePacketEvent;
import io.github.client.event.impl.game.TickEvent;
import io.github.client.util.java.interfaces.IEntityVelocityPacketAccessor;
import io.github.client.module.AbstractModule;
import io.github.client.module.data.ModuleCategory;
import io.github.client.module.data.ModuleInfo;
import io.github.client.module.data.setting.impl.NumberSetting;
import io.github.client.module.data.setting.impl.StringSetting;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;

@SuppressWarnings("ALL")
@ModuleInfo(name = "Velocity", category = ModuleCategory.COMBAT, description = "Decreases your knockback")
public class VelocityModule extends AbstractModule {
    private final StringSetting mode = new StringSetting("Mode", "Legit", "Legit", "Packet");
    private final NumberSetting chance = (NumberSetting) new NumberSetting("Chance", 50, 1, 100).hide(() -> !mode.is("Legit"));
    private final NumberSetting horizontal = (NumberSetting) new NumberSetting("Horizontal", 1, 0, 1).hide(() -> !mode.is("Packet"));
    private final NumberSetting vertical = (NumberSetting) new NumberSetting("Vertical", 0, 0, 1).hide(() -> !mode.is("Packet"));

    //Keeping track of legit velocity
    private boolean jumped;

    @Subscribe
    private void onReceivePacket(ReceivePacketEvent event) {
        if (mc.player == null || mc.world == null) return;

        switch (mode.getValue()) {
            case "Packet" -> {
                if (event.packet instanceof EntityVelocityUpdateS2CPacket packet && packet.getEntityId() == mc.player.getId()) {
                    var accessor = (IEntityVelocityPacketAccessor) packet;

                    var newX = (int) (packet.getVelocityX() * 8000.0 * horizontal.toDouble());
                    var newY = (int) (packet.getVelocityY() * 8000.0 * vertical.toDouble());
                    var newZ = (int) (packet.getVelocityZ() * 8000.0 * horizontal.toDouble());

                    accessor.setVelocityX(newX);
                    accessor.setVelocityY(newY);
                    accessor.setVelocityZ(newZ);
                }
            }
        }
    }

    @Subscribe
    private void onTick(TickEvent event) {
        if (mc.player == null || mc.world == null) return;

        switch (mode.getValue()) {
            case "Legit" -> {
                if (tookDamage() && chanceCheck()) {
                    KeyBinding.setKeyPressed(mc.options.jumpKey.getDefaultKey(), true);
                    jumped = true;
                }

                if (jumped) {
                    KeyBinding.setKeyPressed(mc.options.jumpKey.getDefaultKey(), false);
                    jumped = false;
                }
            }
        }
    }

    private boolean tookDamage() {
        return mc.player.hurtTime == 9 && mc.currentScreen == null;
    }

    private boolean chanceCheck() {
        return chance.getValue().doubleValue() > Math.random() * 100;
    }

    @Override
    public String getSuffix() {
        if (mode.is("Packet")) {
            return horizontal.toInt() + "% " + vertical.toInt() + "%";
        }

        return mode.getValue();
    }
}