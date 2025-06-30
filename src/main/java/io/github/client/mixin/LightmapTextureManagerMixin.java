package io.github.client.mixin;

import io.github.client.event.impl.render.GetDarknessEvent;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * @author markuss
 */
@Mixin(LightmapTextureManager.class)
public abstract class LightmapTextureManagerMixin {
    @Inject(method = "getDarkness", at = @At("HEAD"), cancellable = true)
    private void getDarknessFactor(LivingEntity entity, float factor, float tickProgress, CallbackInfoReturnable<Float> info) {
        GetDarknessEvent getDarknessEvent = new GetDarknessEvent();
        getDarknessEvent.post();

        if (getDarknessEvent.cancelled) {
            info.setReturnValue(0.0f);
        }
    }
}