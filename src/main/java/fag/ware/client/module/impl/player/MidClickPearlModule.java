package fag.ware.client.module.impl.player;

import fag.ware.client.event.data.Subscribe;
import fag.ware.client.event.impl.TickEvent;
import fag.ware.client.module.AbstractModule;
import fag.ware.client.module.data.ModuleCategory;
import fag.ware.client.module.data.ModuleInfo;
import fag.ware.client.module.data.setting.impl.BooleanSetting;
import fag.ware.client.module.data.setting.impl.NumberSetting;
import fag.ware.client.util.game.InventoryUtil;
import fag.ware.client.util.math.Timer;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import org.lwjgl.glfw.GLFW;

@ModuleInfo(name = "MidClickPearl", category = ModuleCategory.PLAYER, description = "Middle click pearl")
public class MidClickPearlModule extends AbstractModule {
    private final NumberSetting delay = new NumberSetting("Delay", 250, 0, 1000);
    private final BooleanSetting switchBack = new BooleanSetting("Switch back", false);
    private final NumberSetting switchBackDelay = ((NumberSetting) new NumberSetting("Switch back delay", 250, 0, 1000).hide(() -> !switchBack.getValue()));

    private final Timer delayTimer = new Timer();
    private final Timer switchBackTimer = new Timer();

    @Subscribe
    public void onTick(TickEvent event) {
        if (mc.player == null || mc.world == null) return;
        if (GLFW.glfwGetMouseButton(mc.getWindow().getHandle(), GLFW.GLFW_MOUSE_BUTTON_MIDDLE) == GLFW.GLFW_PRESS) {
            if (delayTimer.hasElapsed(delay.toInt(), true)) {
                int slot = mc.player.getInventory().getSelectedSlot();
                InventoryUtil.switchToSlot(Items.ENDER_PEARL);
                Item item = mc.player.getMainHandStack().getItem();
                if (item.equals(Items.ENDER_PEARL)) {
                    mc.interactionManager.interactItem(mc.player, Hand.MAIN_HAND);
                }
                if (switchBack.getValue() && switchBackTimer.hasElapsed(switchBackDelay.toInt(), true)) {
                    mc.player.getInventory().setSelectedSlot(slot);
                }
            }
        }
    }

    @Override
    public void onDisable() {
        delayTimer.reset();
        switchBackTimer.reset();
    }
}