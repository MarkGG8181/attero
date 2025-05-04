package fag.ware.client.tracker.impl;

import fag.ware.client.Fagware;
import fag.ware.client.event.data.Subscribe;
import fag.ware.client.event.impl.KeyEvent;
import fag.ware.client.module.Module;
import fag.ware.client.module.impl.combat.KillAuraModule;
import fag.ware.client.module.impl.movement.SprintModule;
import fag.ware.client.module.impl.render.AntiBlindModule;
import fag.ware.client.module.impl.render.WatermarkModule;
import fag.ware.client.tracker.Tracker;

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
}