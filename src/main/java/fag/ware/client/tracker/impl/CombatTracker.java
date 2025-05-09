package fag.ware.client.tracker.impl;

import fag.ware.client.Fagware;
import fag.ware.client.event.data.Subscribe;
import fag.ware.client.event.impl.TickEvent;
import fag.ware.client.module.impl.combat.KillAuraModule;
import fag.ware.client.tracker.Tracker;
import fag.ware.client.util.IMinecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class CombatTracker extends Tracker<LivingEntity> implements IMinecraft {
    private KillAuraModule killAuraModule;
    public float yaw, pitch;
    public float bodyYaw, prevBodyYaw;
    public float prevYaw, prevPitch;
    public LivingEntity target;

    @Override
    public void initialize() {
        Fagware.BUS.register(this);
        killAuraModule = Fagware.INSTANCE.moduleTracker.getByClass(KillAuraModule.class);
    }

    @Subscribe
    public void onTick(TickEvent event) {
        if (mc.player == null || mc.world == null) return;

        if (!killAuraModule.isEnabled()) {
            target = null;
            getSet().clear();
            return;
        }

        List<Entity> entitiesToConsider = new ArrayList<>();

        for (Entity entity : mc.world.getEntities()) {
            if (entity.equals(mc.player)) continue;

            if (entity instanceof LivingEntity livingEnt && livingEnt.isAlive() && livingEnt.isAttackable()) {
                if (!getSet().contains(livingEnt) && shouldIncludeEntity(livingEnt)) {
                    if (isWithinRange(entity, killAuraModule.searchRange.toDouble())) {
                        entitiesToConsider.add(entity);
                    }
                }
            }
        }

        switch (killAuraModule.sortBy.toString()) {
            case "Health" ->
                    entitiesToConsider.sort(Comparator.comparingDouble(entity -> ((LivingEntity) entity).getHealth()));
            case "Armor" ->
                    entitiesToConsider.sort(Comparator.comparingDouble(entity -> ((LivingEntity) entity).getArmor()));
            case "Hurt-ticks" ->
                    entitiesToConsider.sort(Comparator.comparingInt(entity -> ((LivingEntity) entity).hurtTime));
            case "Range" ->
                    entitiesToConsider.sort(Comparator.comparingDouble(entity -> mc.player.squaredDistanceTo(entity)));
        }

        for (Entity entity : entitiesToConsider) {
            getSet().add((LivingEntity) entity);
        }

        LivingEntity localTarget = getSet().stream().findFirst().orElse(null);
        if (localTarget != null) {
            if (isWithinRange(localTarget, killAuraModule.aimRange.toDouble())) {
                target = localTarget;
            } else {
                target = null;
            }
        }
    }

    private boolean isWithinRange(Entity entity, double range) {
        double distanceSquared = mc.player.squaredDistanceTo(entity);

        double playerReachSquared = range * range;
        double entityRadius = entity.getWidth() / 2;

        return distanceSquared <= (playerReachSquared + entityRadius * entityRadius);
    }

    private boolean shouldIncludeEntity(LivingEntity livingEnt) {
        boolean players = killAuraModule.players.getValue();
        boolean animals = killAuraModule.animals.getValue();
        boolean monsters = killAuraModule.monsters.getValue();
        boolean invisibles = killAuraModule.invisibles.getValue();

        if (livingEnt instanceof PlayerEntity && players) return true;
        if (livingEnt instanceof AnimalEntity && animals) return true;
        if (livingEnt instanceof MobEntity && monsters) return true;
        if (livingEnt.isInvisible() && invisibles) return true;

        return false;
    }
}