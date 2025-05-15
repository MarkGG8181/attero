package fag.ware.client.module.impl.movement;

import fag.ware.client.event.data.Subscribe;
import fag.ware.client.event.impl.UpdateEvent;
import fag.ware.client.mixin.KeyBindingAccessor;
import fag.ware.client.module.AbstractModule;
import fag.ware.client.module.data.ModuleCategory;
import fag.ware.client.module.data.ModuleInfo;
import fag.ware.client.util.GLFWUtil;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.option.KeyBinding;

@ModuleInfo(name = "InventoryMove", description = "Allows you to walk with open gui's", category = ModuleCategory.MOVEMENT)
public class InventoryMoveModule extends AbstractModule {
    @Subscribe
    public void onUpdate(UpdateEvent event) {
        var keyBinds = new KeyBinding[]{
                mc.options.forwardKey,
                mc.options.leftKey,
                mc.options.rightKey,
                mc.options.backKey,
                mc.options.jumpKey,
                mc.options.sprintKey
        };

        if (mc.currentScreen != null && !(mc.currentScreen instanceof ChatScreen)) {
            for (KeyBinding keyBind : keyBinds) {
                KeyBindingAccessor accessor = (KeyBindingAccessor) keyBind;
                keyBind.setPressed(GLFWUtil.isKeyDown(accessor.getBoundKey().getCode()));
            }
        }
    }

    @Override
    public void onEnable() {
    }

    @Override
    public void onDisable() {
    }
}