package io.github.client.tracker.impl;

import io.github.client.module.impl.combat.*;
import io.github.client.module.impl.misc.*;
import io.github.client.module.impl.movement.*;
import io.github.client.module.impl.player.*;
import io.github.client.module.impl.render.*;
import io.github.client.module.impl.world.CrystalAuraModule;
import io.github.client.module.impl.world.TimerModule;
import io.github.client.event.data.Subscribe;
import io.github.client.event.impl.interact.KeyEvent;
import io.github.client.file.impl.ModulesFile;
import io.github.client.module.AbstractModule;
import io.github.client.module.data.ModuleCategory;
import io.github.client.tracker.AbstractTracker;
import io.github.client.util.client.ConfigEntry;
import io.github.client.util.java.interfaces.IMinecraft;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ModuleTracker extends AbstractTracker<AbstractModule> implements IMinecraft {
    public AbstractModule lastModule;
    public final ModulesFile modulesFile = new ModulesFile("default");

    public static final ModuleTracker INSTANCE = new ModuleTracker();

    public ConfigEntry currentConfig;

    public final List<ConfigEntry> configs = new ArrayList<>();
    public String activeConfigName = null;

    @Override
    public void initialize() {
        super.initialize();

        /* COMBAT */
        list.add(new KillAuraModule());
        list.add(new TriggerBotModule());
        list.add(new VelocityModule());
        list.add(new AutoTotemModule());
        list.add(new AutoCrystalModule());
        list.add(new AimAssistModule());

        /* MOVEMENT */
        list.add(new SprintModule());
        list.add(new SpeedModule());
        list.add(new CorrectMovementModule());
        list.add(new LongJumpModule());
        list.add(new EagleModule());
        list.add(new InventoryMoveModule());
        list.add(new CounterStrafeModule());
        list.add(new JesusModule());
        list.add(new ParkourModule());

        /* RENDER */
        list.add(new NoRenderModule());
        list.add(new WatermarkModule());
        list.add(new ESPModule());
        list.add(new FullBrightModule());
        list.add(new ModuleListModule());
        list.add(new ClickGUIModule());
        list.add(new SwingAnimationsModule());

        /* PLAYER */
        list.add(new NoFallModule());
        list.add(new AutoDisconnectModule());
        list.add(new BlinkModule());
        list.add(new ScaffoldWalkModule());
        list.add(new MidClickPearlModule());
        list.add(new FastPlaceModule());
        list.add(new KeyFireworkModule());
        list.add(new InventoryManagerModule());

        /* WORLD */
        list.add(new TimerModule());
        list.add(new CrystalAuraModule());

        /* MISC */
        list.add(new AutoRespawnModule());
        list.add(new PacketCancellerModule());
        list.add(new MidClickFriendModule());
        list.add(new AntiTrapModule());
        list.add(new InventorySlotsModule());
        list.add(new DisablerModule());

        list.forEach(AbstractModule::onInit);
        modulesFile.load();
    }

    @Subscribe
    public void onKey(KeyEvent event) {
        if (mc.currentScreen == null)
            list.forEach(mod -> {
                if (mod.getKeybinds().contains(event.key)) {
                    mod.toggle();
                }
            });
    }

    public AbstractModule getByName(String name) {
        return list.stream()
                .filter(mod -> mod.toString().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    public Set<AbstractModule> getByCategory(ModuleCategory category) {
        return list.stream()
                .filter(mod -> mod.getInfo().category().equals(category))
                .collect(Collectors.toSet());
    }
}