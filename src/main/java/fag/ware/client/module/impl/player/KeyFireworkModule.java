package fag.ware.client.module.impl.player;

import fag.ware.client.event.data.Subscribe;
import fag.ware.client.event.impl.game.TickEvent;
import fag.ware.client.module.AbstractModule;
import fag.ware.client.module.data.ModuleCategory;
import fag.ware.client.module.data.ModuleInfo;
import fag.ware.client.module.data.setting.impl.BooleanSetting;
import fag.ware.client.module.data.setting.impl.NumberSetting;
import fag.ware.client.util.game.InventoryUtil;
import fag.ware.client.util.math.Timer;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;

@SuppressWarnings("ALL")
@ModuleInfo(name = "KeyFirework", description = "Switches to a firework on enable", category = ModuleCategory.PLAYER)
public class KeyFireworkModule extends AbstractModule {
    private final NumberSetting delay = new NumberSetting("Delay", 250, 0, 1000);
    private final BooleanSetting switchBack = new BooleanSetting("Switch back", false);
    private final NumberSetting switchBackDelay = (NumberSetting) new NumberSetting("Switch back delay", 250, 0, 1000).hide(() -> !switchBack.getValue());

    private final Timer delayTimer = new Timer();
    private final Timer switchBackTimer = new Timer();
    private int previousSlot = -1;
    private boolean fired = false;

    @Subscribe
    public void onTick(TickEvent event) {
        if (mc.player == null || mc.world == null) return;

        var hasElytra = mc.player.getEquippedStack(EquipmentSlot.CHEST).getItem() == Items.ELYTRA;
        if (!hasElytra) {
            return;
        }

        if (!fired && delayTimer.hasElapsed(delay.toInt(), true)) {
            var mainHandItem = mc.player.getStackInHand(Hand.MAIN_HAND).getItem();
            previousSlot = mc.player.getInventory().getSelectedSlot();
            InventoryUtil.switchToSlot(Items.FIREWORK_ROCKET);
            if (mainHandItem == Items.FIREWORK_ROCKET) {
                mc.interactionManager.interactItem(mc.player, Hand.MAIN_HAND);
                switchBackTimer.reset();
                fired = true;
            }
        }

        if (fired && switchBack.getValue() && switchBackTimer.hasElapsed(switchBackDelay.toInt(), true)) {
            mc.player.getInventory().setSelectedSlot(previousSlot);
            toggle();
        }
    }

    public void onEnable() {
        delayTimer.reset();
        fired = false;
    }

    public void onDisable() {
        delayTimer.reset();
        switchBackTimer.reset();
        previousSlot = -1;
        fired = false;
    }
}