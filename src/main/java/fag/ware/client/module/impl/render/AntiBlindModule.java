package fag.ware.client.module.impl.render;

import fag.ware.client.event.data.Subscribe;
import fag.ware.client.event.impl.render.*;
import fag.ware.client.module.Module;
import fag.ware.client.module.data.ModuleCategory;
import fag.ware.client.module.data.ModuleInfo;
import fag.ware.client.module.data.setting.impl.BooleanSetting;

@ModuleInfo(name = "AntiBlind", category = ModuleCategory.RENDER, description = "Prevents your view from being obstructed")
public class AntiBlindModule extends Module {
    private final BooleanSetting fireOverlay = new BooleanSetting("Fire overlay", true);
    private final BooleanSetting waterOverlay = new BooleanSetting("Water overlay", true);
    private final BooleanSetting wallOverlay = new BooleanSetting("Wall overlay", true);
    private final BooleanSetting portalOverlay = new BooleanSetting("Portal overlay", true);
    private final BooleanSetting nauseaOverlay = new BooleanSetting("Nausea overlay", true);

    @Subscribe
    public void onRenderFireOverlay(RenderFireOverlayEvent event) {
        event.setCancelled(fireOverlay.getValue());
    }

    @Subscribe
    public void onRenderUnderwaterOverlay(RenderUnderwaterOverlayEvent event) {
        event.setCancelled(waterOverlay.getValue());
    }

    @Subscribe
    public void onRenderInWallOverlay(RenderInWallOverlayEvent event) {
        event.setCancelled(wallOverlay.getValue());
    }

    @Subscribe
    public void onRenderPortalOverlay(RenderPortalOverlayEvent event) {
        event.setCancelled(portalOverlay.getValue());
    }

    @Subscribe
    public void onRenderNauseaOverlay(RenderNauseaOverlayEvent event) {
        event.setCancelled(nauseaOverlay.getValue());
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }
}