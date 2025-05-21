package fag.ware.client.util.game;

import fag.ware.client.util.interfaces.IMinecraft;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class InventoryUtil implements IMinecraft {
    //TODO: Recode this horrendous broken util
    public static void switchToNextSlot() {
        int currentSlot = mc.player.getInventory().getSelectedSlot();
        int nextSlot = -1;
        int currentBlockCount = getBlockCount(currentSlot);

        for (int i = 0; i < 9; i++) {
            if (i == currentSlot) continue;

            int slotBlockCount = getBlockCount(i);
            if ((slotBlockCount > currentSlot) || (currentBlockCount == 0 && slotBlockCount > 0)) {
                nextSlot = i;
                break;
            }
        }

        if (nextSlot != -1) {
            mc.player.getInventory().setSelectedSlot(nextSlot);
        }
    }

    public static void switchToSlot(Item item) {
        for (int i = 0; i < 9; i++) {
            ItemStack stack = mc.player.getInventory().getStack(i);
            if (stack.isEmpty()) return;
            if (stack.getItem().equals(item)) {
                mc.player.getInventory().setSelectedSlot(i);
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