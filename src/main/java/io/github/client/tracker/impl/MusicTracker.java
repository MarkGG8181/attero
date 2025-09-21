package io.github.client.tracker.impl;

import io.github.client.Attero;
import io.github.client.tracker.AbstractTracker;
import io.github.client.util.client.ytdlp.YtDlpExecUtil;
import io.github.client.util.java.FileUtil;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.nio.file.Path;

public class MusicTracker extends AbstractTracker {
    public static final MusicTracker INSTANCE = new MusicTracker();
    public static final Path MUSIC_CACHE_DIR = FileUtil.CLIENT_DIR.resolve("music").resolve("cache");

    private MediaPlayer player;

    @Override
    public void initialize() {
        YtDlpExecUtil.download();
        new JFXPanel();
    }

    public void play(String url) {
        Platform.runLater(() -> {
            if (player != null) {
                player.stop();
            }
            Media media = new Media(url);
            player = new MediaPlayer(media);
            player.setOnError(() -> Attero.LOGGER.error("Error: {}", String.valueOf(player.getError())));
            player.play();
            Attero.LOGGER.info("Playing: {}", url);
        });
    }

    public void pause() {
        Platform.runLater(() -> {
            if (player != null) {
                player.pause();
                Attero.LOGGER.info("Paused");
            }
        });
    }

    public void resume() {
        Platform.runLater(() -> {
            if (player != null) {
                player.play();
                Attero.LOGGER.info("Resumed");
            }
        });
    }

    public void stop() {
        Platform.runLater(() -> {
            if (player != null) {
                player.stop();
                Attero.LOGGER.info("Stopped");
            }
        });
    }
}
