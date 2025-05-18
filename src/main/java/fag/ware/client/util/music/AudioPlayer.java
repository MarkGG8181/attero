package fag.ware.client.util.music;

import fag.ware.client.Fagware;
import javazoom.jl.decoder.Bitstream;
import javazoom.jl.decoder.Decoder;
import javazoom.jl.decoder.Header;
import javazoom.jl.player.AudioDevice;
import javazoom.jl.player.Player;

import java.io.BufferedInputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class AudioPlayer {
    private Player player;
    private Thread playerThread;
    private volatile boolean isPaused = false;
    private volatile boolean isStopped = false;
    private String currentUrl;

    public synchronized void play(String url) throws Exception {
        stop();
        currentUrl = url;
        isStopped = false;
        isPaused = false;

        URLConnection conn = new URL(url).openConnection();
        conn.setRequestProperty("User-Agent", "Mozilla/5.0");
        conn.setRequestProperty("Accept", "audio/*");

        Fagware.LOGGER.info("Connecting to audio stream...");
        Fagware.LOGGER.info("Content-Type: {}", conn.getContentType());

        BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
        player = new Player(bis);

        playerThread = new Thread(() -> {
            try {
                Fagware.LOGGER.info("Playback started");
                while (!isStopped && player.play(1)) {
                    if (isPaused) {
                        player.close();
                        while (isPaused && !isStopped) {
                            Thread.sleep(100);
                        }
                        if (!isStopped) {
                            // Recreate player for resume
                            URLConnection newConn = new URL(currentUrl).openConnection();
                            player = new Player(newConn.getInputStream());
                        }
                    }
                }
                Fagware.LOGGER.info("Playback completed");
            } catch (Exception e) {
                if (!(e instanceof InterruptedException)) {
                    Fagware.LOGGER.error("Playback error", e);
                }
            }
        });
        playerThread.start();
    }

    public void pause() {
        if (!isStopped && !isPaused) {
            Fagware.LOGGER.info("Pausing playback");
            isPaused = true;
        }
    }

    public void resume() {
        if (!isStopped && isPaused) {
            Fagware.LOGGER.info("Resuming playback");
            isPaused = false;
        }
    }

    public void stop() {
        Fagware.LOGGER.info("Stopping playback");
        isStopped = true;
        isPaused = false;
        if (player != null) {
            player.close();
        }
        if (playerThread != null) {
            playerThread.interrupt();
        }
    }

    public boolean isPlaying() {
        return !isStopped && !isPaused;
    }
}