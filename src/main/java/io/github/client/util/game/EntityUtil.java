package io.github.client.util.game;

import io.github.client.tracker.impl.FriendTracker;
import io.github.client.util.java.SystemUtil;
import io.github.client.util.java.interfaces.IMinecraft;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.Registries;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class EntityUtil implements IMinecraft {
    public static boolean hasVisiblePotionEffects(PlayerEntity player) {
        for (StatusEffectInstance effect : player.getStatusEffects()) {
            if (effect.shouldShowIcon()) {
                return true;
            }
        }
        return false;
    }

    public static String[] getAllEntities() {
        List<String> names = new ArrayList<>();

        for (EntityType<?> type : Registries.ENTITY_TYPE) {
            Identifier id = Registries.ENTITY_TYPE.getId(type);
            names.add(SystemUtil.toClassName(id.getPath()));
        }

        return names.toArray(new String[0]);
    }

    public static String[] getExampleEntities() {
        return new String[]{
                "ArmorStand",
                "ExperienceOrb"
        };
    }

    public static boolean isWithinRange(Entity entity, double range) {
        double distanceSquared = mc.player.squaredDistanceTo(entity);

        double entityRadius = entity.getWidth() / 2;
        double effectiveRange = range + entityRadius;

        double effectiveRangeSquared = effectiveRange * effectiveRange;

        return distanceSquared <= effectiveRangeSquared;
    }

    public static void attackEntity(Entity entity) {
        if (entity != null && entity.isAlive()) {
            mc.interactionManager.attackEntity(MinecraftClient.getInstance().player, entity);
            mc.player.swingHand(Hand.MAIN_HAND);
        }
    }

    public static boolean shouldIncludeEntity(LivingEntity livingEnt, boolean players, boolean animals, boolean monsters, boolean invisibles) {
        if (livingEnt instanceof PlayerEntity && players) return true;
        if (livingEnt instanceof AnimalEntity && animals) return true;
        if (livingEnt instanceof MobEntity && monsters) return true;
        if (livingEnt.isInvisible() && invisibles) return true;

        return false;
    }

    public static LivingEntity getTarget(LivingEntity target, double aimRange, double searchRange, String sortBy, boolean players, boolean animals, boolean monsters, boolean invisibles) {
        if (mc.player == null || mc.world == null) return null;

        if (target != null && (!target.isAlive() || !isWithinRange(target, aimRange))) {
            return null;
        }

        if (target != null
                && target.isAlive()
                && isWithinRange(target, aimRange)
                && shouldIncludeEntity(target, players, animals, monsters, invisibles)) {
            return target;
        }

        List<LivingEntity> entitiesToConsider = mc.world.getEntitiesByClass(
                LivingEntity.class,
                mc.player.getBoundingBox().expand(searchRange),
                entity -> !entity.equals(mc.player)
                        && shouldIncludeEntity(entity, players, animals, monsters, invisibles)
                        && entity.isAttackable()
                        && !entity.isDead()
                        && entity.isAlive()
                        && !entity.getName().getString().isBlank()
                        && !FriendTracker.INSTANCE.list.contains(entity.getName().getString())
                        && isWithinRange(entity, searchRange)
        );

        switch (sortBy) {
            case "Health" -> entitiesToConsider.sort(Comparator.comparingDouble(LivingEntity::getHealth));
            case "Armor" -> entitiesToConsider.sort(Comparator.comparingDouble(LivingEntity::getArmor));
            case "Hurt-ticks" -> entitiesToConsider.sort(Comparator.comparingInt(e -> e.hurtTime));
            case "Range" ->
                    entitiesToConsider.sort(Comparator.comparingDouble(entity -> mc.player.squaredDistanceTo((LivingEntity) entity))
                            .thenComparingDouble(entity -> ((LivingEntity) entity).getHealth())
                    );
        }

        LivingEntity localTarget = entitiesToConsider.stream().findFirst().orElse(null);
        if (localTarget != null && isWithinRange(localTarget, aimRange)) {
            target = localTarget;
        }

        return target;
    }
}