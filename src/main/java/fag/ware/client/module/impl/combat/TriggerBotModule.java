package fag.ware.client.module.impl.combat;

import fag.ware.client.event.data.Subscribe;
import fag.ware.client.event.impl.TickEvent;
import fag.ware.client.mixin.MinecraftClientAccessor;
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

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Optional;

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

        Entity target = mc.targetedEntity;
        if (target == null || !mc.player.canSee(target)) return;

        Item item = mc.player.getMainHandStack().getItem();
        if (item.getComponents().contains(DataComponentTypes.FOOD) && mc.options.rightKey.isPressed()) return;

        if (!delay()) return;

        if (isValidTarget(target))
            attack(target);
    }

    private boolean delay() {
        Item item = mc.player.getMainHandStack().getItem();
        float min, max;

        if (item instanceof AxeItem) {
            min = axeMS.getMinAsFloat();
            max = axeMS.getMaxAsFloat();
        } else {
            min = swordMs.getMinAsFloat();
            max = swordMs.getMaxAsFloat();
        }

        try {
            if (swordMs.getAbsoluteMax() == swordMs.getAbsoluteMin()) {
                return timer.hasElapsed(swordMs.getAbsoluteMax().longValue());
            }
            if (axeMS.getAbsoluteMax() == axeMS.getAbsoluteMin()) {
                return timer.hasElapsed(axeMS.getAbsoluteMax().longValue());
            }
            float ms = SecureRandom.getInstanceStrong().nextFloat(min, max);
            return timer.hasElapsed((long) ms, true);
        } catch (NoSuchAlgorithmException e) {
            return false;
        }
    }

    private boolean isValidTarget(Entity e) {
        if (e == mc.player || e == mc.cameraEntity || !e.isAlive()) return false;
        if (!(e instanceof LivingEntity l) || l.isDead()) return false;
        if (e instanceof Tameable t && mc.player.getUuid().equals(Optional.ofNullable(t.getOwner()).map(Entity::getUuid).orElse(null)))
            return false;

        return !(e instanceof AnimalEntity a && a.isBaby());
    }

    private void attack(Entity target) {
        MinecraftClientAccessor accessor = (MinecraftClientAccessor) mc;
        accessor.invokeDoAttack();
    }

    @Override
    public void onEnable() {
        timer.reset();
    }
}