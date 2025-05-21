package fag.ware.client.module.impl.misc;

import fag.ware.client.event.data.Subscribe;
import fag.ware.client.event.impl.TickEvent;
import fag.ware.client.module.AbstractModule;
import fag.ware.client.module.data.ModuleCategory;
import fag.ware.client.module.data.ModuleInfo;
import fag.ware.client.module.data.setting.impl.BooleanSetting;
import fag.ware.client.module.data.setting.impl.NumberSetting;
import fag.ware.client.util.game.InventoryUtil;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.item.Items;

@ModuleInfo(name = "Helper", category = ModuleCategory.MISC, description = "Helps you in some situations")
public class HelperModule extends AbstractModule {
    private final BooleanSetting autoSwitch = new BooleanSetting("Goto slot on open-inv", true);
    private final NumberSetting slot = (NumberSetting) new NumberSetting("Slot", 6, 1, 9).hide(() -> !autoSwitch.getValue());
    private final BooleanSetting switchTotem = (BooleanSetting) new BooleanSetting("Equip totem on open-inv", true).hide(autoSwitch::getValue);

    @Subscribe
    public void onTick(TickEvent event) {
        if (mc.player == null || mc.world == null) return;

        if (autoSwitch.getValue() && inInventory()) {
            mc.player.getInventory().setSelectedSlot(slot.getValue().intValue() - 1);
        }

        if (switchTotem.getValue() && inInventory()) {
            InventoryUtil.switchToSlot(Items.TOTEM_OF_UNDYING);
        }
    }

    private boolean inInventory() {
        return mc.currentScreen instanceof InventoryScreen;
    }
}