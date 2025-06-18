package io.github.client.mixin;

import io.github.client.event.impl.render.GetFogModifierEvent;
import io.github.client.event.impl.render.RenderFogEvent;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.Entity;
import org.joml.Vector4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

/**
 * @author markuss
 */
@Mixin(BackgroundRenderer.class)
public class BackgroundRendererMixin {
    @ModifyArgs(method = "applyFog", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/Fog;<init>(FFLnet/minecraft/client/render/FogShape;FFFF)V"))
    private static void modifyFogDistance(Args args, Camera camera, BackgroundRenderer.FogType fogType, Vector4f color, float viewDistance, boolean thickenFog, float tickDelta) {
        if (fogType == BackgroundRenderer.FogType.FOG_TERRAIN) {
            RenderFogEvent renderFogEvent = new RenderFogEvent();
            renderFogEvent.post();

            if (renderFogEvent.isCancelled()) {
                args.set(0, viewDistance * 4);
                args.set(1, viewDistance * 4.25f);
            }
        }
    }

    @Inject(method = "getFogModifier(Lnet/minecraft/entity/Entity;F)Lnet/minecraft/client/render/BackgroundRenderer$StatusEffectFogModifier;", at = @At("HEAD"), cancellable = true)
    private static void onGetFogModifier(Entity entity, float tickDelta, CallbackInfoReturnable<Object> info) {
        GetFogModifierEvent getFogModifierEvent = new GetFogModifierEvent();
        getFogModifierEvent.post();

        if (getFogModifierEvent.isCancelled()) {
            info.setReturnValue(null);
        }
    }
}