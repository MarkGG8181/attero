package fag.ware.client.module.impl.misc;

import fag.ware.client.event.data.Subscribe;
import fag.ware.client.event.impl.MiddleClickEvent;
import fag.ware.client.module.AbstractModule;
import fag.ware.client.module.data.ModuleCategory;
import fag.ware.client.module.data.ModuleInfo;
import fag.ware.client.tracker.impl.FriendTracker;
import fag.ware.client.tracker.impl.ModuleTracker;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;

@ModuleInfo(name = "MidClickFriend", description = "Middle click to add/remove a friend", category = ModuleCategory.MISC)
public class MidClickFriendModule extends AbstractModule {
    @Override
    public void onEnable() {
        ModuleTracker.getInstance().getByName("MidClickPearl").setEnabled(false);
    }

    @Subscribe
    public void onMidClick(MiddleClickEvent event) {
        if (mc.crosshairTarget instanceof EntityHitResult entityHitResult) {
            if (entityHitResult.getType().equals(HitResult.Type.ENTITY) && entityHitResult.getEntity() instanceof PlayerEntity plr) {
                String name = plr.getName().getString();

                if (FriendTracker.getInstance().getSet().contains(name)) {
                    FriendTracker.getInstance().getSet().remove(name);
                    send(String.format("Removed %s from friends", name));
                } else {
                    FriendTracker.getInstance().getSet().add(name);
                    send(String.format("Added %s to friends", name));
                }
            }
        }
    }
}