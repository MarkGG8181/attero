package fag.ware.client;

import fag.ware.client.event.data.EventBus;
import fag.ware.client.tracker.impl.*;
import fag.ware.client.util.FileUtil;
import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Fagware implements ModInitializer {
    public static final String MOD_ID = "fagware";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static final Fagware INSTANCE = new Fagware();
    public static final EventBus BUS = new EventBus();

    public final ModuleTracker moduleTracker = new ModuleTracker();
    public final ScreenTracker screenTracker = new ScreenTracker();
    public final CombatTracker combatTracker = new CombatTracker();
    public final PlayerTracker playerTracker = new PlayerTracker();

    @Override
    public void onInitialize() {
        FileUtil.createDir(MOD_ID);
    }

    public void onStartup() {
        LOGGER.info("Starting {}", MOD_ID);

        moduleTracker.initialize();
        screenTracker.initialize();
        combatTracker.initialize();
        playerTracker.initialize();
    }
}