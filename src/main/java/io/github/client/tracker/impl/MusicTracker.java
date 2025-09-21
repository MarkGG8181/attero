package io.github.client.tracker.impl;

import io.github.client.Attero;
import io.github.client.tracker.AbstractTracker;
import io.github.client.util.client.ytdlp.YtDlpExecUtil;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.net.URI;
import java.net.URISyntaxException;

public class MusicTracker extends AbstractTracker {
    public static final MusicTracker INSTANCE = new MusicTracker();

    private MediaPlayer player;

    @Override
    public void initialize() {
        YtDlpExecUtil.download();
        new JFXPanel();
    }

    public void play(String url) {
        Platform.runLater(() -> {
            try {
                if (player != null) {
                    player.stop();
                }
                URI safeUri = new URI(url);
                Media media = new Media(safeUri.toASCIIString());
                player = new MediaPlayer(media);
                player.setOnError(() -> Attero.LOGGER.error("Error: {}", String.valueOf(player.getError())));
                player.play();
                Attero.LOGGER.info("Playing: {}", url);
            } catch (URISyntaxException e) {
                Attero.LOGGER.error("Invalid media URL: {}", url, e);
            }
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
