package fag.ware.client.tracker.impl;

import fag.ware.client.Fagware;
import fag.ware.client.event.data.Subscribe;
import fag.ware.client.event.impl.TickEvent;
import fag.ware.client.module.impl.combat.KillAuraModule;
import fag.ware.client.tracker.AbstractTracker;
import fag.ware.client.util.interfaces.IMinecraft;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;

import java.util.Comparator;
import java.util.List;

public class CombatTracker extends AbstractTracker<LivingEntity> implements IMinecraft {
    public float prevYaw, prevPitch;
    public float yaw, pitch;
    public LivingEntity target;

    private static final CombatTracker tracker = new CombatTracker();

    public static CombatTracker getInstance() {
        return tracker;
    }

    private KillAuraModule killAuraModule;

    public static void attackEntity(Entity entity) {
        if (entity != null && entity.isAlive()) {
            mc.interactionManager.attackEntity(MinecraftClient.getInstance().player, entity);
            mc.player.swingHand(Hand.MAIN_HAND);
        }
    }

    @Override
    public void initialize() {
        Fagware.BUS.register(this);
        killAuraModule = ModuleTracker.getInstance().getByClass(KillAuraModule.class);
    }

    @Subscribe
    public void onTick(TickEvent event) {
        if (mc.player == null || mc.world == null) return;

        if (!killAuraModule.isEnabled()) {
            target = null;
            getSet().clear();
            return;
        }

        if (target != null && (!target.isAlive() || !isWithinRange(target, killAuraModule.aimRange.toDouble()))) {
            target = null;
            getSet().clear();
        }

        if (target == null) {
            double range = killAuraModule.searchRange.toDouble();

            List<LivingEntity> entitiesToConsider = mc.world.getEntitiesByClass(
                    LivingEntity.class,
                    mc.player.getBoundingBox().expand(range),
                    entity -> !entity.equals(mc.player)
                            && shouldIncludeEntity(entity)
                            && entity.isAttackable()
                            && !entity.isDead()
                            && entity.isAlive()
                            && entity.getHealth() > 0
                            && !entity.getName().getString().isEmpty()
                            && !entity.getName().getString().contains(" ")
                            && !FriendTracker.getInstance().getSet().contains(entity.getName().getString())
                            && isWithinRange(entity, range)
            );

            switch (killAuraModule.sortBy.getValue()) {
                case "Health" -> entitiesToConsider.sort(Comparator.comparingDouble(LivingEntity::getHealth));
                case "Armor" -> entitiesToConsider.sort(Comparator.comparingDouble(LivingEntity::getArmor));
                case "Hurt-ticks" -> entitiesToConsider.sort(Comparator.comparingInt(e -> e.hurtTime));
                case "Range" ->
                        entitiesToConsider.sort(Comparator.comparingDouble(entity -> mc.player.squaredDistanceTo(entity)));
            }

            getSet().clear();
            getSet().addAll(entitiesToConsider);

            LivingEntity localTarget = getSet().stream().findFirst().orElse(null);
            if (localTarget != null && isWithinRange(localTarget, killAuraModule.aimRange.toDouble())) {
                target = localTarget;
            }
        }
    }

    public static boolean isWithinRange(Entity entity, double range) {
        double distanceSquared = mc.player.squaredDistanceTo(entity);

        double playerReachSquared = range * range;
        double entityRadius = entity.getWidth() / 2;

        return distanceSquared <= (playerReachSquared + entityRadius * entityRadius);
    }

    private boolean shouldIncludeEntity(LivingEntity livingEnt) {
        boolean players = killAuraModule.targets.enabled("Players");
        boolean animals = killAuraModule.targets.enabled("Animals");
        boolean monsters = killAuraModule.targets.enabled("Monsters");
        boolean invisibles = killAuraModule.targets.enabled("Invisibles");

        if (livingEnt instanceof PlayerEntity && players) return true;
        if (livingEnt instanceof AnimalEntity && animals) return true;
        if (livingEnt instanceof MobEntity && monsters) return true;
        if (livingEnt.isInvisible() && invisibles) return true;

        return false;
    }
}