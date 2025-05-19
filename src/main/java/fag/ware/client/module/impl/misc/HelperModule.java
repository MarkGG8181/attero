package fag.ware.client.module.impl.misc;

import fag.ware.client.event.data.Subscribe;
import fag.ware.client.event.impl.TickEvent;
import fag.ware.client.module.AbstractModule;
import fag.ware.client.module.data.ModuleCategory;
import fag.ware.client.module.data.ModuleInfo;
import fag.ware.client.module.data.setting.impl.BooleanSetting;
import fag.ware.client.module.data.setting.impl.NumberSetting;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

// ass description
@ModuleInfo(name = "Helper", category = ModuleCategory.MISC, description = "Helps you in some situations")
public class HelperModule extends AbstractModule {
    public BooleanSetting autoSwitchToSlot = new BooleanSetting("Auto Switch to slot on inventory open", true);
    public NumberSetting slotNumber = (NumberSetting) new NumberSetting("Slot Number", 6, 1, 9).hide(() -> !autoSwitchToSlot.getValue());
    public BooleanSetting switchToTotem = (BooleanSetting) new BooleanSetting("Switch to totem on inventory open", true).hide(() -> autoSwitchToSlot.getValue());
    @Subscribe
    public void onTick(TickEvent event) {
        if (mc.player == null || mc.world == null) return;
        if (autoSwitchToSlot.getValue() && inInventory()) {
            mc.player.getInventory().setSelectedSlot(slotNumber.getValue().intValue() - 1);
        }
        if (switchToTotem.getValue() && inInventory()) {
            for (int i = 0; i < 9; i++) {
                ItemStack stack = mc.player.getInventory().getStack(i);
                if (stack.isEmpty()) return;
                if (stack.getItem().equals(Items.TOTEM_OF_UNDYING)) {
                    mc.player.getInventory().setSelectedSlot(i);
                }
            }
        }
    }
    public boolean inInventory() {
        return mc.currentScreen instanceof InventoryScreen;
    }
}
