package fag.ware.client.module.impl.player;

import fag.ware.client.event.data.Subscribe;
import fag.ware.client.event.impl.game.TickEvent;
import fag.ware.client.module.AbstractModule;
import fag.ware.client.module.data.ModuleCategory;
import fag.ware.client.module.data.ModuleInfo;
import fag.ware.client.module.data.setting.impl.BooleanSetting;
import fag.ware.client.module.data.setting.impl.MultiStringSetting;
import fag.ware.client.module.data.setting.impl.NumberSetting;
import fag.ware.client.util.SystemUtil;
import fag.ware.client.util.game.InventoryUtil;
import fag.ware.client.util.math.Timer;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
//import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.registry.Registries;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.SlotActionType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("ALL")
@ModuleInfo(name = "InventoryManager", description = "Manages your inventory & hotbar", category = ModuleCategory.PLAYER)
public class InventoryManagerModule extends AbstractModule {
    private final BooleanSetting autoArmor = new BooleanSetting("Auto armor", true);
    private final NumberSetting equipDelay = (NumberSetting) new NumberSetting("Equip delay", 30, 0, 300).hide(() -> !autoArmor.getValue());
    private final BooleanSetting dropTrash = new BooleanSetting("Drop trash", true);
    private final MultiStringSetting trashItems = (MultiStringSetting) new MultiStringSetting("Trash items", InventoryUtil.getExampleItems(), InventoryUtil.getAllItems()).hide(() -> !dropTrash.getValue());

    private final NumberSetting dropDelay = (NumberSetting) new NumberSetting("Drop delay", 150, 0, 300).hide(() -> !dropTrash.getValue() && !autoArmor.getValue());

    private final List<Integer> trashSlotQueue = new ArrayList<>();
    private final List<Integer> armorQueue = new ArrayList<>();
    private final Timer timer = new Timer();

    @Override
    public void onEnable() {
        trashSlotQueue.clear();
        armorQueue.clear();
        timer.reset();
    }

    @Subscribe
    public void onTick(TickEvent event) {
        if (mc.player == null) {
            return;
        }

        if (mc.currentScreen instanceof InventoryScreen inv) {
            var handler = inv.getScreenHandler();

            if (autoArmor.getValue()) {
                performAutoArmor(handler);
            }

            if (dropTrash.getValue()) {
                dropTrashItemsOpenInventory(handler);
            }
        } /*else if (mc.currentScreen == null) {
            if (dropTrash.getValue()) {
                dropTrashItemsHotbar();
            }
        }
        */
    }

    private void quickMoveStack(int syncId, int slot) {
        if (timer.hasElapsed(equipDelay.toInt(), true)) {
            mc.interactionManager.clickSlot(
                    syncId,
                    slot,
                    1, //1 is for all, 0 is for only one item
                    SlotActionType.QUICK_MOVE,
                    mc.player
            );
        }
    }

    private void throwStack(int syncId, int slot) {
        if (timer.hasElapsed(dropDelay.toInt(), true)) {
            mc.interactionManager.clickSlot(
                    syncId,
                    slot,
                    1, //1 is for all, 0 is for only one item
                    SlotActionType.THROW,
                    mc.player
            );
        }
    }

    private void performAutoArmor(ScreenHandler handler) {
        Map<InventoryUtil.ArmorType, InventoryUtil.ArmorItem> bestArmor = new HashMap<>();

        for (var i = 0; i < handler.slots.size(); i++) {
            var slot = handler.getSlot(i);
            var stack = slot.getStack();

            if (!stack.isEmpty() && InventoryUtil.isArmor(stack)) {
                var armorItem = new InventoryUtil.ArmorItem(stack, i);
                var type = armorItem.type;

                var best = bestArmor.get(type);
                if (best == null || InventoryUtil.isBetterItem(stack, best.stack, Enchantments.PROTECTION, EntityAttributes.ARMOR)) {
                    bestArmor.put(type, armorItem);
                }
            }
        }

        armorQueue.clear();
        for (var armorItem : bestArmor.values()) {
            armorQueue.add(armorItem.slot);
        }

        for (var i = 0; i < handler.slots.size(); i++) {
            var slot = handler.getSlot(i);
            var stack = slot.getStack();

            if (!stack.isEmpty() && InventoryUtil.isArmor(stack)) {
                var armorItem = new InventoryUtil.ArmorItem(stack, i);

                boolean best = armorQueue.contains(i);
                if (armorItem.isEquipped()) {
                    if (!best) {
                        throwStack(handler.syncId, i); //drop worse
                    }
                } else if (best) {
                    quickMoveStack(handler.syncId, i);
                }
            }
        }

        for (var i = 0; i < handler.slots.size(); i++) {
            var slot = handler.getSlot(i);
            var stack = slot.getStack();

            if (!stack.isEmpty() && InventoryUtil.isArmor(stack)) {
                var armorItem = new InventoryUtil.ArmorItem(stack, i);

                if (!armorItem.isEquipped()) {
                    throwStack(handler.syncId, i); //drop worse
                }
            }
        }

        armorQueue.clear();
    }

    private void dropTrashItemsOpenInventory(ScreenHandler handler) {
        if (trashSlotQueue.isEmpty()) {
            for (var i = 0; i < handler.slots.size(); i++) {
                var slot = handler.getSlot(i);
                var stack = slot.getStack();

                if (!stack.isEmpty()) {
                    var itemName = Registries.ITEM.getId(stack.getItem()).getPath();
                    if (trashItems.enabled(SystemUtil.toClassName(itemName))) {
                        trashSlotQueue.add(i);
                    }
                }
            }
        }

        if (!trashSlotQueue.isEmpty()) {
            throwStack(handler.syncId, trashSlotQueue.removeFirst());
        }

        trashSlotQueue.clear();
    }

    /*
    private void dropTrashItemsHotbar() {
        var inv = mc.player.getInventory();
        int current = inv.getSelectedSlot();

        if (trashSlotQueue.isEmpty()) {
            for (int i = 0; i < inv.getHotbarSize(); i++) {
                var stack = inv.getStack(i);

                if (!stack.isEmpty()) {
                    var itemName = Registries.ITEM.getId(stack.getItem()).getPath();
                    if (trashItems.enabled(SystemUtil.toClassName(itemName))) {
                        trashSlotQueue.add(i);
                    }
                }
            }
        }

        if (!trashSlotQueue.isEmpty()) {
            int trash = trashSlotQueue.removeFirst();

            inv.setSelectedSlot(trash);
            sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.DROP_ITEM, mc.player.getBlockPos(), mc.player.getFacing()));
            inv.removeStack(trash);
        }

        if (trashSlotQueue.isEmpty()) {
            inv.setSelectedSlot(current);
        }
    }
     */
}