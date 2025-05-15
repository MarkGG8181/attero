package fag.ware.client.util.player;

import net.minecraft.entity.EntityType;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class WorldUtil {
    public static String[] getAllEntityNames() {
        List<String> names = new ArrayList<>();

        for (EntityType<?> type : Registries.ENTITY_TYPE) {
            Identifier id = Registries.ENTITY_TYPE.getId(type);
            names.add(toClassName(id.getPath()));
        }

        return names.toArray(new String[0]);
    }

    public static String[] getExampleEntities() {
        return new String[]{
                "ArmorStand",
                "ExperienceOrb"
        };
    }

    public static String toClassName(String name) {
        String[] parts = name.split("_");
        StringBuilder sb = new StringBuilder();
        for (String part : parts) {
            sb.append(Character.toUpperCase(part.charAt(0)))
                    .append(part.substring(1));
        }
        return sb.toString();
    }
}
