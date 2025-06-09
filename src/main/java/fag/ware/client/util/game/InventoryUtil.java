package fag.ware.client.util.game;

import fag.ware.client.util.SystemUtil;
import fag.ware.client.util.interfaces.IMinecraft;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.CactusBlock;
import net.minecraft.block.FallingBlock;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.ItemTags;

import java.util.ArrayList;
import java.util.List;

public class InventoryUtil implements IMinecraft {
    public static String[] getAllItems() {
        List<String> names = new ArrayList<>();

        for (var item : Registries.ITEM) {
            var id = Registries.ITEM.getId(item);
            names.add(SystemUtil.toClassName(id.getPath()));
        }

        return names.toArray(new String[0]);
    }

    public static String[] getExampleItems() {
        return new String[]{
                "RottenFlesh",
                "SpiderEye",
                "Stick",
                "FermentedSpiderEye",
                "Flint",
                "Egg",
                "TurtleEgg",
                "SnifferEgg",
                "Scaffolding",
                "TubeCoral",
                "BrainCoral",
                "BubbleCoral",
                "FireCoral",
                "HornCoral",
                "Conduit",
                "Saddle",
                "Bowl",
                "Cactus",
                "RawCopper",
                "String",
                "Feather",
                "Gunpowder",
                "WheatSeeds",
                "Wheat",
                "Leather"
        };
    }

    public static boolean checkHotbarForBlocks() {
        var blocks = false;
        for (var i = 0; i < 9; i++) {
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

        final var block = item.getBlock();

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
        var currentSlot = mc.player.getInventory().getSelectedSlot();

        var bestSlot = currentSlot;
        var bestCount = getBlockCount(currentSlot);

        for (var slot = 0; slot < 9; slot++) {
            var stack = mc.player.getInventory().getStack(slot);

            if (stack.getItem() instanceof BlockItem item) {
                if (isGoodBlock(item)) {
                    var slotBlockCount = getBlockCount(slot);

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
        for (var i = 0; i < 9; i++) {
            var stack = mc.player.getInventory().getStack(i);

            if (stack.isEmpty()) continue;
            if (stack.getItem().equals(item)) {
                mc.player.getInventory().setSelectedSlot(i);
                return;
            }
        }
    }

    public static int getBlockCount(int slot) {
        var itemStack = mc.player.getInventory().getStack(slot);
        if (itemStack != null && itemStack.getItem() instanceof BlockItem) {
            return itemStack.getCount();
        }
        return 0;
    }

    public static void getEnchantments(ItemStack itemStack, Object2IntMap<RegistryEntry<Enchantment>> enchantments) {
        enchantments.clear();

        if (!itemStack.isEmpty()) {
            var itemEnchantments = itemStack.getItem() == Items.ENCHANTED_BOOK
                    ? itemStack.getOrDefault(DataComponentTypes.STORED_ENCHANTMENTS, ItemEnchantmentsComponent.DEFAULT).getEnchantmentEntries()
                    : itemStack.getEnchantments().getEnchantmentEntries();

            for (var entry : itemEnchantments) {
                enchantments.put(entry.getKey(), entry.getIntValue());
            }
        }
    }

    public static boolean isArmor(ItemStack itemStack) {
        return itemStack.isIn(ItemTags.FOOT_ARMOR) || itemStack.isIn(ItemTags.LEG_ARMOR) || itemStack.isIn(ItemTags.CHEST_ARMOR) || itemStack.isIn(ItemTags.HEAD_ARMOR);
    }

    public static ArmorType getArmorType(ItemStack stack) {
        if (stack.isIn(ItemTags.HEAD_ARMOR)) {
            return ArmorType.HEAD;
        } else if (stack.isIn(ItemTags.CHEST_ARMOR)) {
            return ArmorType.CHEST;
        } else if (stack.isIn(ItemTags.LEG_ARMOR)) {
            return ArmorType.LEGS;
        } else if (stack.isIn(ItemTags.FOOT_ARMOR)) {
            return ArmorType.FOOT;
        }

        return null;
    }

    public static boolean isBetterArmor(ItemStack a, ItemStack b) {
        if (a.getMaxDamage() > 0 && b.getMaxDamage() > 0) {
            float aDurability = (float) (a.getMaxDamage() - a.getDamage()) / a.getMaxDamage();
            float bDurability = (float) (b.getMaxDamage() - b.getDamage()) / b.getMaxDamage();
            if (Math.abs(aDurability - bDurability) > 0.2f) {
                return aDurability > bDurability;
            }
        }

        var aProt = getProtection(a);
        var bProt = getProtection(b);

        if (aProt != bProt) return aProt > bProt;

        var aArmor = getArmorAttribute(a);
        var bArmor = getArmorAttribute(b);

        return aArmor > bArmor;
    }

    public static int getProtection(ItemStack stack) {
        Object2IntMap<RegistryEntry<Enchantment>> enchantments = new Object2IntOpenHashMap<>();
        getEnchantments(stack, enchantments);

        for (var entry : enchantments.object2IntEntrySet()) {
            if (entry.getKey().matchesKey(Enchantments.PROTECTION)) {
                return entry.getIntValue();
            }
        }
        return 0;
    }

    public static int getArmorAttribute(ItemStack stack) {
        var comp = stack.get(DataComponentTypes.ATTRIBUTE_MODIFIERS);
        if (comp == null) return 0;

        return comp.modifiers().stream()
                .filter(e -> e.attribute() == EntityAttributes.ARMOR)
                .mapToInt(e -> (int) e.modifier().value())
                .sum();
    }

    public enum ArmorType {
        HEAD(5),
        CHEST(6),
        LEGS(7),
        FOOT(8);

        public final int slot;

        ArmorType(int slot) {
            this.slot = slot;
        }
    }

    public static class ArmorItem {
        public ItemStack stack;
        public ArmorType type;
        public int slot = 0;

        public ArmorItem(ItemStack stack) {
            this.stack = stack;
            type = getArmorType(stack);
        }

        public ArmorItem(ItemStack stack, int slot) {
            this(stack);
            this.slot = slot;
        }

        public boolean isEquipped() {
            return slot >= 5 && slot <= 8;
        }

        @Override
        public String toString() {
            return "{\"slot\": " + slot + ", \"name\": \"" + stack.getItem().toString() + "\"}";
        }
    }
}