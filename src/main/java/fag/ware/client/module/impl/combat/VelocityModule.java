package fag.ware.client.module.impl.combat;


import fag.ware.client.event.data.Subscribe;
import fag.ware.client.event.impl.TickEvent;
import fag.ware.client.module.Module;
import fag.ware.client.module.data.ModuleCategory;
import fag.ware.client.module.data.ModuleInfo;
import fag.ware.client.module.data.setting.impl.NumberSetting;
import fag.ware.client.module.data.setting.impl.StringSetting;
import net.minecraft.client.option.KeyBinding;

@ModuleInfo(name = "Velocity", category = ModuleCategory.COMBAT, description = "Decreases your knockback")
public class VelocityModule extends Module {
    public final StringSetting mode = new StringSetting("Mode", "Legit", "Legit");
    public final NumberSetting chance = new NumberSetting("Chance", 50, 1, 100);

    // Keeping track of legit velocity,
    boolean jumped;

    @Subscribe
    public void tick(TickEvent event) {
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
        if (chance.getValue().doubleValue() > Math.random() * 100) {
            return true;
        }
        return false;
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }
}
