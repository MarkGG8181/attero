package io.github.client.module.impl.render;

import io.github.client.event.data.Subscribe;
import io.github.client.event.impl.player.ShouldApplyStatusEffectEvent;
import io.github.client.event.impl.render.*;
import io.github.client.module.AbstractModule;
import io.github.client.module.data.ModuleCategory;
import io.github.client.module.data.ModuleInfo;
import io.github.client.module.data.setting.impl.BooleanSetting;
import io.github.client.module.data.setting.impl.GroupSetting;
import io.github.client.module.data.setting.impl.MultiStringSetting;
import io.github.client.util.java.SystemUtil;
import io.github.client.util.game.EntityUtil;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.StatusEffects;

@ModuleInfo(name = "NoRender", category = ModuleCategory.RENDER, description = "Prevents your view from being obstructed")
public class NoRenderModule extends AbstractModule {
    private final GroupSetting overlays = new GroupSetting("Overlays", false);
    private final BooleanSetting fireOverlay = new BooleanSetting("Fire overlay", true).setParent(overlays);
    private final BooleanSetting waterOverlay = new BooleanSetting("Water overlay", true).setParent(overlays);
    private final BooleanSetting wallOverlay = new BooleanSetting("Wall overlay", true).setParent(overlays);
    private final BooleanSetting portalOverlay = new BooleanSetting("Portal overlay", false).setParent(overlays);
    private final BooleanSetting nauseaOverlay = new BooleanSetting("Nausea overlay", true).setParent(overlays);
    private final BooleanSetting spyglassOverlay = new BooleanSetting("Spyglass overlay", false).setParent(overlays);
    private final BooleanSetting pumpkinOverlay = new BooleanSetting("Pumpkin overlay", true).setParent(overlays);
    private final BooleanSetting powderedSnowOverlay = new BooleanSetting("Powdered snow overlay", true).setParent(overlays);

    private final GroupSetting blocks = new GroupSetting("Blocks", false);
    private final BooleanSetting blockBreakParticles = new BooleanSetting("Block break particles", true).setParent(blocks);
    private final BooleanSetting blockBreakingParticles = new BooleanSetting("Block breaking particles", true).setParent(blocks);
    private final BooleanSetting fallingLeavesParticles = new BooleanSetting("Leaf block falling leaves", true).setParent(blocks);
    private final BooleanSetting leafBlockWaterDrip = new BooleanSetting("Leaf block water drip", true).setParent(blocks);

    private final GroupSetting weather = new GroupSetting("Weather", false);

    private final BooleanSetting precipitation = new BooleanSetting("Precipitation", false).setParent(weather);
    private final BooleanSetting rainParticles = new BooleanSetting("Rain particles & sounds", false).setParent(weather);
    private final BooleanSetting fog = new BooleanSetting("Fog", false).setParent(weather);

    private final GroupSetting world = new GroupSetting("World", false);

    private final BooleanSetting worldBorder = new BooleanSetting("World border", true).setParent(world);
    private final BooleanSetting beaconBeams = new BooleanSetting("Beacon beams", true).setParent(world);
    private final BooleanSetting signText = new BooleanSetting("Sign text", false).setParent(world);
    private final BooleanSetting fallingBlocks = new BooleanSetting("Falling blocks", false).setParent(world);
    private final MultiStringSetting entities = new MultiStringSetting("Entities", EntityUtil.getExampleEntities(), EntityUtil.getAllEntities()).setParent(world);

    private final GroupSetting client = new GroupSetting("Client", false);
    private final BooleanSetting blindness = new BooleanSetting("Blindness", true).setParent(client);
    private final BooleanSetting darkness = new BooleanSetting("Darkness", true).setParent(client);
    private final BooleanSetting eatParticles = new BooleanSetting("Eating particles", true).setParent(client);
    private final BooleanSetting totemAnimation = new BooleanSetting("Totem animation", true).setParent(client);
    private final BooleanSetting enchantingGlint = new BooleanSetting("Enchanting glint", true).setParent(client);

    @Subscribe
    private void onRenderFireOverlay(RenderFireOverlayEvent event) {
        event.cancelled = fireOverlay.getValue();
    }

    @Subscribe
    private void onRenderUnderwaterOverlay(RenderUnderwaterOverlayEvent event) {
        event.cancelled = waterOverlay.getValue();
    }

    @Subscribe
    private void onRenderInWallOverlay(RenderInWallOverlayEvent event) {
        event.cancelled = wallOverlay.getValue();
    }

    @Subscribe
    private void onRenderPortalOverlay(RenderPortalOverlayEvent event) {
        event.cancelled = portalOverlay.getValue();
    }

    @Subscribe
    private void onRenderNauseaOverlay(RenderNauseaOverlayEvent event) {
        event.cancelled = nauseaOverlay.getValue();
    }

    @Subscribe
    private void onRenderSpyglassOverlay(RenderSpyglassOverlayEvent event) {
        event.cancelled = spyglassOverlay.getValue();
    }

    @Subscribe
    private void onRenderPumpkinOverlay(RenderPumpkinOverlayEvent event) {
        event.cancelled = pumpkinOverlay.getValue();
    }

    @Subscribe
    private void onRenderPowderedSnowOverlay(RenderPowderedSnowOverlay event) {
        event.cancelled = powderedSnowOverlay.getValue();
    }

    @Subscribe
    private void onRenderBlockBreakingParticles(AddBlockBreakingParticleEvent event) {
        event.cancelled = blockBreakingParticles.getValue();
    }

    @Subscribe
    private void onRenderBlockBreakParticles(AddBlockBreakParticleEvent event) {
        event.cancelled = blockBreakParticles.getValue();
    }

    @Subscribe
    private void onRenderPrecipitation(RenderPrecipitationEvent event) {
        event.cancelled = precipitation.getValue();
    }

    @Subscribe
    private void onParticlesAndSoundEvent(AddParticlesAndSoundEvent event) {
        event.cancelled = rainParticles.getValue();
    }

    @Subscribe
    private void onSpawnDrippingWaterFromLeaves(SpawnLeavesWaterParticlesEvent event) {
        event.cancelled = leafBlockWaterDrip.getValue();
    }

    @Subscribe
    private void onSpawnFallingLeaves(SpawnLeavesFallingParticlesEvent event) {
        event.cancelled = fallingLeavesParticles.getValue();
    }

    @Subscribe
    private void onRenderFog(RenderFogEvent event) {
        event.cancelled = fog.getValue();
    }

    @Subscribe
    private void onRenderFallingBlocks(RenderFallingBlockEvent event) {
        event.cancelled = fallingBlocks.getValue();
    }

    @Subscribe
    private void onRenderWorldBorder(RenderWorldBorderEvent event) {
        event.cancelled = worldBorder.getValue();
    }

    @Subscribe
    private void onRenderBeaconBeam(RenderBeaconBeamEvent event) {
        event.cancelled = beaconBeams.getValue();
    }

    @Subscribe
    private void onRenderSignText(RenderSignTextEvent event) {
        event.cancelled = signText.getValue();
    }

    @Subscribe
    private void onRenderEntity(RenderEntityEvent event) {
        if (event.entity != null) {
            var entityName = EntityType.getId(event.entity.getType()).getPath();
            if (entities.enabled(SystemUtil.toClassName(entityName))) {
                event.cancelled = true;
            }
        }
    }

    @Subscribe
    private void onRenderTotemAnimation(RenderTotemAnimationEvent event) {
        event.cancelled = totemAnimation.getValue();
    }

    @Subscribe
    private void onRenderEatingParticles(RenderEatingParticlesEvent event) {
        event.cancelled = eatParticles.getValue();
    }

    @Subscribe
    private void onRenderItemEnchantmentGlint(RenderItemEnchantmentGlintEvent event) {
        event.cancelled = enchantingGlint.getValue();
    }

    @Subscribe
    private void hasBlindness(HasBlindnessEvent event) {
        event.cancelled = blindness.getValue();
    }

    @Subscribe
    private void hasDarkness(HasDarknessEvent event) {
        event.cancelled = darkness.getValue();
    }

    @Subscribe
    private void getDarkness(GetDarknessEvent event) {
        event.cancelled = darkness.getValue();
    }

    @Subscribe
    private void getFogModifier(ShouldApplyStatusEffectEvent event) {
        event.cancelled = (event.effect == StatusEffects.BLINDNESS && blindness.getValue()) || (event.effect == StatusEffects.DARKNESS && darkness.getValue());
    }
}