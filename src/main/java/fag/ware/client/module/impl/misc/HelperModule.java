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

@ModuleInfo(name = "Helper", category = ModuleCategory.MISC, description = "Helps you in some situations")
public class HelperModule extends AbstractModule {
    public BooleanSetting autoSwitch = new BooleanSetting("Goto Slot on Inventory Open", true);
    public NumberSetting slot = (NumberSetting) new NumberSetting("Slot", 6, 1, 9).hide(() -> !autoSwitch.getValue());
    public BooleanSetting switchTotem = (BooleanSetting) new BooleanSetting("Switch to Totem on inv open", true).hide(() -> autoSwitch.getValue());

    @Subscribe
    public void onTick(TickEvent event) {
        if (mc.player == null || mc.world == null) return;

        if (autoSwitch.getValue() && inInventory()) {
            switchToSlot(slot.getValue().intValue() - 1);
        }

        if (switchTotem.getValue() && inInventory()) {
            switchToTotem();
        }
    }

    private void switchToSlot(int slotIndex) {
        mc.player.getInventory().setSelectedSlot(slotIndex);
    }

    private void switchToTotem() {
        for (int i = 0; i < 9; i++) {
            ItemStack stack = mc.player.getInventory().getStack(i);
            if (stack.isEmpty()) continue;
            if (stack.getItem().equals(Items.TOTEM_OF_UNDYING)) {
                mc.player.getInventory().setSelectedSlot(i);
                return;
            }
        }
    }

    public boolean inInventory() {
        return mc.currentScreen instanceof InventoryScreen;
    }
}
