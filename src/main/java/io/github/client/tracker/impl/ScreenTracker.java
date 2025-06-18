package io.github.client.tracker.impl;

import io.ml.security.JvmArgsChecker;
import io.github.client.Attero;
import io.github.client.event.data.Subscribe;
import io.github.client.event.impl.interact.KeyEvent;
import io.github.client.module.impl.render.ClickGUIModule;
import io.github.client.screen.ClickScreen;
import io.github.client.screen.JelloClickScreen;
import io.github.client.screen.LoginScreen;
import io.github.client.screen.PanelClickScreen;
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
        Attero.BUS.register(this);
        getSet().add(new ClickScreen());
        getSet().add(new JelloClickScreen());
        getSet().add(new PanelClickScreen());
    }

    @Subscribe
    public void onKey(KeyEvent event) {
        if (!AuthTracker.getInstance().isAuthenticated()) {
            MinecraftClient.getInstance().setScreen(new LoginScreen());
            return;
        }

        getSet().forEach(s -> {
            switch (ModuleTracker.getInstance().getByClass(ClickGUIModule.class).mode.getValue()) {
                case "Attero" -> {
                    if (event.getKey() == ModuleTracker.getInstance().getByClass(ClickGUIModule.class).getKeybinds().getFirst()) {
                        MinecraftClient.getInstance().setScreen(getByClass(ClickScreen.class));
                        ModuleTracker.getInstance().getByClass(ClickGUIModule.class).setEnabled(true);
                    }
                }
                case "Jello" -> {
                    if (event.getKey() == ModuleTracker.getInstance().getByClass(ClickGUIModule.class).getKeybinds().getFirst()) {
                        MinecraftClient.getInstance().setScreen(getByClass(JelloClickScreen.class));
                        ModuleTracker.getInstance().getByClass(ClickGUIModule.class).setEnabled(true);
                    }
                }
                case "Panel" -> {
                    if (event.getKey() == ModuleTracker.getInstance().getByClass(ClickGUIModule.class).getKeybinds().getFirst()) {
                        MinecraftClient.getInstance().setScreen(getByClass(PanelClickScreen.class));
                        ModuleTracker.getInstance().getByClass(ClickGUIModule.class).setEnabled(true);
                    }
                }
            }
        });
    }
}
