package io.github.client.module.impl.combat;

import io.github.client.event.data.Subscribe;
import io.github.client.event.impl.game.TickEvent;
import io.github.client.module.AbstractModule;
import io.github.client.module.data.ModuleCategory;
import io.github.client.module.data.ModuleInfo;
import io.github.client.module.data.setting.impl.RangeNumberSetting;
import io.github.client.util.java.math.Timer;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Tameable;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.item.AxeItem;
import org.lwjgl.glfw.GLFW;

import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@ModuleInfo(name = "TriggerBot", category = ModuleCategory.COMBAT, description = "Attacks enemies if you're looking at them")
public class TriggerBotModule extends AbstractModule {
    private final RangeNumberSetting swordDelay = new RangeNumberSetting("Sword delay", 40, 1000, 40, 1000);
    private final RangeNumberSetting axeDelay = new RangeNumberSetting("Axe delay", 40, 1000, 40, 1000);
    private final Timer timer = new Timer();

    @Subscribe
    private void onTick(TickEvent ignored) {
        if (mc.player == null || mc.world == null) return;
        if (mc.player.isSpectator()) return;
        if (mc.currentScreen != null) return;
        if (mc.player.isBlocking()) return;

        Entity target = mc.targetedEntity;
        if (target == null || !mc.player.canSee(target)) return;
        if (!isValidTarget(target)) return;

        var mainHandItem = mc.player.getMainHandStack().getItem();

        if (mainHandItem.getComponents().contains(DataComponentTypes.FOOD) &&
                GLFW.glfwGetMouseButton(mc.getWindow().getHandle(), GLFW.GLFW_MOUSE_BUTTON_RIGHT) != GLFW.GLFW_PRESS) {
            return;
        }

        long delayMs = getDelayForItem(mainHandItem);
        if (!timer.hasElapsed(delayMs, true)) return;

        mc.doAttack();
    }

    private long getDelayForItem(Object item) {
        float min, max;
        if (item instanceof AxeItem) {
            min = axeDelay.getMinAsFloat();
            max = axeDelay.getMaxAsFloat();
        } else {
            min = swordDelay.getMinAsFloat();
            max = swordDelay.getMaxAsFloat();
        }
        if (min == max) return (long) min;
        return (long) (ThreadLocalRandom.current().nextFloat() * (max - min) + min);
    }

    private boolean isValidTarget(Entity entity) {
        if (entity == mc.player || entity == mc.cameraEntity) return false;
        if (!(entity instanceof LivingEntity living) || !living.isAlive() || living.isDead()) return false;

        if (entity instanceof Tameable tameable) {
            var owner = Optional.ofNullable(tameable.getOwner()).map(Entity::getUuid).orElse(null);
            if (mc.player.getUuid().equals(owner)) return false;
        }

        if (entity instanceof AnimalEntity animal && animal.isBaby()) return false;

        return true;
    }

    public void onEnable() {
        timer.reset();
    }

    @Override
    public String getSuffix() {
        return (swordDelay.absoluteMin.intValue() + swordDelay.absoluteMax.intValue()) / 2 + "ms";
    }
}