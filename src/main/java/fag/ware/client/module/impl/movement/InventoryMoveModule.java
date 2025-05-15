package fag.ware.client.module.impl.movement;

import fag.ware.client.event.data.Subscribe;
import fag.ware.client.event.impl.UpdateEvent;
import fag.ware.client.mixin.KeyBindingAccessor;
import fag.ware.client.module.AbstractModule;
import fag.ware.client.module.data.ModuleCategory;
import fag.ware.client.module.data.ModuleInfo;
import fag.ware.client.module.data.setting.impl.MultiStringSetting;
import fag.ware.client.util.GLFWUtil;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.option.KeyBinding;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@ModuleInfo(name = "InventoryMove", description = "Allows you to walk with open gui's", category = ModuleCategory.MOVEMENT)
public class InventoryMoveModule extends AbstractModule {

    public MultiStringSetting keys = new MultiStringSetting("Keys", new String[]{"Forward", "Left", "Right", "Back"}, new String[]{"Forward", "Left", "Right", "Back", "Jump", "Sneak"});

    @Subscribe
    public void onUpdate(UpdateEvent event) {
        List<KeyBinding> keyBinds = Arrays.asList(
                mc.options.forwardKey,
                mc.options.leftKey,
                mc.options.rightKey,
                mc.options.backKey,
                mc.options.jumpKey,
                mc.options.sneakKey
        );
        List<String> allowedKeys = Arrays.asList(keys.getValue());
        List<KeyBinding> filteredKeyBinds = keyBinds.stream()
                .filter(keyBind -> allowedKeys.contains(getKeyName(keyBind)))
                .collect(Collectors.toList());

        if (mc.currentScreen != null && !(mc.currentScreen instanceof ChatScreen)) {
            filteredKeyBinds.forEach(keyBind -> {
                KeyBindingAccessor accessor = (KeyBindingAccessor) keyBind;
                boolean keyPressed = GLFWUtil.isKeyDown(accessor.getBoundKey().getCode());
                keyBind.setPressed(keyPressed);
            });
        }
    }

    @Override
    public void onEnable() {
    }

    @Override
    public void onDisable() {
    }

    private String getKeyName(KeyBinding keyBind) {
        if (keyBind == mc.options.forwardKey) return "Forward";
        if (keyBind == mc.options.leftKey) return "Left";
        if (keyBind == mc.options.rightKey) return "Right";
        if (keyBind == mc.options.backKey) return "Back";
        if (keyBind == mc.options.jumpKey) return "Jump";
        if (keyBind == mc.options.sprintKey) return "Sprint";
        if (keyBind == mc.options.sneakKey) return "Sneak";
        return null;
    }
}
