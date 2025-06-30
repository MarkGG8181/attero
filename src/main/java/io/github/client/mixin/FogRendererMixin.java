package io.github.client.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import io.github.client.event.impl.render.RenderFogEvent;
import net.minecraft.client.render.fog.FogRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/**
 * @author markuss
 */
@Mixin(FogRenderer.class)
public class FogRendererMixin {
    @ModifyExpressionValue(method = "getFogBuffer", at = @At(value = "FIELD", target = "Lnet/minecraft/client/render/fog/FogRenderer;fogEnabled:Z"))
    private boolean modifyFogEnabled(boolean original) {
        RenderFogEvent renderFogEvent = new RenderFogEvent();
        renderFogEvent.post();

        if (renderFogEvent.cancelled) {
            return false;
        }

        return original;
    }
}