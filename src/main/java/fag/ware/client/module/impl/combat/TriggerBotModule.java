package fag.ware.client.module.impl.combat;

import fag.ware.client.event.data.Subscribe;
import fag.ware.client.event.impl.TickEvent;
import fag.ware.client.mixin.MinecraftAccessor;
import fag.ware.client.module.AbstractModule;
import fag.ware.client.module.data.ModuleCategory;
import fag.ware.client.module.data.ModuleInfo;
import fag.ware.client.module.data.setting.impl.RangeNumberSetting;
import fag.ware.client.util.math.Timer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
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
import java.util.Random;

@ModuleInfo(name = "TriggerBot", category = ModuleCategory.COMBAT, description = "Attacks enemies if you're looking at them")
public class TriggerBotModule extends AbstractModule {
    private final RangeNumberSetting swordMs = new RangeNumberSetting("Sword MS", 40, 1000, 40, 1000);
    private final RangeNumberSetting axeMS = new RangeNumberSetting("Axe MS", 40, 1000, 40, 1000);
    private final Timer timer = new Timer();
    private final Random random = new Random();

    @Subscribe
    public void onTick(TickEvent event) {
        if (mc.player == null || mc.world == null || mc.player.isSpectator() || mc.currentScreen != null || mc.player.isBlocking()) {
            return;
        }

        Entity targetedEntity = mc.targetedEntity;
        if (targetedEntity == null || !mc.player.canSee(targetedEntity)) {
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
        // ignore self and camera entity
        if (entity.equals(mc.player) || entity.equals(mc.cameraEntity)) return false;

        // check if entity is alive and not dead
        if (!(entity instanceof LivingEntity livingEntity) || livingEntity.isDead() || !entity.isAlive()) return false;

        // ignore owned pets (tameable entities)
        if (entity instanceof Tameable tameable && tameable.getOwner().getUuid() != null && tameable.getOwner().getUuid().equals(mc.player.getUuid())) {
            return false;
        }

        // ignore baby animals
        return !(entity instanceof AnimalEntity) || !((AnimalEntity) entity).isBaby();
    }

    private void hitEntity(Entity target) {
        MinecraftAccessor accessor = (MinecraftAccessor) mc;
        accessor.invokeDoAttack();
    }

    public boolean delay() {
        Item item = mc.player.getMainHandStack().getItem();
        try {
            if (item instanceof AxeItem) {
                return timer.hasElapsed(((long) SecureRandom.getInstanceStrong().nextFloat(axeMS.getMinAsFloat(), axeMS.getMaxAsFloat())), true);
            } else if (isSword(item)) {
                return timer.hasElapsed(((long) SecureRandom.getInstanceStrong().nextFloat(swordMs.getMinAsFloat(), swordMs.getMaxAsFloat())), true);
            } else {
                return timer.hasElapsed((long) swordMs.getMaxAsFloat(), true);
            }

        } catch (NoSuchAlgorithmException e) {
            return false;
        }
    }

    private boolean isSword(Item item) {
        return item == Items.WOODEN_SWORD || item == Items.STONE_SWORD || item == Items.IRON_SWORD ||
                item == Items.DIAMOND_SWORD || item == Items.NETHERITE_SWORD;
    }

    @Override
    public void onEnable() {
        timer.reset();
    }

    @Override
    public void onDisable() {

    }
}
