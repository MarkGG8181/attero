package io.github.client.module.impl.render;

import io.github.client.event.data.Subscribe;
import io.github.client.event.impl.player.UpdateEvent;
import io.github.client.module.AbstractModule;
import io.github.client.module.data.ModuleCategory;
import io.github.client.module.data.ModuleInfo;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;

@SuppressWarnings("ALL")
@ModuleInfo(name = "FullBright", description = "Gives you infinite night vision", category = ModuleCategory.RENDER)
public class FullBrightModule extends AbstractModule {
    @Subscribe
    public void onUpdate(UpdateEvent ignoredEvent) {
        if (mc.player.getStatusEffect(StatusEffects.NIGHT_VISION) == null) {
            mc.player.addStatusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, Integer.MAX_VALUE, 255, false, false));
        }
    }

    public void onDisable() {
        if (mc.player == null || mc.world == null) return;

        mc.player.removeStatusEffect(StatusEffects.NIGHT_VISION);
    }
}