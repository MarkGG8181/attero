package io.github.client.module.impl.combat;

import io.github.client.event.data.Subscribe;
import io.github.client.event.impl.game.TickEvent;
import io.github.client.module.AbstractModule;
import io.github.client.module.data.ModuleCategory;
import io.github.client.module.data.ModuleInfo;
import io.github.client.module.data.setting.impl.BooleanSetting;
import io.github.client.module.data.setting.impl.NumberSetting;
import io.github.client.util.game.InventoryUtil;
import io.github.client.util.math.Timer;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.SlotActionType;

@SuppressWarnings("ALL")
@ModuleInfo(name = "AutoTotem", category = ModuleCategory.COMBAT, description = "Automatically puts a totem in your offhand once hovered")
public class AutoTotemModule extends AbstractModule {
    private final NumberSetting totemDelay = new NumberSetting("Totem delay", 100, 0, 750);
    private final BooleanSetting switchToTotem = new BooleanSetting("Equip totem on open-inv", true);
    private final Timer timer = new Timer();

    @Subscribe
    public void onTick(TickEvent ignoredEvent) {
        if (mc.player == null || mc.world == null) return;

        var offhand = mc.player.getOffHandStack();
        if (offhand.getItem() == Items.TOTEM_OF_UNDYING) return;
        if (switchToTotem.getValue()) InventoryUtil.switchToSlot(Items.TOTEM_OF_UNDYING);

        handleTotem();
    }

    private void handleTotem() {
        if (!(mc.currentScreen instanceof InventoryScreen inv)) return;

        var hoveredSlot = inv.focusedSlot;
        if (hoveredSlot == null) return;

        var hoveredStack = hoveredSlot.getStack();
        if (hoveredStack.getItem() != Items.TOTEM_OF_UNDYING) return;

        if (timer.hasElapsed(totemDelay.toInt(), true)) {
            mc.interactionManager.clickSlot(
                    mc.player.currentScreenHandler.syncId,
                    hoveredSlot.getIndex(),
                    40,
                    SlotActionType.SWAP,
                    mc.player
            );
        }
    }

    public void onDisable() {
        timer.reset();
    }
}