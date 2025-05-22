package fag.ware.client;

import fag.ware.client.event.data.EventBus;
import fag.ware.client.screen.data.ImGuiImpl;
import fag.ware.client.tracker.impl.*;
import fag.ware.client.util.FileUtil;
import net.fabricmc.api.ClientModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public final class Fagware implements ClientModInitializer {

    public static final String MOD_ID = "fagware";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static final Fagware INSTANCE = new Fagware();
    public static final EventBus BUS = new EventBus();

    @Override
    public void onInitializeClient() {
        FileUtil.createDir(MOD_ID);
        FileUtil.createDir(MOD_ID + File.separator + "configs");
    }

    public void onStartup() {
        LOGGER.info("Starting {}", MOD_ID);

        ModuleTracker.getInstance().initialize();
        CommandTracker.getInstance().initialize();
        ScreenTracker.getInstance().initialize();
        CombatTracker.getInstance().initialize();
        PlayerTracker.getInstance().initialize();
    }

    public void onEnd() {
        ImGuiImpl.dispose();
        ModuleTracker.getInstance().modulesFile.save(); //default config
        FriendTracker.getInstance().friendsFile.save();
    }
}