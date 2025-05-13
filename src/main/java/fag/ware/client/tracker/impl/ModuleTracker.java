package fag.ware.client.tracker.impl;

import fag.ware.client.Fagware;
import fag.ware.client.event.data.Subscribe;
import fag.ware.client.event.impl.KeyEvent;
import fag.ware.client.module.Module;
import fag.ware.client.module.data.ModuleCategory;
import fag.ware.client.module.impl.combat.*;
import fag.ware.client.module.impl.movement.*;
import fag.ware.client.module.impl.player.*;
import fag.ware.client.module.impl.render.*;
import fag.ware.client.tracker.Tracker;
import fag.ware.client.util.IMinecraft;

import java.util.Set;
import java.util.stream.Collectors;

public class ModuleTracker extends Tracker<Module> implements IMinecraft {
    public Module lastModule;

    @Override
    public void initialize() {
        Fagware.BUS.register(this);

        getSet().add(new SprintModule());
        getSet().add(new KillAuraModule());
        getSet().add(new AntiBlindModule());
        getSet().add(new WatermarkModule());
        getSet().add(new SpeedModule());
        getSet().add(new CorrectMovementModule());
        getSet().add(new VelocityModule());
        getSet().add(new FastPlaceModule());
        getSet().add(new ModuleListModule());
        getSet().forEach(Module::onInit);
    }

    @Subscribe
    public void onKey(KeyEvent event) {
        if (mc.player != null && mc.world != null && mc.currentScreen == null)
            getSet().forEach(m -> {
                if (event.getKey() == m.getKeybind()) {
                    m.toggle();
                }
            });
    }

    public Module getByName(String name) {
        return getSet().stream()
                .filter(m -> m.getInfo().name().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    public Set<Module> getByCategory(ModuleCategory category) {
        return getSet().stream()
                .filter(m -> m.getInfo().category().equals(category))
                .collect(Collectors.toSet());
    }
}