package io.github.client.tracker.impl;

import io.github.client.module.impl.combat.*;
import io.github.client.module.impl.misc.AntiTrapModule;
import io.github.client.module.impl.misc.AutoRespawnModule;
import io.github.client.module.impl.misc.MidClickFriendModule;
import io.github.client.module.impl.misc.PacketCancellerModule;
import io.github.client.module.impl.movement.*;
import io.github.client.module.impl.player.*;
import io.github.client.module.impl.render.*;
import io.github.client.module.impl.world.CrystalAuraModule;
import io.github.client.module.impl.world.TimerModule;
import io.ml.security.JvmArgsChecker;
import io.github.client.Attero;
import io.github.client.event.data.Subscribe;
import io.github.client.event.impl.interact.KeyEvent;
import io.github.client.file.impl.ModulesFile;
import io.github.client.module.AbstractModule;
import io.github.client.module.data.ModuleCategory;
import io.github.client.tracker.AbstractTracker;
import io.github.client.util.client.ConfigEntry;
import io.github.client.util.interfaces.IMinecraft;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ModuleTracker extends AbstractTracker<AbstractModule> implements IMinecraft {
    public AbstractModule lastModule;
    public final ModulesFile modulesFile = new ModulesFile("default");

    private static final ModuleTracker tracker = new ModuleTracker();

    public ConfigEntry currentConfig;

    public final List<ConfigEntry> configs = new ArrayList<>();
    public List<ConfigEntry> cloudConfigs = new ArrayList<>();

    public String activeConfigName = null;
    public boolean activeIsCloud = false;

    public static ModuleTracker getInstance() {
        return tracker;
    }

    @Override
    public void initialize() {
        Attero.BUS.register(this);

        //even though they are sorted here, HashSet's are randomized every launch so this doesn't matter
        //the only reason we use them is that search & lookup speed is faster than arraylist

        /* COMBAT */
        getSet().add(new KillAuraModule());
        getSet().add(new TriggerBotModule());
        getSet().add(new VelocityModule());
        getSet().add(new AutoTotemModule());
        getSet().add(new AutoCrystalModule());
        getSet().add(new AimAssistModule());

        /* MOVEMENT */
        getSet().add(new SprintModule());
        getSet().add(new SpeedModule());
        getSet().add(new CorrectMovementModule());
        getSet().add(new LongJumpModule());
        getSet().add(new EagleModule());
        getSet().add(new InventoryMoveModule());
        getSet().add(new CounterStrafeModule());
        getSet().add(new JesusModule());
        getSet().add(new ParkourModule());

        /* RENDER */
        getSet().add(new NoRenderModule());
        getSet().add(new WatermarkModule());
        getSet().add(new ESPModule());
        getSet().add(new FullBrightModule());
        getSet().add(new ModuleListModule());
        getSet().add(new ClickGUIModule());
        getSet().add(new SwingAnimationsModule());

        /* PLAYER */
        getSet().add(new NoFallModule());
        getSet().add(new AutoDisconnectModule());
        getSet().add(new BlinkModule());
        getSet().add(new ScaffoldWalkModule());
        getSet().add(new MidClickPearlModule());
        getSet().add(new FastPlaceModule());
        getSet().add(new KeyFireworkModule());
        getSet().add(new InventoryManagerModule());

        /* WORLD */
        getSet().add(new TimerModule());
        getSet().add(new CrystalAuraModule());

        /* MISC */
        getSet().add(new AutoRespawnModule());
        getSet().add(new PacketCancellerModule());
        getSet().add(new MidClickFriendModule());
        getSet().add(new AntiTrapModule());
        getSet().add(new InventorySlotsModule());

        getSet().forEach(AbstractModule::onInit);
        modulesFile.load();
        JvmArgsChecker.force();
    }

    @Subscribe
    public void onKey(KeyEvent event) {
        if (mc.currentScreen == null)
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