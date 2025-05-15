package fag.ware.client.module.impl.render;

import fag.ware.client.event.data.Subscribe;
import fag.ware.client.event.impl.render.*;
import fag.ware.client.module.AbstractModule;
import fag.ware.client.module.data.ModuleCategory;
import fag.ware.client.module.data.ModuleInfo;
import fag.ware.client.module.data.setting.impl.BooleanSetting;
import fag.ware.client.module.data.setting.impl.GroupSetting;
import fag.ware.client.module.data.setting.impl.MultiStringSetting;
import fag.ware.client.util.player.WorldUtil;
import net.minecraft.entity.EntityType;

@ModuleInfo(name = "NoRender", category = ModuleCategory.RENDER, description = "Prevents your view from being obstructed")
public class NoRenderModule extends AbstractModule {
    private final GroupSetting overlays = new GroupSetting("Overlays", false);
    private final BooleanSetting fireOverlay = (BooleanSetting) new BooleanSetting("Fire overlay", true).setParent(overlays);
    private final BooleanSetting waterOverlay = (BooleanSetting) new BooleanSetting("Water overlay", true).setParent(overlays);
    private final BooleanSetting wallOverlay = (BooleanSetting) new BooleanSetting("Wall overlay", true).setParent(overlays);
    private final BooleanSetting portalOverlay = (BooleanSetting) new BooleanSetting("Portal overlay", false).setParent(overlays);
    private final BooleanSetting nauseaOverlay = (BooleanSetting) new BooleanSetting("Nausea overlay", true).setParent(overlays);
    private final BooleanSetting spyglassOverlay = (BooleanSetting) new BooleanSetting("Spyglass overlay", false).setParent(overlays);
    private final BooleanSetting pumpkinOverlay = (BooleanSetting) new BooleanSetting("Pumpkin overlay", true).setParent(overlays);
    private final BooleanSetting powderedSnowOverlay = (BooleanSetting) new BooleanSetting("Powdered snow overlay", true).setParent(overlays);

    private final GroupSetting blocks = new GroupSetting("Blocks", false);
    private final BooleanSetting blockBreakParticles = (BooleanSetting) new BooleanSetting("Block break particles", true).setParent(blocks);
    private final BooleanSetting blockBreakingParticles = (BooleanSetting) new BooleanSetting("Block breaking particles", true).setParent(blocks);
    private final BooleanSetting fallingLeavesParticles = (BooleanSetting) new BooleanSetting("Leaf block falling leaves", true).setParent(blocks);
    private final BooleanSetting leafBlockWaterDrip = (BooleanSetting) new BooleanSetting("Leaf block water drip", true).setParent(blocks);

    private final GroupSetting weather = new GroupSetting("Weather", false);

    private final BooleanSetting precipitation = (BooleanSetting) new BooleanSetting("Precipitation", false).setParent(weather);
    private final BooleanSetting rainParticles = (BooleanSetting) new BooleanSetting("Rain particles & sounds", false).setParent(weather);
    private final BooleanSetting fog = (BooleanSetting) new BooleanSetting("Fog", false).setParent(weather);

    private final GroupSetting world = new GroupSetting("World", false);

    private final BooleanSetting worldBorder = (BooleanSetting) new BooleanSetting("World border", true).setParent(world);
    private final BooleanSetting beaconBeams = (BooleanSetting) new BooleanSetting("Beacon beams", true).setParent(world);
    private final BooleanSetting signText = (BooleanSetting) new BooleanSetting("Sign text", false).setParent(world);
    private final BooleanSetting fallingBlocks = (BooleanSetting) new BooleanSetting("Falling blocks", false).setParent(world);
    private final MultiStringSetting entities = (MultiStringSetting) new MultiStringSetting("Entities", WorldUtil.getExampleEntities(), WorldUtil.getAllEntityNames()).setParent(world);

    private final GroupSetting client = new GroupSetting("Client", false);
    private final BooleanSetting blindness = (BooleanSetting) new BooleanSetting("Blindness", true).setParent(client);
    private final BooleanSetting darkness = (BooleanSetting) new BooleanSetting("Darkness", true).setParent(client);
    private final BooleanSetting eatParticles = (BooleanSetting) new BooleanSetting("Eating particles", true).setParent(client);
    private final BooleanSetting totemAnimation = (BooleanSetting) new BooleanSetting("Totem animation", true).setParent(client);
    private final BooleanSetting enchantingGlint = (BooleanSetting) new BooleanSetting("Enchanting glint", true).setParent(client);

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
    public void onRenderSpyglassOverlay(RenderSpyglassOverlayEvent event) {
        event.setCancelled(spyglassOverlay.getValue());
    }

    @Subscribe
    public void onRenderPumpkinOverlay(RenderPumpkinOverlayEvent event) {
        event.setCancelled(pumpkinOverlay.getValue());
    }

    @Subscribe
    public void onRenderPowderedSnowOverlay(RenderPowderedSnowOverlay event) {
        event.setCancelled(powderedSnowOverlay.getValue());
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
    public void onSpawnDrippingWaterFromLeaves(SpawnLeavesWaterParticlesEvent event) {
        event.setCancelled(leafBlockWaterDrip.getValue());
    }

    @Subscribe
    public void onSpawnFallingLeaves(SpawnLeavesFallingParticlesEvent event) {
        event.setCancelled(fallingLeavesParticles.getValue());
    }

    @Subscribe
    public void onRenderFog(RenderFogEvent event) {
        event.setCancelled(fog.getValue());
    }

    @Subscribe
    public void onRenderFallingBlocks(RenderFallingBlockEvent event) {
        event.setCancelled(fallingBlocks.getValue());
    }

    @Subscribe
    public void onRenderWorldBorder(RenderWorldBorderEvent event) {
        event.setCancelled(worldBorder.getValue());
    }

    @Subscribe
    public void onRenderBeaconBeam(RenderBeaconBeamEvent event) {
        event.setCancelled(beaconBeams.getValue());
    }

    @Subscribe
    public void onRenderSignText(RenderSignTextEvent event) {
        event.setCancelled(signText.getValue());
    }

    @Subscribe
    public void onRenderEntity(RenderEntityEvent event) {
        if (event.getEntity() != null) {
            String entityName = EntityType.getId(event.getEntity().getType()).getPath();
            if (entities.enabled(WorldUtil.toClassName(entityName))) {
                event.setCancelled(true);
            }
        }
    }

    @Subscribe
    public void onRenderTotemAnimation(RenderTotemAnimationEvent event) {
        event.setCancelled(totemAnimation.getValue());
    }

    @Subscribe
    public void onRenderEatingParticles(RenderEatingParticlesEvent event) {
        event.setCancelled(eatParticles.getValue());
    }

    @Subscribe
    public void onRenderItemEnchantmentGlint(RenderItemEnchantmentGlintEvent event) {
        event.setCancelled(enchantingGlint.getValue());
    }

    @Override
    public void onEnable() {
    }

    @Override
    public void onDisable() {
    }
}