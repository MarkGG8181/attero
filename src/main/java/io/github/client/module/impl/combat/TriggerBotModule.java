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

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Optional;

// Horrid Code - graph
@SuppressWarnings("ALL")
@ModuleInfo(name = "TriggerBot", category = ModuleCategory.COMBAT, description = "Attacks enemies if you're looking at them")
public class TriggerBotModule extends AbstractModule {
    private final RangeNumberSetting swordMs = new RangeNumberSetting("Sword delay", 40, 1000, 40, 1000);
    private final RangeNumberSetting axeMS = new RangeNumberSetting("Axe delay", 40, 1000, 40, 1000);
    private final Timer timer = new Timer();
    private float ms;

    @Subscribe
    private void onTick(TickEvent ignoredEvent) {
        if (mc.player == null || mc.world == null || mc.player.isSpectator() || mc.currentScreen != null || mc.player.isBlocking())
            return;

        var target = mc.targetedEntity;
        if (target == null || !mc.player.canSee(target)) return;

        var item = mc.player.getMainHandStack().getItem();
        if (item.getComponents().contains(DataComponentTypes.FOOD) && GLFW.glfwGetMouseButton(mc.getWindow().getHandle(), GLFW.GLFW_MOUSE_BUTTON_RIGHT) != GLFW.GLFW_PRESS) return;

        if (!delay()) return;

        if (isValidTarget(target)) {
            mc.doAttack();
        }
    }

    private boolean delay() {
        var item = mc.player.getMainHandStack().getItem();
        var speed = new float[2];

        if (item instanceof AxeItem) {
            speed[0] = axeMS.getMinAsFloat();
            speed[1] = axeMS.getMaxAsFloat();
        } else {
            speed[0] = swordMs.getMinAsFloat();
            speed[1] = swordMs.getMaxAsFloat();
        }

        try {
            if (swordMs.absoluteMax == swordMs.absoluteMin) {
                return timer.hasElapsed(swordMs.absoluteMax.longValue());
            }

            if (axeMS.absoluteMax == axeMS.absoluteMin) {
                return timer.hasElapsed(axeMS.absoluteMax.longValue());
            }

            ms = SecureRandom.getInstanceStrong().nextFloat(speed[0], speed[1]);
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

    public void onEnable() {
        timer.reset();
    }

    @Override
    public String getSuffix() {
        return ((int) ms) + "ms";
    }
}