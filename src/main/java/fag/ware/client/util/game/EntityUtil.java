package fag.ware.client.util.game;

import fag.ware.client.util.SystemUtil;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.Registries;
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
}