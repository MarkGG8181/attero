package io.github.client.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import io.github.client.Attero;
import io.github.client.event.impl.render.FlipFrameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderSystem.class)
public class RenderSystemMixin {
    @Inject(method = "flipFrame", at = @At("HEAD"))
    private static void onFlipFrame(final CallbackInfo ci) {
        Attero.BUS.post(new FlipFrameEvent());
    }
}