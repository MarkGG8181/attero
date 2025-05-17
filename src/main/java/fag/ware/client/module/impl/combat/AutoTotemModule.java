package fag.ware.client.module.impl.combat;

import fag.ware.client.event.data.Subscribe;
import fag.ware.client.event.impl.TickEvent;
import fag.ware.client.module.AbstractModule;
import fag.ware.client.module.data.ModuleCategory;
import fag.ware.client.module.data.ModuleInfo;
import fag.ware.client.module.data.setting.impl.BooleanSetting;
import fag.ware.client.module.data.setting.impl.NumberSetting;
import fag.ware.client.util.math.Timer;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.SlotActionType;

@ModuleInfo(name = "AutoTotem", category = ModuleCategory.COMBAT, description = "Automatically puts a totem in your offhand")
public class AutoTotemModule extends AbstractModule {
    private final BooleanSetting manuallyOpenInv = new BooleanSetting("Manually open inventory", false);
    private final NumberSetting inventoryOpenDelay = (NumberSetting) new NumberSetting("Inventory open delay", 60, 0, 750).hide(manuallyOpenInv::getValue);
    private final NumberSetting totemDelay = new NumberSetting("Totem delay", 100, 0, 750);

    private final Timer timer = new Timer();

    @Subscribe
    public void onTick(TickEvent event) {
        if (mc.player == null || mc.world == null) return;
        
        ItemStack offhand = mc.player.getOffHandStack();
        if (offhand.getItem() == Items.TOTEM_OF_UNDYING) return;
        
        thing();
    }

    private void thing() {
        for (int i = 0; i < 36; i++) {
            ItemStack item = mc.player.getInventory().getStack(i);
            if (item.getItem() == Items.TOTEM_OF_UNDYING) {
                int slot = i < 9 ? i + 36 : i;

                if (!(mc.currentScreen instanceof InventoryScreen) && timer.hasElapsed(inventoryOpenDelay.toInt(), true) && !manuallyOpenInv.getValue()) {
                    mc.setScreen(new InventoryScreen(mc.player));
                }

                //ai :drool:
                if (timer.hasElapsed(totemDelay.toInt(), true)) {
                    mc.interactionManager.clickSlot(
                            mc.player.currentScreenHandler.syncId,
                            slot,
                            0,
                            SlotActionType.PICKUP,
                            mc.player
                    );

                    mc.interactionManager.clickSlot(
                            mc.player.currentScreenHandler.syncId,
                            45,
                            0,
                            SlotActionType.PICKUP,
                            mc.player
                    );
                }
                break;
            }
        }
    }

    @Override
    public void onDisable() {
        timer.reset();
    }
}