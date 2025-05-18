package fag.ware.client.util.music;

import com.jfposton.ytdlp.YtDlpException;
import fag.ware.client.Fagware;
import fag.ware.client.screen.data.ImGuiImpl;
import net.minecraft.client.MinecraftClient;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class YoutubePlaylistLoader {
    private static final Set<YoutubePlaylist> loadedPlaylists = new HashSet<>();

    public static void loadPlaylistOnce(YoutubePlaylist playlist) {
        if (loadedPlaylists.contains(playlist)) return;
        loadedPlaylists.add(playlist);
        loadPlaylist(playlist, true);
    }

    public static void loadPlaylists(boolean opengl) throws YtDlpException {
        for (var playlist : YoutubePlaylist.values()) {
            loadPlaylist(playlist, opengl);
        }
    }

    public static void loadPlaylist(YoutubePlaylist playlist, boolean opengl) {
        if (playlist.tracks.isEmpty() || opengl) {
            String playlistUrl = "https://www.youtube.com/playlist?list=" + playlist.playlistId;

            if (!opengl) {
                CompletableFuture.supplyAsync(() -> {
                    try {
                        return YoutubePlaylistParser.getTracksFromYtDlp(playlistUrl);
                    } catch (YtDlpException e) {
                        Fagware.LOGGER.error("Failed to load playlist tracks", e);
                        return List.<YoutubePlaylistParser.TrackInfo>of();
                    }
                }).thenAccept(tracks -> {
                    playlist.tracks = tracks;

                    for (var track : tracks) {
                        loadTrackImageAsync(track);
                    }
                });
            }

            if (opengl) {
                for (var track : playlist.tracks) {
                    loadTrackImageAsyncTexture(track);
                }
            }
        }
    }

    public static void loadTrackImageAsync(YoutubePlaylistParser.TrackInfo track) {
        CompletableFuture.runAsync(() -> {
            try {
                track.bufferedImage = YoutubeThumbnailParser.getBufferedImage(
                        YoutubeThumbnailParser.getThumbnailURL(track)
                );
            } catch (Exception e) {
                Fagware.LOGGER.error("Failed to load image for track: {}", track.title, e);
            }
        });
    }

    public static void loadTrackImageAsyncTexture(YoutubePlaylistParser.TrackInfo track) {
        CompletableFuture.runAsync(() -> MinecraftClient.getInstance().execute(() -> {
            try {
                track.textureId = ImGuiImpl.loadTexture(track.bufferedImage);
            } catch (Exception e) {
                Fagware.LOGGER.error("Failed to load image for track: {} {}", track.title, track.url, e);
            }
        }));
    }
}