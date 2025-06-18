package io.github.client.mixin;

import io.github.client.event.impl.render.RenderFireOverlayEvent;
import io.github.client.event.impl.render.RenderInWallOverlayEvent;
import io.github.client.event.impl.render.RenderUnderwaterOverlayEvent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameOverlayRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author markuss
 */
@Mixin(InGameOverlayRenderer.class)
public class InGameOverlayRendererMixin {
    @Inject(method = "renderFireOverlay", at = @At("HEAD"), cancellable = true)
    private static void renderFireOverlay(MatrixStack matrices, VertexConsumerProvider vertexConsumers, CallbackInfo ci) {
        RenderFireOverlayEvent fireOverlayEvent = new RenderFireOverlayEvent();
        fireOverlayEvent.post();

        if (fireOverlayEvent.isCancelled()) {
            ci.cancel();
        }
    }

    @Inject(method = "renderUnderwaterOverlay", at = @At("HEAD"), cancellable = true)
    private static void renderUnderwaterOverlay(MinecraftClient client, MatrixStack matrices, VertexConsumerProvider vertexConsumers, CallbackInfo ci) {
        RenderUnderwaterOverlayEvent underwaterOverlayEvent = new RenderUnderwaterOverlayEvent();
        underwaterOverlayEvent.post();

        if (underwaterOverlayEvent.isCancelled()) {
            ci.cancel();
        }
    }

    @Inject(method = "renderInWallOverlay", at = @At("HEAD"), cancellable = true)
    private static void renderInWallOverlay(Sprite sprite, MatrixStack matrices, VertexConsumerProvider vertexConsumers, CallbackInfo ci) {
        RenderInWallOverlayEvent inWallOverlayEvent = new RenderInWallOverlayEvent();
        inWallOverlayEvent.post();

        if (inWallOverlayEvent.isCancelled()) {
            ci.cancel();
        }
    }
}