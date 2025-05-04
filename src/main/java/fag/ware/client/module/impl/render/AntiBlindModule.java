package fag.ware.client.module.impl.render;

import fag.ware.client.event.data.Subscribe;
import fag.ware.client.event.impl.render.*;
import fag.ware.client.module.Module;
import fag.ware.client.module.data.ModuleCategory;
import fag.ware.client.module.data.ModuleInfo;

@ModuleInfo(name = "AntiBlind", category = ModuleCategory.RENDER, description = "Prevents your view from being obstructed")
public class AntiBlindModule extends Module {
    @Subscribe
    public void onRenderFireOverlay(RenderFireOverlayEvent event) {
        event.setCancelled(true);
    }

    @Subscribe
    public void onRenderUnderwaterOverlay(RenderUnderwaterOverlayEvent event) {
        event.setCancelled(true);
    }

    @Subscribe
    public void onRenderInWallOverlay(RenderInWallOverlayEvent event) {
        event.setCancelled(true);
    }

    @Subscribe
    public void onRenderPortalOverlay(RenderPortalOverlayEvent event) {
        event.setCancelled(true);
    }

    @Subscribe
    public void onRenderVignetteOverlay(RenderVignetteOverlayEvent event) {
        event.setCancelled(true);
    }

    @Subscribe
    public void onRenderNauseaOverlay(RenderNauseaOverlayEvent event) {
        event.setCancelled(true);
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }
}