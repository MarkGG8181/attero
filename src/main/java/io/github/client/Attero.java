package io.github.client;

import io.github.client.event.data.EventBus;
import io.github.client.screen.data.ImGuiImpl;
import io.github.client.tracker.impl.*;
import io.github.client.util.java.FileUtil;
import net.fabricmc.api.ClientModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * @author markuss
 * @since 4/05/2025
 */
public final class Attero implements ClientModInitializer {
    public static final String MOD_ID = "attero";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static final Attero INSTANCE = new Attero();
    public static final EventBus BUS = new EventBus();

    @Override
    public void onInitializeClient() {
        FileUtil.createDir(MOD_ID);
        FileUtil.createDir(MOD_ID + File.separator + "configs");
    }

    public void onStartup() {
        LOGGER.info("Starting {}", MOD_ID);

        ScreenTracker.INSTANCE.initialize();
    }

    public void onEnd() {
        ImGuiImpl.dispose();
        AuthTracker.INSTANCE.shutdown();
        ModuleTracker.INSTANCE.modulesFile.save();
        FriendTracker.INSTANCE.friendsFile.save();
    }
}