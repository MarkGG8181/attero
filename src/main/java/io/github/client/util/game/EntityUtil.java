package io.github.client.util.game;

import io.github.client.util.java.SystemUtil;
import io.github.client.util.java.interfaces.IMinecraft;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.Registries;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class EntityUtil {
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
        double distanceSquared = IMinecraft.mc.player.squaredDistanceTo(entity);

        double playerReachSquared = range * range;
        double entityRadius = entity.getWidth() / 2;

        return distanceSquared <= (playerReachSquared + entityRadius * entityRadius);
    }

    public static void attackEntity(Entity entity) {
        if (entity != null && entity.isAlive()) {
            IMinecraft.mc.interactionManager.attackEntity(MinecraftClient.getInstance().player, entity);
            IMinecraft.mc.player.swingHand(Hand.MAIN_HAND);
        }
    }
}