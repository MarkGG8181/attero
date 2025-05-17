package fag.ware.client.tracker.impl;

import fag.ware.client.Fagware;
import fag.ware.client.event.data.Subscribe;
import fag.ware.client.event.impl.KeyEvent;
import fag.ware.client.module.AbstractModule;
import fag.ware.client.module.data.ModuleCategory;
import fag.ware.client.module.impl.combat.*;
import fag.ware.client.module.impl.misc.*;
import fag.ware.client.module.impl.movement.*;
import fag.ware.client.module.impl.player.*;
import fag.ware.client.module.impl.render.*;
import fag.ware.client.module.impl.world.*;
import fag.ware.client.tracker.AbstractTracker;
import fag.ware.client.util.interfaces.IMinecraft;

import java.util.Set;
import java.util.stream.Collectors;

public class ModuleTracker extends AbstractTracker<AbstractModule> implements IMinecraft {
    public AbstractModule lastModule;

    private static final ModuleTracker tracker = new ModuleTracker();

    public static ModuleTracker getInstance() {
        return tracker;
    }

    @Override
    public void initialize() {
        Fagware.BUS.register(this);

        /* COMBAT */
        getSet().add(new KillAuraModule());
        getSet().add(new TriggerBotModule());
        getSet().add(new VelocityModule());
        getSet().add(new AutoTotemModule());

        /* MOVEMENT */
        getSet().add(new SprintModule());
        getSet().add(new SpeedModule());
        getSet().add(new CorrectMovementModule());
        getSet().add(new LongJumpModule());
        getSet().add(new EagleModule());
        getSet().add(new InventoryMoveModule());
        getSet().add(new CounterStrafeModule());

        /* RENDER */
        getSet().add(new NoRenderModule());
        getSet().add(new WatermarkModule());
        getSet().add(new ESPModule());
        getSet().add(new FullBrightModule());
        getSet().add(new ModuleListModule());

        /* PLAYER */
        getSet().add(new NoFallModule());
        getSet().add(new FastPlaceModule());
        getSet().add(new AutoDisconnectModule());
        getSet().add(new BlinkModule());

        /* WORLD */
        getSet().add(new TimerModule());

        /* MISC */
        getSet().add(new AutoRespawnModule());
        getSet().add(new PacketCancellerModule());

        getSet().forEach(AbstractModule::onInit);
    }

    @Subscribe
    public void onKey(KeyEvent event) {
        if (mc.player != null && mc.world != null && mc.currentScreen == null)
            getSet().forEach(mod -> {
                if (mod.getKeybinds().contains(event.getKey())) {
                    mod.toggle();
                }
            });
    }

    public AbstractModule getByName(String name) {
        return getSet().stream()
                .filter(mod -> mod.getInfo().name().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    public Set<AbstractModule> getByCategory(ModuleCategory category) {
        return getSet().stream()
                .filter(mod -> mod.getInfo().category().equals(category))
                .collect(Collectors.toSet());
    }
}