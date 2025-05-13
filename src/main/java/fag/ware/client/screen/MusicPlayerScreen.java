package fag.ware.client.screen;

import fag.ware.client.screen.data.ImGuiImpl;
import fag.ware.client.util.music.SpotifyPlaylistParser;
import fag.ware.client.util.music.SpotifyPlaylists;
import fag.ware.client.util.music.SpotifyThumbnailParser;
import imgui.ImGui;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class MusicPlayerScreen extends Screen {
    public MusicPlayerScreen() {
        super(Text.of("Music Player"));

        for (var playlist : SpotifyPlaylists.values()) {
            loadPlaylist(playlist);
        }
    }

    private void loadPlaylist(SpotifyPlaylists playlist) {
        try (InputStream in = getClass().getResourceAsStream(playlist.url)) {
            if (in != null) {
                String html = new String(in.readAllBytes(), StandardCharsets.UTF_8);
                List<SpotifyPlaylistParser.TrackInfo> tracks = SpotifyPlaylistParser.getTracksFromHtml(html);
                playlist.tracks = tracks;

                // Load images asynchronously for each track
                for (var track : tracks) {
                    loadTrackImageAsync(track);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load playlist: " + playlist.name, e);
        }
    }

    private void loadTrackImageAsync(SpotifyPlaylistParser.TrackInfo track) {
        CompletableFuture.runAsync(() -> {
            try {
                track.bufferedImage = SpotifyThumbnailParser.getScaledImageFromUrl(
                        SpotifyThumbnailParser.getThumbnailURL(track), 300, 300
                );

                MinecraftClient.getInstance().execute(() -> {
                    try {
                        track.textureId = ImGuiImpl.fromBufferedImage(track.bufferedImage);
                    } catch (Exception e) {
                        System.err.println("Failed to load texture for track: " + track.title);
                        e.printStackTrace();
                    }
                });
            } catch (Exception e) {
                System.err.println("Failed to load image for track: " + track.title);
                e.printStackTrace();
            }
        });
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        super.render(context, mouseX, mouseY, deltaTicks);

        ImGuiImpl.draw(io -> {
            if (ImGui.begin("Music Player")) {
                if (ImGui.beginTabBar("TABBAR")) {
                    for (SpotifyPlaylists playlist : SpotifyPlaylists.values()) {
                        if (ImGui.beginTabItem(playlist.name)) {
                            if (ImGui.beginChild("TrackGrid", 0, 300, true)) { // 300 is height
                                int columns = 8;
                                int count = 0;

                                for (var track : playlist.tracks) {
                                    if (track.textureId != -1) {
                                        ImGui.image(track.textureId, 48, 48);

                                        count++;
                                        if (count % columns != 0) {
                                            ImGui.sameLine();
                                        }
                                    }
                                }
                                ImGui.endChild();
                            }
                            ImGui.endTabItem();
                        }
                    }

                    ImGui.separator();

                    float startX = ImGui.getCursorPosX();
                    float startY = ImGui.getCursorPosY();

                    ImGui.image(3, 100, 100);

                    ImGui.setCursorPosX(startX + 110);
                    ImGui.setCursorPosY(startY);
                    ImGui.text("Title");

                    ImGui.setCursorPosX(startX + 110);
                    ImGui.setCursorPosY(startY + ImGui.getTextLineHeight());
                    ImGui.text("Author");

                    ImGui.setCursorPosX(startX + 110);
                    ImGui.setCursorPosY(startY + ImGui.getTextLineHeight() * 2);
                    ImGui.text("Duration: 00:00 - 03:00");

                    ImGui.endTabBar();
                }
            }
            ImGui.end();
        });
    }
}
