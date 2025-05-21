package fag.ware.client.module.impl.combat;

import fag.ware.client.event.data.Subscribe;
import fag.ware.client.event.impl.TickEvent;
import fag.ware.client.module.AbstractModule;
import fag.ware.client.module.data.ModuleCategory;
import fag.ware.client.module.data.ModuleInfo;
import fag.ware.client.module.data.setting.impl.NumberSetting;
import fag.ware.client.util.math.Timer;
import net.minecraft.block.Blocks;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.glfw.GLFW;

// TODO: MAKE THIS LESS LOW QUALITY, ADD SETTINGS.
// Author : Graph
@ModuleInfo(name = "AutoCrystal", description = "Places and breaks crystals hold RMB", category = ModuleCategory.COMBAT)
public class AutoCrystalModule extends AbstractModule {
    private final NumberSetting breakDelay = new NumberSetting("Break delay", 250, 0, 750);
    private final NumberSetting placeDelay = new NumberSetting("Place delay", 250, 0, 750);
    private final Timer breakDelayTimer = new Timer();
    private final Timer placeDelayTimer = new Timer();

    @Subscribe
    public void onTick(TickEvent event) {
        if (mc.player == null || mc.world == null || mc.player.isUsingItem()) return;

        if (GLFW.glfwGetMouseButton(mc.getWindow().getHandle(), GLFW.GLFW_MOUSE_BUTTON_RIGHT) != GLFW.GLFW_PRESS)
            return;

        if (mc.crosshairTarget instanceof BlockHitResult hit) {
            BlockPos pos = hit.getBlockPos();
            if (mc.world.getBlockState(pos).isOf(Blocks.OBSIDIAN) || mc.world.getBlockState(pos).isOf(Blocks.BEDROCK)) {
                if (mc.player.getInventory().getSelectedStack().getItem() == Items.END_CRYSTAL && placeDelayTimer.hasElapsed(placeDelay.toInt(), true)) {
                    mc.interactionManager.interactBlock(mc.player, Hand.MAIN_HAND, hit);
                    mc.player.swingHand(Hand.MAIN_HAND);

                }
            }
        }

        if (breakDelayTimer.hasElapsed(breakDelay.toInt(), true)) {
            if (mc.crosshairTarget instanceof EntityHitResult entityHitResult &&
                    entityHitResult.getEntity() instanceof EndCrystalEntity crystal) {
                mc.interactionManager.attackEntity(mc.player, crystal);
                mc.player.swingHand(Hand.MAIN_HAND);
            }
        }
    }
}