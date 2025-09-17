package io.github.client.screen;

import imgui.ImGui;
import io.github.client.music.impl.PlaylistData;
import io.github.client.music.impl.SongData;
import net.minecraft.client.gui.DrawContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MusicScreen {
    public static final MusicScreen INSTANCE = new MusicScreen();

    private final Map<PlaylistData, List<SongData>> playlists = new HashMap<>();

    private PlaylistData currentPlaylist = null;

    public void init() {
        if (playlists.isEmpty())
            PlaylistData.fetch().forEach(playlist -> playlists.put(playlist, SongData.fetch(playlist)));
    }

    public void render(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        float sidebarWidth = 200.0f;
        float mainPageWidth = 400.0f;

        if (ImGui.begin("Music")) {
            if (ImGui.beginChild("Sidebar", sidebarWidth, 0, true)) {
                for (var entry : playlists.entrySet()) {
                    PlaylistData playlist = entry.getKey();

                    if (ImGui.button(playlist.playlistName, sidebarWidth - 10, 0)) {
                        currentPlaylist = playlist;
                    }

                    ImGui.spacing();
                }
                ImGui.endChild();
            }

            ImGui.sameLine();

            if (currentPlaylist != null && ImGui.beginChild("Playlist content", mainPageWidth, 0, true)) {
                ImGui.text(currentPlaylist.playlistName);

                ImGui.separator();

                for (var song : playlists.get(currentPlaylist)) {
                    ImGui.button(song.songName, mainPageWidth - 28, 0);
                }

                ImGui.endChild();
            }
        }
        ImGui.end();
    }
}