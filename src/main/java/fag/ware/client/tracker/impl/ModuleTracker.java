package fag.ware.client.tracker.impl;

import fag.ware.client.Fagware;
import fag.ware.client.event.data.Subscribe;
import fag.ware.client.event.impl.KeyEvent;
import fag.ware.client.module.Module;
import fag.ware.client.module.data.ModuleCategory;
import fag.ware.client.module.impl.combat.KillAuraModule;
import fag.ware.client.module.impl.movement.SprintModule;
import fag.ware.client.module.impl.render.AntiBlindModule;
import fag.ware.client.module.impl.render.WatermarkModule;
import fag.ware.client.tracker.Tracker;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ModuleTracker extends Tracker<Module> {
    @Override
    public void initialize() {
        Fagware.BUS.register(this);

        getSet().add(new SprintModule());
        getSet().add(new KillAuraModule());
        getSet().add(new AntiBlindModule());
        getSet().add(new WatermarkModule());

        getSet().forEach(Module::onInit);
    }

    @Subscribe
    public void onKey(KeyEvent event) {
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