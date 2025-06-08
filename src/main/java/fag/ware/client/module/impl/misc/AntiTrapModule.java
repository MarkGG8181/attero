package fag.ware.client.module.impl.misc;

import fag.ware.client.event.data.Subscribe;
import fag.ware.client.event.impl.game.TickEvent;
import fag.ware.client.module.AbstractModule;
import fag.ware.client.module.data.ModuleCategory;
import fag.ware.client.module.data.ModuleInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;

@ModuleInfo(
        name = "AntiTrap",
        description = "Removes entities to prevent traps",
        category = ModuleCategory.MISC
)
public class AntiTrapModule extends AbstractModule {

    @Subscribe
    public void onTick(TickEvent event) {
        if (mc.player == null || mc.world == null) return;
        for (Entity entities : mc.world.getEntities()) {
            if (entityCheck(entities.getType()))
                entities.remove(Entity.RemovalReason.DISCARDED);
            if (mc.player.age % 60 == 0) {
                send("Removed");
            }
        }
    }

    public boolean entityCheck(EntityType type) {
        return type == EntityType.ARMOR_STAND;
    }
}
