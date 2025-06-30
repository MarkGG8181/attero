package io.github.client.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import io.github.client.event.impl.player.ShouldApplyStatusEffectEvent;
import net.minecraft.client.render.fog.StatusEffectFogModifier;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.entry.RegistryEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(StatusEffectFogModifier.class)
public abstract class StatusEffectFogModifierMixin {
    @Shadow
    public abstract RegistryEntry<StatusEffect> getStatusEffect();

    @ModifyReturnValue(method = "shouldApply", at = @At("RETURN"))
    private boolean modifyShouldApply(boolean original) {
        ShouldApplyStatusEffectEvent effectEvent = new ShouldApplyStatusEffectEvent(getStatusEffect());
        effectEvent.post();

        if (effectEvent.cancelled) {
            return false;
        }

        return original;
    }
}
