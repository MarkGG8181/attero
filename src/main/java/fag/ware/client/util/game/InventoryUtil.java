package fag.ware.client.util.game;

import fag.ware.client.util.interfaces.IMinecraft;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class InventoryUtil implements IMinecraft {
    public static void switchToNextSlot() {
        int currentSlot = mc.player.getInventory().getSelectedSlot();
        int bestSlot = currentSlot;
        int bestCount = getBlockCount(currentSlot);

        for (int i = 0; i < 9; i++) {
            int slotBlockCount = getBlockCount(i);
            if (slotBlockCount > bestCount) {
                bestCount = slotBlockCount;
                bestSlot = i;
            }
        }

        if (bestSlot != currentSlot) {
            mc.player.getInventory().setSelectedSlot(bestSlot);
        }
    }

    public static void switchToSlot(Item item) {
        for (int i = 0; i < 9; i++) {
            ItemStack stack = mc.player.getInventory().getStack(i);
            if (stack.isEmpty()) continue;
            if (stack.getItem().equals(item)) {
                mc.player.getInventory().setSelectedSlot(i);
                return;
            }
        }
    }

    public static int getBlockCount(int slot) {
        ItemStack itemStack = mc.player.getInventory().getStack(slot);
        if (itemStack != null && itemStack.getItem() instanceof BlockItem) {
            return itemStack.getCount();
        }
        return 0;
    }
}