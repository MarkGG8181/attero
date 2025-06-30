package io.github.client.module.impl.misc;

import io.github.client.event.data.Subscribe;
import io.github.client.event.impl.interact.MiddleClickEvent;
import io.github.client.module.AbstractModule;
import io.github.client.module.data.ModuleCategory;
import io.github.client.module.data.ModuleInfo;
import io.github.client.tracker.impl.FriendTracker;
import io.github.client.tracker.impl.ModuleTracker;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;

@ModuleInfo(name = "MidClickFriend", description = "Middle click to add/remove a friend", category = ModuleCategory.MISC)
public class MidClickFriendModule extends AbstractModule {
    @Subscribe
    private void onMidClick(MiddleClickEvent event) {
        if (mc.crosshairTarget instanceof EntityHitResult entityHitResult) {
            if (entityHitResult.getType().equals(HitResult.Type.ENTITY) && entityHitResult.getEntity() instanceof PlayerEntity plr) {
                var name = plr.getName().getString();

                if (FriendTracker.getInstance().list.contains(name)) {
                    FriendTracker.getInstance().list.remove(name);
                    send(String.format("Removed §e%s§r from friends", name));
                } else {
                    FriendTracker.getInstance().list.add(name);
                    send(String.format("Added §e%s§r to friends", name));
                }
            }
        }
    }

    public void onEnable() {
        ModuleTracker.INSTANCE.getByName("MidClickPearl").setEnabled(false);
    }
}