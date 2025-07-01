package io.github.client.module.impl.world;

import io.github.client.event.data.Subscribe;
import io.github.client.event.impl.game.RunLoopEvent;
import io.github.client.event.impl.game.TickEvent;
import io.github.client.module.data.ModuleCategory;
import io.github.client.module.data.ModuleInfo;
import io.github.client.module.data.rotate.AbstractRotator;
import io.github.client.module.data.setting.impl.BooleanSetting;
import io.github.client.module.data.setting.impl.NumberSetting;
import io.github.client.module.data.setting.impl.RangeNumberSetting;
import io.github.client.util.game.EntityUtil;
import io.github.client.util.game.RotationUtil;
import net.minecraft.entity.decoration.EndCrystalEntity;

import java.util.Comparator;
import java.util.HashSet;

@SuppressWarnings("ALL")
@ModuleInfo(name = "CrystalAura", description = "Attacks nearby crystals", category = ModuleCategory.WORLD)
public class CrystalAuraModule extends AbstractRotator {
    private final RangeNumberSetting speed = new RangeNumberSetting("Speed Min/Max", 10, 180, 10, 180);
    private final NumberSetting searchRange = new NumberSetting("Search range", 5, 1, 10);
    private final NumberSetting attackRange = new NumberSetting("Attack range", 3, 1, 6);
    private final NumberSetting aimRange = new NumberSetting("Aim range", 4.5, 1, 6);
    private final BooleanSetting yCheck = new BooleanSetting("Y check", true);
    private final BooleanSetting nearbyEntityCheck = new BooleanSetting("Nearby entity check", true);
    private final BooleanSetting raycast = new BooleanSetting("Raycast", true);

    private final HashSet<EndCrystalEntity> targets = new HashSet<>();
    private EndCrystalEntity target;

    public CrystalAuraModule() {
        super(70);
    }

    @Subscribe
    private void onTick(TickEvent ignoredEvent) {
        if (mc.player == null || mc.world == null || !canPerform) return;

        if (target != null) {
            var foundNearbyEntity = nearbyEntityCheck.getValue() ? mc.world.getEntitiesByClass(
                    net.minecraft.entity.Entity.class,
                    target.getBoundingBox().expand(3.0),
                    e -> e != mc.player && !(e instanceof EndCrystalEntity)
            ).size() > 0 : true;

            var stillValid = target.isAlive()
                    && EntityUtil.isWithinRange(target, aimRange.toDouble())
                    && foundNearbyEntity
                    && mc.player.getBlockPos().equals(target.getBlockPos().down(1));

            if (!stillValid) {
                target = null;
                targets.clear();
            }
        }

        if (target == null) {
            var range = searchRange.toDouble();

            var entitiesToConsider = mc.world.getEntitiesByClass(
                    EndCrystalEntity.class,
                    mc.player.getBoundingBox().expand(range),
                    entity -> {
                        if (!entity.isAttackable() || !EntityUtil.isWithinRange(entity, range))
                            return false;

                        boolean foundNearbyEntity = nearbyEntityCheck.getValue() ? mc.world.getEntitiesByClass(
                                net.minecraft.entity.Entity.class,
                                entity.getBoundingBox().expand(3.0),
                                e -> e != mc.player && !(e instanceof EndCrystalEntity)
                        ).size() > 0 : true;

                        double crystalBaseY = entity.getBlockPos().getY();
                        double playerFeetY = mc.player.getBoundingBox().minY; // bottom of the player's hitbox

                        boolean playerUnderneath = yCheck.getValue() ? playerFeetY <= (crystalBaseY - 1.0) : true;
                        return foundNearbyEntity && playerUnderneath;
                    }
            );

            entitiesToConsider.sort(Comparator.comparingDouble(entity -> mc.player.squaredDistanceTo(entity)));

            targets.clear();
            targets.addAll(entitiesToConsider);

            var localTarget = targets.stream().findFirst().orElse(null);
            if (localTarget != null && EntityUtil.isWithinRange(localTarget, aimRange.toDouble())) {
                target = localTarget;
            }
        }
    }

    @Subscribe
    private void onRunLoop(RunLoopEvent ignoredEvent) {
        if (mc.player == null || mc.world == null || mc.currentScreen != null || !canPerform) return;

        if (target != null && EntityUtil.isWithinRange(target, attackRange.toDouble())) {
            EntityUtil.attackEntity(target);
            target = null;
        }
    }

    @Override
    public float[] shouldRotate() {
        if (target != null && (raycast.getValue() && mc.player.canSee(target))) {
            return RotationUtil.toRotation(target, speed.getMinAsFloat(), speed.getMaxAsFloat());
        }

        return null;
    }
}