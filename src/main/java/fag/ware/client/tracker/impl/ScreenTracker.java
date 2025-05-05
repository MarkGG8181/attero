package fag.ware.client.tracker.impl;

import fag.ware.client.Fagware;
import fag.ware.client.event.data.Subscribe;
import fag.ware.client.event.impl.KeyEvent;
import fag.ware.client.screen.ClickScreen;
import fag.ware.client.tracker.Tracker;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import org.lwjgl.glfw.GLFW;

public class ScreenTracker extends Tracker<Screen> {
    @Override
    public void initialize() {
        Fagware.BUS.register(this);
        getSet().add(new ClickScreen());
    }

    @Subscribe
    public void onKey(KeyEvent event) {
        getSet().forEach(s -> {
            if (event.getKey() == GLFW.GLFW_KEY_RIGHT_SHIFT) {
                MinecraftClient.getInstance().setScreen(getByClass(ClickScreen.class));
            }
        });
    }
}
