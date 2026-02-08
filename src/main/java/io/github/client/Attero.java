package io.github.client;

import io.github.client.event.data.EventBus;
import io.github.client.imgui.ImGuiImpl;
import io.github.client.tracker.impl.*;
import io.github.client.util.java.FileUtil;
import io.github.client.util.java.GLFWUtil;
import net.fabricmc.api.ClientModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
        FileUtil.createDir("configs");
        FileUtil.createDir("fonts");
        FileUtil.createDir("music");
        FileUtil.createDir("music/cache");
    }

    public void onStartup() {
        LOGGER.info("Starting {}", MOD_ID);

        ScreenTracker.INSTANCE.initialize();
        MusicTracker.INSTANCE.initialize();
        ModuleTracker.INSTANCE.initialize();
        CommandTracker.INSTANCE.initialize();
        RotationTracker.INSTANCE.initialize();
    }

    public void onEnd() {
        LOGGER.info("Shutting down {}", MOD_ID);
        ModuleTracker.INSTANCE.modulesFile.save();
        FriendTracker.INSTANCE.friendsFile.save();
        MusicTracker.INSTANCE.stop();
        ImGuiImpl.dispose();
        GLFWUtil.clearCache();
    }
}