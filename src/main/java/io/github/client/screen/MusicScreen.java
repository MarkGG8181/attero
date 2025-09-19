package io.github.client.screen;

import imgui.ImGui;
import imgui.flag.ImGuiWindowFlags;
import io.github.client.Attero;
import io.github.client.music.impl.PlaylistData;
import io.github.client.music.impl.SongData;
import io.github.client.util.java.math.MathUtil;

import java.awt.*;
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

    public void init() {
        executor.submit(() -> {
            try {
                if (playlists.isEmpty()) {
                    for (PlaylistData playlist : PlaylistData.fetch()) {
                        playlists.put(playlist, SongData.fetch(playlist));
                    }
                    final PlaylistData firstPlaylist = playlists.keySet().iterator().next();
                    currentPlaylist = firstPlaylist;
                    currentSong = playlists.get(firstPlaylist).getFirst();
                }
            } catch (Exception e) {
                Attero.LOGGER.error("Failed to initialize playlists", e);
            }
        });
    }

    public void render(int mouseX, int mouseY, float deltaTicks) {
        float sidebarWidth = 200.0f;
        float mainPageWidth = 400.0f;

        ImGui.setNextWindowSize(600, 500);

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
                        currentSong = song;
                    }
                }

                ImGui.endChild();
            }

            if (currentSong != null && ImGui.beginChild("Currently playing", 0, 100f, true, ImGuiWindowFlags.NoScrollbar | ImGuiWindowFlags.NoScrollWithMouse)) {
                ImGui.image(3, 85, 85);
                ImGui.sameLine();
                ImGui.text(currentSong.songName + "\nReleased " + MathUtil.formatTime(currentSong.publishDate));
                ImGui.endChild();
            }
        }
        ImGui.end();
    }
}