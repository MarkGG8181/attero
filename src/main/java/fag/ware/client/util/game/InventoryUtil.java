package fag.ware.client.util.game;

import fag.ware.client.util.interfaces.IMinecraft;
import net.minecraft.block.Block;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.CactusBlock;
import net.minecraft.block.FallingBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class InventoryUtil implements IMinecraft {
    public static boolean checkHotbarForBlocks() {
        boolean blocks = false;
        for (int i = 0; i < 9; i++) {
            ItemStack itemStack = mc.player.getInventory().getStack(i);

            if (itemStack != null && itemStack.getItem() instanceof BlockItem block) {
                if (isGoodBlock(block)) {
                    blocks = true;
                    break;
                }
            }
        }

        return blocks;
    }

    public static boolean isGoodBlock(BlockItem item) {
        if (item == null) {
            return false;
        }

        final Block block = item.getBlock();

        if (block == null) {
            return false;
        }

        if (block.getSlipperiness() > 0.6f) return false;
        if (block.getVelocityMultiplier() < 1.0f) return false;
        if (block.getJumpVelocityMultiplier() < 1.0f) return false;

        if (block instanceof CactusBlock) return false;
        if (block instanceof BlockWithEntity || block instanceof FallingBlock) return false;

        return true;
    }

    public static void switchToBestSlotWithBlocks() {
        final int currentSlot = mc.player.getInventory().getSelectedSlot();

        int bestSlot = currentSlot;
        int bestCount = getBlockCount(currentSlot);

        for (int slot = 0; slot < 9; slot++) {
            final ItemStack stack = mc.player.getInventory().getStack(slot);

            if (stack.getItem() instanceof final BlockItem item) {
                if (isGoodBlock(item)) {
                    final int slotBlockCount = getBlockCount(slot);
                    if (slotBlockCount > bestCount) {
                        bestCount = slotBlockCount;
                        bestSlot = slot;
                    }
                }
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