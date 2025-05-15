package fag.ware.client.mixin;

import fag.ware.client.event.impl.render.AddParticlesAndSoundEvent;
import fag.ware.client.event.impl.render.RenderPrecipitationEvent;
import net.minecraft.client.render.WeatherRendering;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author markuss
 */
@Mixin(WeatherRendering.class)
public class WeatherRenderingMixin {
    @Inject(method = "renderPrecipitation(Lnet/minecraft/world/World;Lnet/minecraft/client/render/VertexConsumerProvider;IFLnet/minecraft/util/math/Vec3d;)V", at = @At("HEAD"), cancellable = true)
    private void renderPrecipitation(CallbackInfo ci) {
        RenderPrecipitationEvent renderPrecipitationEvent = new RenderPrecipitationEvent();
        renderPrecipitationEvent.post();

        if (renderPrecipitationEvent.isCancelled()) {
            ci.cancel();
        }
    }

    @Inject(method = "addParticlesAndSound", at = @At("HEAD"), cancellable = true)
    private void addParticlesAndSound(CallbackInfo ci) {
        AddParticlesAndSoundEvent addParticlesAndSoundEvent = new AddParticlesAndSoundEvent();
        addParticlesAndSoundEvent.post();

        if (addParticlesAndSoundEvent.isCancelled()) {
            ci.cancel();
        }
    }
}