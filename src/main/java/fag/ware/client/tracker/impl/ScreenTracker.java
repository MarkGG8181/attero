package fag.ware.client.tracker.impl;

import fag.ware.client.Fagware;
import fag.ware.client.event.data.Subscribe;
import fag.ware.client.event.impl.KeyEvent;
import fag.ware.client.module.impl.render.ClickGUIModule;
import fag.ware.client.screen.ClickScreen;
import fag.ware.client.screen.JelloClickScreen;
import fag.ware.client.screen.PanelClickScreen;
import fag.ware.client.tracker.AbstractTracker;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;

public class ScreenTracker extends AbstractTracker<Screen> {
    private static final ScreenTracker tracker = new ScreenTracker();

    public static ScreenTracker getInstance() {
        return tracker;
    }

    @Override
    public void initialize() {
        Fagware.BUS.register(this);
        getSet().add(new ClickScreen());
        getSet().add(new JelloClickScreen());
        getSet().add(new PanelClickScreen());
    }

    @Subscribe
    public void onKey(KeyEvent event) {
        getSet().forEach(s -> {
            switch (ModuleTracker.getInstance().getByClass(ClickGUIModule.class).mode.getValue()) {
                case "Fagware" -> {
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
