package io.github.client.module.impl.render;

import io.github.client.event.data.Subscribe;
import io.github.client.event.impl.game.TickEvent;
import io.github.client.module.AbstractModule;
import io.github.client.module.data.ModuleCategory;
import io.github.client.module.data.ModuleInfo;
import io.github.client.module.data.setting.impl.StringSetting;
import io.github.client.screen.DropdownClickScreen;
import io.github.client.screen.JelloClickScreen;
import io.github.client.screen.FrameClickScreen;
import org.lwjgl.glfw.GLFW;

@ModuleInfo(name = "ClickGUI", category = ModuleCategory.RENDER, description = "Renders the ClickGUI")
public class ClickGUIModule extends AbstractModule {
    public final StringSetting mode = new StringSetting("Mode", "Dropdown", "Dropdown", "Frame"/*, "Jello"*/);
    public final StringSetting theme = new StringSetting("Theme", "Marine", "Marine", "Dark", "White", "Future dark").hide(() -> mode.getValue().equals("Jello"));

    @Subscribe
    public void onTick(TickEvent ignoredEvent) {
        if (isEnabled() && mc.currentScreen == null) {
            setEnabled(false);
        }
    }

    public void onInit() {
        getKeybinds().add(GLFW.GLFW_KEY_RIGHT_SHIFT);
    }

    public void onDisable() {
        if (mc.currentScreen instanceof DropdownClickScreen || mc.currentScreen instanceof JelloClickScreen || mc.currentScreen instanceof FrameClickScreen) {
            mc.setScreen(null);
        }
    }
}