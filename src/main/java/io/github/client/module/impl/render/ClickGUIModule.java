package io.github.client.module.impl.render;

import io.github.client.event.data.Subscribe;
import io.github.client.event.impl.game.TickEvent;
import io.github.client.module.AbstractModule;
import io.github.client.module.data.ModuleCategory;
import io.github.client.module.data.ModuleInfo;
import io.github.client.module.data.setting.impl.StringSetting;
import io.github.client.screen.ClickScreen;
import io.github.client.screen.JelloClickScreen;
import io.github.client.screen.PanelClickScreen;
import org.lwjgl.glfw.GLFW;

@ModuleInfo(name = "ClickGUI", category = ModuleCategory.RENDER, description = "Renders the ClickGUI")
public class ClickGUIModule extends AbstractModule {
    public StringSetting mode = new StringSetting("Mode", "Attero", "Attero", "Jello", "Panel");
    public StringSetting theme = (StringSetting) new StringSetting("Theme", "Marine", "Marine", "Dark", "White").hide(() -> mode.getValue().equals("Jello"));

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
        if (mc.currentScreen instanceof ClickScreen || mc.currentScreen instanceof JelloClickScreen || mc.currentScreen instanceof PanelClickScreen) {
            mc.setScreen(null);
        }
    }
}