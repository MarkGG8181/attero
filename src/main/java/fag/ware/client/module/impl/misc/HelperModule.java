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
    private final NumberSetting actionDelay = new NumberSetting("Delay (ms)", 250, 50, 2000);
    private final BooleanSetting autoCloseInv = new BooleanSetting("Close inv after action", false);
    private final BooleanSetting survivalOnly = new BooleanSetting("Survival only", false);

    private long lastActionTime = 0;

    @Subscribe
    public void onTick(TickEvent event) {
        if (mc.player == null || mc.world == null) return;
        if (survivalOnly.getValue() && mc.player.isCreative()) return;

        if (System.currentTimeMillis() - lastActionTime < actionDelay.toInt()) return;

        if (autoSwitch.getValue() && inInventory()) {
            mc.player.getInventory().setSelectedSlot(slot.getValue().intValue() - 1);
        }

        if (switchTotem.getValue() && inInventory()) {
            boolean hasTotem = mc.player.getOffHandStack().getItem() == Items.TOTEM_OF_UNDYING
                    || mc.player.getMainHandStack().getItem() == Items.TOTEM_OF_UNDYING;

            if (!hasTotem) {
                InventoryUtil.switchToSlot(Items.TOTEM_OF_UNDYING);
            }
        }

        if ((autoSwitch.getValue() || switchTotem.getValue()) && inInventory() && autoCloseInv.getValue()) {
            mc.setScreen(null);
        }

        lastActionTime = System.currentTimeMillis();
    }

    private boolean inInventory() {
        return mc.currentScreen instanceof InventoryScreen;
    }

    @Override
    public void onEnable() {
        lastActionTime = 0;
    }
}