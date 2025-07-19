package io.github.client.tracker.impl;

import io.github.client.event.data.Subscribe;
import io.github.client.event.impl.interact.KeyEvent;
import io.github.client.module.impl.render.ClickGUIModule;
import io.github.client.screen.DropdownClickScreen;
import io.github.client.screen.FrameClickScreen;
import io.github.client.tracker.AbstractTracker;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;

public class ScreenTracker extends AbstractTracker<Screen> {
    private static final ScreenTracker tracker = new ScreenTracker();

    public static ScreenTracker getInstance() {
        return tracker;
    }

    @Override
    public void initialize() {
        super.initialize();
        list.add(new DropdownClickScreen());
        list.add(new FrameClickScreen());
    }

    @Subscribe
    public void onKey(KeyEvent event) {
        list.forEach(s -> {
            switch (ModuleTracker.INSTANCE.getByClass(ClickGUIModule.class).mode.getValue()) {
                case "Dropdown" -> {
                    if (event.key == ModuleTracker.INSTANCE.getByClass(ClickGUIModule.class).getKeybinds().getFirst()) {
                        MinecraftClient.getInstance().setScreen(getByClass(DropdownClickScreen.class));
                        ModuleTracker.INSTANCE.getByClass(ClickGUIModule.class).setEnabled(true);
                    }
                }
                case "Frame" -> {
                    if (event.key == ModuleTracker.INSTANCE.getByClass(ClickGUIModule.class).getKeybinds().getFirst()) {
                        MinecraftClient.getInstance().setScreen(getByClass(FrameClickScreen.class));
                        ModuleTracker.INSTANCE.getByClass(ClickGUIModule.class).setEnabled(true);
                    }
                }
            }
        });
    }
}
