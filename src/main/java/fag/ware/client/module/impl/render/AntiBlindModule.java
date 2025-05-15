package fag.ware.client.module.impl.render;

import fag.ware.client.event.data.Subscribe;
import fag.ware.client.event.impl.render.*;
import fag.ware.client.module.AbstractModule;
import fag.ware.client.module.data.ModuleCategory;
import fag.ware.client.module.data.ModuleInfo;
import fag.ware.client.module.data.setting.impl.BooleanSetting;
import fag.ware.client.module.data.setting.impl.GroupSetting;

@ModuleInfo(name = "AntiBlind", category = ModuleCategory.RENDER, description = "Prevents your view from being obstructed")
public class AntiBlindModule extends AbstractModule {
    private final GroupSetting overlays = new GroupSetting("Overlays", false);
    private final BooleanSetting fireOverlay = (BooleanSetting) new BooleanSetting("Fire overlay", true).setParent(overlays);
    private final BooleanSetting waterOverlay = (BooleanSetting) new BooleanSetting("Water overlay", true).setParent(overlays);
    private final BooleanSetting wallOverlay = (BooleanSetting) new BooleanSetting("Wall overlay", true).setParent(overlays);
    private final BooleanSetting portalOverlay = (BooleanSetting) new BooleanSetting("Portal overlay", true).setParent(overlays);
    private final BooleanSetting nauseaOverlay = (BooleanSetting) new BooleanSetting("Nausea overlay", true).setParent(overlays);

    private final GroupSetting blocks = new GroupSetting("Blocks", false);
    private final BooleanSetting blockBreakParticles = (BooleanSetting) new BooleanSetting("Block break particles", true).setParent(blocks);
    private final BooleanSetting blockBreakingParticles = (BooleanSetting) new BooleanSetting("Block breaking particles", true).setParent(blocks);
    private final BooleanSetting fallingLeavesParticles = (BooleanSetting) new BooleanSetting("Leaf block falling leaves", true).setParent(blocks);
    private final BooleanSetting leafBlockWaterDrip = (BooleanSetting) new BooleanSetting("Leaf block water drip", true).setParent(blocks);

    private final GroupSetting weather = new GroupSetting("Weather", false);

    private final BooleanSetting precipitation = (BooleanSetting) new BooleanSetting("Precipitation", false).setParent(weather);
    private final BooleanSetting rainParticles = (BooleanSetting) new BooleanSetting("Rain particles & sounds", false).setParent(weather);

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

    @Subscribe
    public void onRenderBlockBreakingParticles(AddBlockBreakingParticleEvent event) {
        event.setCancelled(blockBreakingParticles.getValue());
    }

    @Subscribe
    public void onRenderBlockBreakParticles(AddBlockBreakParticleEvent event) {
        event.setCancelled(blockBreakParticles.getValue());
    }

    @Subscribe
    public void onRenderPrecipitation(RenderPrecipitationEvent event) {
        event.setCancelled(precipitation.getValue());
    }

    @Subscribe
    public void onParticlesAndSoundEvent(AddParticlesAndSoundEvent event) {
        event.setCancelled(rainParticles.getValue());
    }

    @Subscribe
    public void onSpawnDrippingWaterFromLeaves(SpawnLeavesWaterParticles event) {
        event.setCancelled(leafBlockWaterDrip.getValue());
    }

    @Subscribe
    public void onSpawnFallingLeaves(SpawnLeavesFallingParticles event) {
        event.setCancelled(fallingLeavesParticles.getValue());
    }

    @Override
    public void onEnable() {
    }

    @Override
    public void onDisable() {
    }
}