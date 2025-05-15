package fag.ware.client.module.impl.combat;

import fag.ware.client.event.data.Subscribe;
import fag.ware.client.event.impl.TickEvent;
import fag.ware.client.module.AbstractModule;
import fag.ware.client.module.data.ModuleCategory;
import fag.ware.client.module.data.ModuleInfo;
import fag.ware.client.module.data.setting.impl.RangeNumberSetting;
import fag.ware.client.util.math.Timer;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Tameable;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/// Horrid Code - graph
@SuppressWarnings("ALL")
@ModuleInfo(name = "TriggerBot", category = ModuleCategory.COMBAT, description = "Attacks enemies if you're looking at them")
public class TriggerBotModule extends AbstractModule {
    private final RangeNumberSetting swordMs = new RangeNumberSetting("Sword MS", 40, 1000, 40, 1000);
    private final RangeNumberSetting axeMS = new RangeNumberSetting("Axe MS", 40, 1000, 40, 1000);
    private final Timer timer = new Timer();

    @Subscribe
    public void onTick(TickEvent event) {
        if (mc.player == null || mc.world == null || mc.player.isSpectator() || mc.currentScreen != null || mc.player.isBlocking())
            return;

        Entity targetedEntity = mc.targetedEntity;
        if (targetedEntity == null && !mc.player.canSee(targetedEntity)) {
            return;
        }

        Item item = mc.player.getMainHandStack().getItem();
        if (item.getComponents().contains(DataComponentTypes.FOOD) && mc.options.rightKey.isPressed()) {
            return;
        }

        if (!delay()) {
            return;
        }

        if (entityCheck(targetedEntity)) {
            hitEntity(targetedEntity);
        }
    }

    private boolean entityCheck(Entity entity) {
        // ignore self
        if (entity.equals(mc.player) || entity.equals(mc.cameraEntity)) return false;
        // check
        if (!(entity instanceof LivingEntity livingEntity) || livingEntity.isDead() || !entity.isAlive()) return false;
        // doggies horsies and etc
        if (entity instanceof Tameable tameable && tameable.getOwner().getUuid() != null && tameable.getOwner().getUuid().equals(mc.player.getUuid()))
            return false;

        return !(entity instanceof AnimalEntity) || !((AnimalEntity) entity).isBaby();
    }

    private void hitEntity(Entity target) {
        mc.interactionManager.attackEntity(mc.player, target);
        mc.player.swingHand(Hand.MAIN_HAND);
    }

    public boolean delay() {
        Item item = mc.player.getMainHandStack().getItem();
        try {
            if (item instanceof AxeItem) {
                return timer.hasElapsed(((long) SecureRandom.getInstanceStrong().nextFloat(axeMS.getMinAsFloat(), axeMS.getMaxAsFloat())), true);
            } else if (mc.player.isHolding(Items.WOODEN_SWORD) || mc.player.isHolding(Items.STONE_SWORD) || mc.player.isHolding(Items.IRON_SWORD) || mc.player.isHolding(Items.DIAMOND_SWORD) || mc.player.isHolding(Items.NETHERITE_SWORD)) {
                return timer.hasElapsed(((long) SecureRandom.getInstanceStrong().nextFloat(swordMs.getMinAsFloat(), swordMs.getMaxAsFloat())), true);
            } else {
                return timer.hasElapsed(((long) SecureRandom.getInstanceStrong().nextFloat(swordMs.getMinAsFloat(), swordMs.getMaxAsFloat())), true);
            }

        } catch (NoSuchAlgorithmException e) {
            return false;
        }
    }

    @Override
    public void onEnable() {
        timer.reset();
    }

    @Override
    public void onDisable() {
        // No need to reset timer on disable, unless you want to
    }
}