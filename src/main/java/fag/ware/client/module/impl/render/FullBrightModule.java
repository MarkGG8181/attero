package fag.ware.client.module.impl.render;

import fag.ware.client.event.data.Subscribe;
import fag.ware.client.event.impl.player.UpdateEvent;
import fag.ware.client.module.AbstractModule;
import fag.ware.client.module.data.ModuleCategory;
import fag.ware.client.module.data.ModuleInfo;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;

@ModuleInfo(name = "FullBright", description = "Gives you infinite night vision", category = ModuleCategory.RENDER)
public class FullBrightModule extends AbstractModule {
    @Subscribe
    public void onUpdate(UpdateEvent event) {
        if (mc.player.getStatusEffect(StatusEffects.NIGHT_VISION) == null) {
            mc.player.addStatusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, Integer.MAX_VALUE, 255, false, false));
        }
    }

    @Override
    public void onDisable() {
        if (mc.player == null) {
            return;
        }

        mc.player.removeStatusEffect(StatusEffects.NIGHT_VISION);
    }
}