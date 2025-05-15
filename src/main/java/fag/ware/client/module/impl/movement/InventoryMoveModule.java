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

import java.util.*;

@ModuleInfo(name = "InventoryMove", description = "Allows you to walk with open gui's", category = ModuleCategory.MOVEMENT)
public class InventoryMoveModule extends AbstractModule {

    private static final Map<KeyBinding, String> KEY_BIND_NAMES = new LinkedHashMap<>();

    static {
        KEY_BIND_NAMES.put(mc.options.forwardKey, "Forward");
        KEY_BIND_NAMES.put(mc.options.leftKey, "Left");
        KEY_BIND_NAMES.put(mc.options.rightKey, "Right");
        KEY_BIND_NAMES.put(mc.options.backKey, "Back");
        KEY_BIND_NAMES.put(mc.options.jumpKey, "Jump");
        KEY_BIND_NAMES.put(mc.options.sneakKey, "Sneak");
        KEY_BIND_NAMES.put(mc.options.sprintKey, "Sprint");
    }

    private final MultiStringSetting keys = new MultiStringSetting(
            "Keys",
            new String[]{"Forward", "Left", "Right", "Back"},
            KEY_BIND_NAMES.values().toArray(new String[0])
    );

    @Subscribe
    public void onUpdate(UpdateEvent event) {
        if (mc.currentScreen != null && !(mc.currentScreen instanceof ChatScreen)) {
            Set<String> allowed = new HashSet<>(Arrays.asList(keys.getValue()));

            for (Map.Entry<KeyBinding, String> entry : KEY_BIND_NAMES.entrySet()) {
                if (allowed.contains(entry.getValue())) {
                    KeyBinding keyBind = entry.getKey();
                    KeyBindingAccessor accessor = (KeyBindingAccessor) keyBind;
                    boolean isPressed = GLFWUtil.isKeyDown(accessor.getBoundKey().getCode());
                    keyBind.setPressed(isPressed);
                }
            }
        }
    }
}