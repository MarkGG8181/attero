package fag.ware.client.util.music;

import javazoom.jl.decoder.Bitstream;
import javazoom.jl.decoder.Decoder;
import javazoom.jl.decoder.Header;
import javazoom.jl.player.AudioDevice;
import javazoom.jl.player.Player;

import java.io.BufferedInputStream;
import java.net.URL;
import java.net.URLConnection;

public class AudioPlayer {
    private Thread playbackThread;
    private Player player;
    private BufferedInputStream stream;
    private boolean isPaused = false;
    private boolean stopRequested = false;
    private int currentFrame = 0;
    private int skipToFrame = 0;
    private Bitstream bitstream;
    private Decoder decoder;
    private AudioDevice audioDevice;

    public synchronized void play(String url) throws Exception {
        stop(); // stop current playback if any

        URLConnection conn = new URL(url).openConnection();
        stream = new BufferedInputStream(conn.getInputStream());

        stopRequested = false;
        isPaused = false;

        bitstream = new Bitstream(stream);
        decoder = new Decoder();
        audioDevice = javazoom.jl.player.FactoryRegistry.systemRegistry().createAudioDevice();
        audioDevice.open(decoder);

        currentFrame = 0;

        playbackThread = new Thread(() -> {
            try {
                while (!stopRequested) {
                    Header header = bitstream.readFrame();
                    if (header == null) break;

                    if (currentFrame >= skipToFrame) {
                        // Decode and play frame
                        var sampleBuffer = (javazoom.jl.decoder.SampleBuffer) decoder.decodeFrame(header, bitstream);
                        audioDevice.write(sampleBuffer.getBuffer(), 0, sampleBuffer.getBufferLength());
                    }
                    bitstream.closeFrame();

                    currentFrame++;
                }
                audioDevice.flush();
                audioDevice.close();
                bitstream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        playbackThread.start();
    }

    public int getCurrentFrame() {
        return currentFrame;
    }

    public void pause() {
        if (player != null) {
            stopRequested = true;
            isPaused = true;
            player.close();
            // store the frame where it stopped
            currentFrame = skipToFrame;
        }
    }

    public void resume() throws Exception {
        if (isPaused) {
            skipToFrame = currentFrame;
            playFromFrame(skipToFrame);
            isPaused = false;
        }
    }

    private void playFromFrame(int frame) throws Exception {
        stream.reset(); // reset to beginning
        player = new Player(stream);
        player.play(frame);
    }

    public void stop() {
        stopRequested = true;
        if (player != null) {
            player.close();
        }
        if (playbackThread != null && playbackThread.isAlive()) {
            playbackThread.interrupt();
        }
    }

    public void seek(int frame) throws Exception {
        skipToFrame = frame;
        stop();
        playFromFrame(frame);
    }
}