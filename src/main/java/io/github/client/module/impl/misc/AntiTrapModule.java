package io.github.client.module.impl.misc;

import io.github.client.event.data.Subscribe;
import io.github.client.event.impl.game.TickEvent;
import io.github.client.module.AbstractModule;
import io.github.client.module.data.ModuleCategory;
import io.github.client.module.data.ModuleInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;

@ModuleInfo(
        name = "AntiTrap",
        description = "Removes entities to prevent traps",
        category = ModuleCategory.MISC
)
public class AntiTrapModule extends AbstractModule {
    @Subscribe
    private void onTick(TickEvent event) {
        if (mc.player == null || mc.world == null) return;

        for (var entities : mc.world.getEntities()) {
            if (entityCheck(entities.getType())) {
                entities.remove(Entity.RemovalReason.DISCARDED);
            }
        }
    }

    private boolean entityCheck(EntityType<?> type) {
        return type == EntityType.ARMOR_STAND;
    }
}
