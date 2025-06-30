package io.github.client.module.impl.movement;

import io.github.client.event.data.Subscribe;
import io.github.client.event.impl.player.UpdateEvent;
import io.github.client.module.AbstractModule;
import io.github.client.module.data.ModuleCategory;
import io.github.client.module.data.ModuleInfo;
import io.github.client.module.data.setting.impl.MultiStringSetting;
import io.github.client.util.java.GLFWUtil;
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
            var allowed = new HashSet<>(Arrays.asList(keys.getValue()));

            for (var entry : KEY_BIND_NAMES.entrySet()) {
                if (allowed.contains(entry.getValue())) {
                    KeyBinding keyBind = entry.getKey();

                    var isPressed = GLFWUtil.isKeyDown(keyBind.boundKey.getCode());

                    keyBind.setPressed(isPressed);
                }
            }
        }
    }
}