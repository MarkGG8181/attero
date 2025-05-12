package fag.ware.client.module.impl.combat;


import fag.ware.client.event.data.Subscribe;
import fag.ware.client.event.impl.ReceivePacketEvent;
import fag.ware.client.event.impl.TickEvent;
import fag.ware.client.util.IEntityVelocityPacketAccessor;
import fag.ware.client.module.Module;
import fag.ware.client.module.data.ModuleCategory;
import fag.ware.client.module.data.ModuleInfo;
import fag.ware.client.module.data.setting.impl.NumberSetting;
import fag.ware.client.module.data.setting.impl.StringSetting;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;

@ModuleInfo(name = "Velocity", category = ModuleCategory.COMBAT, description = "Decreases your knockback")
public class VelocityModule extends Module {
    private final StringSetting mode = new StringSetting("Mode", "Legit", "Legit", "Packet");
    private final NumberSetting chance = (NumberSetting) new NumberSetting("Chance", 50, 1, 100).hide(() -> !mode.is("Legit"));
    private final NumberSetting horizontal = (NumberSetting) new NumberSetting("Horizontal", 1, 0, 1).hide(() -> !mode.is("Packet"));
    private final NumberSetting vertical = (NumberSetting) new NumberSetting("Vertical", 0, 0, 1).hide(() -> !mode.is("Packet"));

    // Keeping track of legit velocity
    private boolean jumped;

    @Subscribe
    public void onReceivePacket(ReceivePacketEvent event) {
        if (mc.player == null || mc.world == null) return;

        switch (mode.getValue()) {
            case "Packet" -> {
                if (event.getPacket() instanceof EntityVelocityUpdateS2CPacket packet && packet.getEntityId() == mc.player.getId()) {
                    IEntityVelocityPacketAccessor accessor = (IEntityVelocityPacketAccessor) packet;

                    int newX = (int) (packet.getVelocityX() * 8000.0 * horizontal.toDouble());
                    int newY = (int) (packet.getVelocityY() * 8000.0 * vertical.toDouble());
                    int newZ = (int) (packet.getVelocityZ() * 8000.0 * horizontal.toDouble());

                    accessor.setVelocityX(newX);
                    accessor.setVelocityY(newY);
                    accessor.setVelocityZ(newZ);
                }
            }
        }
    }

    @Subscribe
    public void onTick(TickEvent event) {
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

    public boolean tookDamage() {
        return mc.player.hurtTime == 9 && mc.currentScreen == null;
    }

    public boolean chanceCheck() {
        return chance.getValue().doubleValue() > Math.random() * 100;
    }

    @Override
    public void onEnable() {
    }

    @Override
    public void onDisable() {
    }
}