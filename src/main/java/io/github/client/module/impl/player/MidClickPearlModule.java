package io.github.client.module.impl.player;

import io.github.client.event.data.Subscribe;
import io.github.client.event.impl.interact.MiddleClickEvent;
import io.github.client.module.AbstractModule;
import io.github.client.module.data.ModuleCategory;
import io.github.client.module.data.ModuleInfo;
import io.github.client.module.data.setting.impl.BooleanSetting;
import io.github.client.module.data.setting.impl.NumberSetting;
import io.github.client.tracker.impl.ModuleTracker;
import io.github.client.util.game.InventoryUtil;
import io.github.client.util.java.math.Timer;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;

@ModuleInfo(name = "MidClickPearl", category = ModuleCategory.PLAYER, description = "Middle click pearl")
public class MidClickPearlModule extends AbstractModule {
    private final NumberSetting delay = new NumberSetting("Delay", 250, 0, 1000);
    private final BooleanSetting switchBack = new BooleanSetting("Switch back", false);
    private final NumberSetting switchBackDelay = ((NumberSetting) new NumberSetting("Switch back delay", 250, 0, 1000).hide(() -> !switchBack.getValue()));

    private final Timer delayTimer = new Timer();
    private final Timer switchBackTimer = new Timer();

    @Subscribe
    public void onTick(MiddleClickEvent event) {
        if (mc.player == null || mc.world == null) return;

        if (delayTimer.hasElapsed(delay.toInt(), true)) {
            var slot = mc.player.getInventory().getSelectedSlot();
            var item = mc.player.getMainHandStack().getItem();

            InventoryUtil.switchToSlot(Items.ENDER_PEARL);

            if (item.equals(Items.ENDER_PEARL)) {
                mc.interactionManager.interactItem(mc.player, Hand.MAIN_HAND);
            }

            if (switchBack.getValue() && switchBackTimer.hasElapsed(switchBackDelay.toInt(), true)) {
                mc.player.getInventory().setSelectedSlot(slot);
            }
        }
    }

    public void onEnable() {
        delayTimer.reset();
        switchBackTimer.reset();
        ModuleTracker.INSTANCE.getByName("MidClickFriend").setEnabled(false);
    }
}