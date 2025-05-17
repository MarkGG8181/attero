package fag.ware.client.module.impl.player;

import fag.ware.client.event.data.Subscribe;
import fag.ware.client.event.impl.TickEvent;
import fag.ware.client.module.AbstractModule;
import fag.ware.client.module.data.ModuleCategory;
import fag.ware.client.module.data.ModuleInfo;
import fag.ware.client.module.data.setting.impl.BooleanSetting;
import fag.ware.client.module.data.setting.impl.NumberSetting;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;

// Fuckass mappings, fuckass mc, fuckass EVERYTHING, https://tenor.com/gw1bwm4crmQ.gif
@SuppressWarnings("ALL")
@ModuleInfo(name = "AutoEat", category = ModuleCategory.PLAYER, description = "Automatically eats")
public class AutoEat extends AbstractModule {
    private final NumberSetting minHealth = new NumberSetting("Min HP", 5, 1, 20);
    private final NumberSetting minHunger = new NumberSetting("Min Hunger", 10, 1, 20);
    private final BooleanSetting gapple = new BooleanSetting("Golden Apples", false);
    private final BooleanSetting chorus = new BooleanSetting("Chorus Fruit", false);
    private final BooleanSetting rottenFlesh = new BooleanSetting("Rotten Flesh", false);
    private final BooleanSetting steak = new BooleanSetting("Steak", false);
    private final BooleanSetting spiderEye = new BooleanSetting("Spider Eye", false);
    private final BooleanSetting pufferfish = new BooleanSetting("Pufferfish", false);

    private int prevSlot = -1;
    private boolean isEating = false;

    //looks really similar to Meteor's AutoEat :thinking:

    @Subscribe
    public void onTick(TickEvent event) {
        if (mc.player == null || mc.world == null) return;

        boolean needsHealth = mc.player.getHealth() <= minHealth.getValue().floatValue();
        boolean needsFood = mc.player.getHungerManager().getFoodLevel() <= minHunger.getValue().floatValue();

        if (!needsHealth && !needsFood) {
            if (isEating) {
                stopEating();
            }
            return;
        }

        if (!isEating) {
            if (switchToFood()) {
                isEating = true;
            }
        }

        if (isEating) {
            mc.options.useKey.setPressed(true);
            if (!mc.player.isUsingItem()) {
                mc.interactionManager.interactItem(mc.player, Hand.MAIN_HAND);
            }
        }
    }

    private boolean switchToFood() {
        for (int i = 0; i < 9; i++) {
            ItemStack stack = mc.player.getInventory().getStack(i);
            if (stack.getComponents().contains(DataComponentTypes.FOOD)) {
                if (!gapple.getValue() && (stack.getItem() == Items.GOLDEN_APPLE || stack.getItem() == Items.ENCHANTED_GOLDEN_APPLE))
                    continue;
                if (!chorus.getValue() && (stack.getItem() == Items.CHORUS_FRUIT))
                    continue;
                if (!rottenFlesh.getValue() && (stack.getItem() == Items.ROTTEN_FLESH))
                    continue;
                if (!steak.getValue() && (stack.getItem() == Items.COOKED_BEEF))
                    continue;
                if (!spiderEye.getValue() && (stack.getItem() == Items.SPIDER_EYE))
                    continue;
                if (!pufferfish.getValue() && (stack.getItem() == Items.PUFFERFISH))
                    continue;
                prevSlot = mc.player.getInventory().getSelectedSlot();
                mc.player.getInventory().setSelectedSlot(i);
                return true;
            }
        }
        return false;
    }

    private void stopEating() {
        if (prevSlot != -1) {
            mc.player.getInventory().setSelectedSlot(prevSlot);
            prevSlot = -1;
        }
        mc.options.useKey.setPressed(false);
        isEating = false;
    }

    @Override
    public void onDisable() {
        if (isEating) {
            stopEating();
        }
    }
}