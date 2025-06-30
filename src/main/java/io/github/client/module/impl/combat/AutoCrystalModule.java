package io.github.client.module.impl.combat;

import io.github.client.event.data.Subscribe;
import io.github.client.event.impl.game.TickEvent;
import io.github.client.module.AbstractModule;
import io.github.client.module.data.ModuleCategory;
import io.github.client.module.data.ModuleInfo;
import io.github.client.module.data.setting.impl.NumberSetting;
import io.github.client.util.java.math.Timer;
import net.minecraft.block.Blocks;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import org.lwjgl.glfw.GLFW;

/**
 * @author Graph
 */
// TODO: MAKE THIS BETTER QUALITY, ADD MORE SETTINGS.
@SuppressWarnings("ALL")
@ModuleInfo(name = "AutoCrystal", description = "Places and breaks crystals hold RMB", category = ModuleCategory.COMBAT)
public class AutoCrystalModule extends AbstractModule {
    private final NumberSetting breakDelay = new NumberSetting("Break delay", 250, 0, 750);
    private final NumberSetting placeDelay = new NumberSetting("Place delay", 250, 0, 750);
    private final Timer breakDelayTimer = new Timer();
    private final Timer placeDelayTimer = new Timer();

    @Subscribe
    public void onTick(TickEvent ignoredEvent) {
        if (mc.player == null || mc.world == null || mc.player.isUsingItem()) return;

        if (GLFW.glfwGetMouseButton(mc.getWindow().getHandle(), GLFW.GLFW_MOUSE_BUTTON_RIGHT) != GLFW.GLFW_PRESS) {
            return;
        }

        if (mc.crosshairTarget instanceof BlockHitResult hit) {
            var pos = hit.getBlockPos();
            if (mc.world.getBlockState(pos).isOf(Blocks.OBSIDIAN) || mc.world.getBlockState(pos).isOf(Blocks.BEDROCK)) {
                if (mc.player.getInventory().getSelectedStack().getItem() == Items.END_CRYSTAL && placeDelayTimer.hasElapsed(placeDelay.toInt(), true)) {
                    mc.interactionManager.interactBlock(mc.player, Hand.MAIN_HAND, hit);
                    mc.player.swingHand(Hand.MAIN_HAND);
                    breakDelayTimer.reset();
                }
            }
        }

        if (breakDelayTimer.hasElapsed(breakDelay.toInt(), true)) {
            if (mc.crosshairTarget instanceof EntityHitResult entityHitResult &&
                    entityHitResult.getEntity() instanceof EndCrystalEntity crystal) {
                mc.interactionManager.attackEntity(mc.player, crystal);
                mc.player.swingHand(Hand.MAIN_HAND);
                placeDelayTimer.reset();
            }
        }
    }
}