package fag.ware.client.mixin;

import fag.ware.client.event.impl.render.Render2DEvent;
import fag.ware.client.event.impl.render.RenderNauseaOverlayEvent;
import fag.ware.client.event.impl.render.RenderPortalOverlayEvent;
import fag.ware.client.event.impl.render.RenderVignetteOverlayEvent;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class InGameHudMixin {

    @Inject(method = "renderCrosshair", at = @At("HEAD"))
    private void renderCrosshair(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        new Render2DEvent(context).post();
    }

    @Inject(method = "renderVignetteOverlay", at = @At("HEAD"), cancellable = true)
    private void renderVignetteOverlay(DrawContext context, Entity entity, CallbackInfo ci) {
        RenderVignetteOverlayEvent vignetteOverlayEvent = new RenderVignetteOverlayEvent();
        vignetteOverlayEvent.post();

        if (vignetteOverlayEvent.isCancelled()) {
            ci.cancel();
        }
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
