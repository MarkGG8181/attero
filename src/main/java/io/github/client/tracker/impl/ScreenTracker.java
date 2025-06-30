package io.github.client.tracker.impl;

import io.ml.security.JvmArgsChecker;
import io.github.client.event.data.Subscribe;
import io.github.client.event.impl.interact.KeyEvent;
import io.github.client.module.impl.render.ClickGUIModule;
import io.github.client.screen.DropdownClickScreen;
import io.github.client.screen.JelloClickScreen;
import io.github.client.screen.LoginScreen;
import io.github.client.screen.FrameClickScreen;
import io.github.client.tracker.AbstractTracker;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;

public class ScreenTracker extends AbstractTracker<Screen> {
    private static final ScreenTracker tracker = new ScreenTracker();

    public static ScreenTracker getInstance() {
        return tracker;
    }

    public ScreenTracker()
    {
        JvmArgsChecker.force();
    }

    @Override
    public void initialize() {
        super.initialize();
        list.add(new DropdownClickScreen());
        list.add(new JelloClickScreen());
        list.add(new FrameClickScreen());
    }

    @Subscribe
    public void onKey(KeyEvent event) {
        if (!AuthTracker.INSTANCE.isAuthenticated()) {
            MinecraftClient.getInstance().setScreen(new LoginScreen());
            return;
        }

        list.forEach(s -> {
            switch (ModuleTracker.INSTANCE.getByClass(ClickGUIModule.class).mode.getValue()) {
                case "Dropdown" -> {
                    if (event.key == ModuleTracker.INSTANCE.getByClass(ClickGUIModule.class).getKeybinds().getFirst()) {
                        MinecraftClient.getInstance().setScreen(getByClass(DropdownClickScreen.class));
                        ModuleTracker.INSTANCE.getByClass(ClickGUIModule.class).setEnabled(true);
                    }
                }
                case "Jello" -> {
                    if (event.key == ModuleTracker.INSTANCE.getByClass(ClickGUIModule.class).getKeybinds().getFirst()) {
                        MinecraftClient.getInstance().setScreen(getByClass(JelloClickScreen.class));
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
