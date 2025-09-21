package io.github.client.screen;

import com.jfposton.ytdlp.YtDlp;
import com.jfposton.ytdlp.YtDlpException;
import com.jfposton.ytdlp.YtDlpRequest;
import com.jfposton.ytdlp.YtDlpResponse;
import imgui.ImGui;
import imgui.ImVec2;
import imgui.flag.ImGuiWindowFlags;
import io.github.client.Attero;
import io.github.client.music.impl.PlaylistData;
import io.github.client.music.impl.SongData;
import io.github.client.tracker.impl.ModuleTracker;
import io.github.client.tracker.impl.MusicTracker;
import io.github.client.util.java.GLFWUtil;
import io.github.client.util.java.math.MathUtil;
import net.minecraft.client.MinecraftClient;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SuppressWarnings("unused")
public class MusicScreen {

    public static final MusicScreen INSTANCE = new MusicScreen();

    private final Map<PlaylistData, List<SongData>> playlists = new HashMap<>();
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    private PlaylistData currentPlaylist = null;
    private SongData currentSong = null;

    private boolean playing;

    public void init() {
        executor.submit(() -> {
            try {
                if (playlists.isEmpty()) {
                    for (PlaylistData playlist : PlaylistData.fetch()) {
                        playlists.put(playlist, SongData.fetch(playlist));
                    }
                    final PlaylistData firstPlaylist = playlists.keySet().iterator().next();
                    currentPlaylist = firstPlaylist;
                    update(playlists.get(firstPlaylist).getFirst(), true);
                }
            } catch (Exception e) {
                Attero.LOGGER.error("Failed to initialize playlists", e);
            }
        });
    }

    public void render(int mouseX, int mouseY, float deltaTicks) {
        float sidebarWidth = 200.0f;
        float mainPageWidth = 400.0f;

        ImGui.setNextWindowSize(600, 530);

        if (ImGui.begin("Music", ImGuiWindowFlags.NoResize)) {
            if (!playlists.isEmpty() && ImGui.beginChild("Sidebar", sidebarWidth, 350, true)) {
                for (var entry : playlists.entrySet()) {
                    PlaylistData playlist = entry.getKey();

                    float availWidth = ImGui.getContentRegionAvailX();
                    if (ImGui.button(playlist.playlistName, availWidth, 0)) {
                        currentPlaylist = playlist;
                    }

                    ImGui.spacing();
                }
                ImGui.endChild();
            }

            ImGui.sameLine();

            if (currentPlaylist != null && ImGui.beginChild("Playlist content", 0, 350, true)) {
                ImGui.text(currentPlaylist.playlistName);
                ImGui.separator();

                float availWidth = ImGui.getContentRegionAvailX();

                for (var song : playlists.get(currentPlaylist)) {
                    if (ImGui.button(song.songName, availWidth, 0)) {
                        update(song, false);
                    }
                }

                ImGui.endChild();
            }

            if (currentSong != null && ImGui.beginChild("Currently playing", 0, 132, true, ImGuiWindowFlags.NoScrollbar | ImGuiWindowFlags.NoScrollWithMouse)) {
                ImGui.image(currentSong.texId, new ImVec2(85, 85));
                ImGui.sameLine();
                ImGui.text(currentSong.songName + "\nReleased " + MathUtil.formatTime(currentSong.publishDate));

                if (ImGui.button("Backward")) {
                    playPreviousSong();
                }

                ImGui.sameLine();

                if (ImGui.button(playing ? "Pause" : "Play")) {
                    play(false);
                }

                ImGui.sameLine();

                if (ImGui.button("Forward")) {
                    playNextSong();
                }

                ImGui.endChild();
            }
        }
        ImGui.end();
    }

    private void update(SongData song, boolean first) {
        MinecraftClient.getInstance().execute(() -> {
            this.currentSong = song;
            this.currentSong.texId = GLFWUtil.loadTextureFromUrl(song.songArtwork);
            play(first);
        });
    }

    private void play(boolean first) {
        if (first) return;

        if (playing) {
            playing = false;
            MusicTracker.INSTANCE.pause();
        } else {
            if (currentSong.url == null) {
                executor.submit(() -> {
                    try {
                        Path localPath = downloadSong(currentSong.youtubeUrl, removeFirst(SongData.YOUTUBE_URL, currentSong.youtubeUrl));
                        currentSong.url = localPath.toUri().toString();
                        Attero.LOGGER.debug("Downloaded & Cached: {} -> {}", currentSong.songName, currentSong.url);
                        MusicTracker.INSTANCE.play(currentSong.url);
                        playing = true;
                    } catch (Exception e) {
                        Attero.LOGGER.error("Failed to download/play {}", currentSong.songName, e);
                    }
                });
            } else {
                MusicTracker.INSTANCE.resume();
                playing = true;
            }
        }
    }

    private Path downloadSong(String youtubeUrl, String youtubeId) throws YtDlpException {
        Path outputPath = MusicTracker.MUSIC_CACHE_DIR.resolve(youtubeId + ".mp3");

        if (Files.exists(outputPath)) {
            return outputPath;
        }

        YtDlpRequest downloadRequest = new YtDlpRequest(youtubeUrl);
        downloadRequest.addOption("paths", MusicTracker.MUSIC_CACHE_DIR.toString());
        downloadRequest.addOption("output", "%(id)s.%(ext)s");
        downloadRequest.addOption("no-check-certificate");
        downloadRequest.addOption("prefer-insecure");
        downloadRequest.addOption("format", "bestaudio");
        downloadRequest.addOption("retries", 10);
        downloadRequest.addOption("extract-audio");
        downloadRequest.addOption("audio-format", "mp3");

        YtDlpResponse downloadResponse = YtDlp.execute(downloadRequest);

        return outputPath;
    }

    private String sanitize(String input) {
        return input.replaceAll("[^a-zA-Z0-9\\-_\\. ]", "_");
    }

    private int getCurrentSongIndex() {
        List<SongData> songs = playlists.get(currentPlaylist);
        return songs.indexOf(currentSong);
    }

    private void playNextSong() {
        if (currentPlaylist == null || currentSong == null) return;

        List<SongData> songs = playlists.get(currentPlaylist);
        int currentIndex = getCurrentSongIndex();

        if (currentIndex < songs.size() - 1) {
            update(songs.get(currentIndex + 1), false);
        }
    }

    private void playPreviousSong() {
        if (currentPlaylist == null || currentSong == null) return;

        List<SongData> songs = playlists.get(currentPlaylist);
        int currentIndex = getCurrentSongIndex();

        if (currentIndex > 0) {
            update(songs.get(currentIndex - 1), false);
        }
    }

    public static String removeFirst(String remove, String source) {
        if (remove == null || source == null || remove.isEmpty()) return source;
        int index = source.indexOf(remove);
        if (index == -1) return source;
        return source.substring(0, index) + source.substring(index + remove.length());
    }
}