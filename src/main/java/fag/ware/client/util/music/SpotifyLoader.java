package fag.ware.client.util.music;

import fag.ware.client.Fagware;
import fag.ware.client.screen.data.ImGuiImpl;
import net.minecraft.client.MinecraftClient;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class SpotifyLoader {
    private static final Set<SpotifyPlaylists> loadedPlaylists = new HashSet<>();

    public static void loadPlaylistOnce(SpotifyPlaylists playlist) {
        if (loadedPlaylists.contains(playlist)) return;

        loadedPlaylists.add(playlist);
        loadPlaylist(playlist, true);
    }

    public static void loadPlaylists(boolean opengl) {
        for (var playlist : SpotifyPlaylists.values()) {
            loadPlaylist(playlist, opengl);
        }
    }

    public static void loadPlaylist(SpotifyPlaylists playlist, boolean opengl) {
        if (playlist.tracks.isEmpty() || opengl) {
            try (InputStream in = SpotifyLoader.class.getResourceAsStream(playlist.path)) {
                if (in != null) {
                    String html = new String(in.readAllBytes(), StandardCharsets.UTF_8);
                    if (!opengl) {
                        List<SpotifyPlaylistParser.TrackInfo> tracks = SpotifyPlaylistParser.getTracksFromHtml(html);
                        playlist.tracks = tracks;

                        // Load images asynchronously for each track
                        for (var track : tracks) {
                            loadTrackImageAsync(track);
                        }
                    }

                    if (opengl) {
                        for (var track : playlist.tracks) {
                            loadTrackImageAsyncTexture(track);
                        }
                    }
                }
            } catch (IOException e) {
                Fagware.LOGGER.error("Failed to load playlist {}", playlist.name, e);
            }
        }
    }

    public static void loadTrackImageAsync(SpotifyPlaylistParser.TrackInfo track) {
        CompletableFuture.runAsync(() -> {
            try {
                track.bufferedImage = SpotifyThumbnailParser.getBufferedImage(
                        SpotifyThumbnailParser.getThumbnailURL(track)
                );
            } catch (Exception e) {
                Fagware.LOGGER.error("Failed to load image for track: {}", track.title, e);
            }
        });
    }

    public static void loadTrackImageAsyncTexture(SpotifyPlaylistParser.TrackInfo track) {
        CompletableFuture.runAsync(() -> MinecraftClient.getInstance().execute(() -> {
            try {
                track.textureId = ImGuiImpl.fromBufferedImage(track.bufferedImage);
            } catch (Exception e) {
                Fagware.LOGGER.error("Failed to load image for track: {} {}", track.title, track.url, e);
            }
        }));
    }
}