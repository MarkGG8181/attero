package fag.ware.client.module.impl.render;

import fag.ware.client.event.data.Subscribe;
import fag.ware.client.event.impl.game.TickEvent;
import fag.ware.client.module.AbstractModule;
import fag.ware.client.module.data.ModuleCategory;
import fag.ware.client.module.data.ModuleInfo;
import fag.ware.client.module.data.setting.impl.StringSetting;
import fag.ware.client.screen.ClickScreen;
import fag.ware.client.screen.JelloClickScreen;
import fag.ware.client.screen.PanelClickScreen;
import org.lwjgl.glfw.GLFW;

@ModuleInfo(name = "ClickGUI", category = ModuleCategory.RENDER, description = "Renders the ClickGUI")
public class ClickGUIModule extends AbstractModule {
    public StringSetting mode = new StringSetting("Mode", "Fagware", "Fagware", "Jello", "Panel");
    public StringSetting theme = (StringSetting) new StringSetting("Theme", "Marine", "Marine", "Dark", "White").hide(() -> mode.getValue().equals("Jello"));

    @Subscribe
    public void onTick(TickEvent event) {
        if (isEnabled() && mc.currentScreen == null) {
            setEnabled(false);
        }
    }

    @Override
    public void onInit() {
        getKeybinds().add(GLFW.GLFW_KEY_RIGHT_SHIFT);
    }

    @Override
    public void onDisable() {
        if (mc.currentScreen instanceof ClickScreen || mc.currentScreen instanceof JelloClickScreen || mc.currentScreen instanceof PanelClickScreen) {
            mc.setScreen(null);
        }
    }
}