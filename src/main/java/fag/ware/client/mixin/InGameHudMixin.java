package fag.ware.client.mixin;

import fag.ware.client.event.impl.render.*;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

/**
 * @author markuss
 */
@Mixin(InGameHud.class)
public class InGameHudMixin {
    @Inject(method = "renderCrosshair", at = @At("HEAD"))
    private void renderCrosshair(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        new Render2DEvent(context).post();
    }

    @ModifyArgs(method = "renderMiscOverlays", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;renderOverlay(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/util/Identifier;F)V", ordinal = 0))
    private void onRenderPumpkinOverlay(Args args) {
        RenderPumpkinOverlayEvent renderPumpkinOverlayEvent = new RenderPumpkinOverlayEvent();
        renderPumpkinOverlayEvent.post();

        if (renderPumpkinOverlayEvent.isCancelled()) {
            args.set(2, 0f);
        }
    }

    @ModifyArgs(method = "renderMiscOverlays", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;renderOverlay(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/util/Identifier;F)V", ordinal = 1))
    private void onRenderPowderedSnowOverlay(Args args) {
        RenderPowderedSnowOverlay renderPowderedSnowOverlay = new RenderPowderedSnowOverlay();
        renderPowderedSnowOverlay.post();

        if (renderPowderedSnowOverlay.isCancelled()) {
            args.set(2, 0f);
        }
    }

    @Inject(method = "renderSpyglassOverlay", at = @At("HEAD"), cancellable = true)
    private void onRenderSpyglassOverlay(DrawContext context, float scale, CallbackInfo ci) {
        RenderSpyglassOverlayEvent renderSpyglassOverlayEvent = new RenderSpyglassOverlayEvent();
        renderSpyglassOverlayEvent.post();

        if (renderSpyglassOverlayEvent.isCancelled()) {
            ci.cancel();
        }
    }

    @Inject(method = "renderVignetteOverlay", at = @At("HEAD"), cancellable = true)
    private void renderVignetteOverlay(DrawContext context, Entity entity, CallbackInfo ci) {
        ci.cancel();
    }

    @Inject(method = "renderNauseaOverlay", at = @At("HEAD"), cancellable = true)
    private void renderNauseaOverlay(DrawContext context, float nauseaStrength, CallbackInfo ci) {
        RenderNauseaOverlayEvent renderNauseaOverlayEvent = new RenderNauseaOverlayEvent();
        renderNauseaOverlayEvent.post();

        if (renderNauseaOverlayEvent.isCancelled()) {
            ci.cancel();
        }
    }

    @Inject(method = "renderPortalOverlay", at = @At("HEAD"), cancellable = true)
    private void renderPortalOverlay(DrawContext context, float nauseaStrength, CallbackInfo ci) {
        RenderPortalOverlayEvent renderPortalOverlayEvent = new RenderPortalOverlayEvent();
        renderPortalOverlayEvent.post();

        if (renderPortalOverlayEvent.isCancelled()) {
            ci.cancel();
        }
    }
}
