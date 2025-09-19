package io.github.client.music.impl;

import com.google.gson.JsonObject;
import io.github.client.Attero;
import io.github.client.music.AbstractData;
import io.github.client.util.java.NetUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PlaylistData extends AbstractData {
    public final String playlistName;
    public final String publishDate;

    public PlaylistData(@NotNull JsonObject jsonObject, String playlistName, String publishDate) {
        super(jsonObject);
        this.playlistName = playlistName;
        this.publishDate = publishDate;
    }

    public static final Map<String, String> PLAYLISTS = Map.of(
            "NCS", "https://music.youtube.com/playlist?list=PLRBp0Fe2Gpgn8Y9qI-p0aTxVtw8onBSFj",
            "EDM", "https://music.youtube.com/playlist?list=PLM2V-zC1RSte9rMMAN_GxAf_6JNjVfp7B",
            "VEVO", "https://music.youtube.com/playlist?list=PLesm76O8GFZOpPPwQtFA5X5Q-Q694_DNU",
            "Pop", "https://music.youtube.com/playlist?list=PLYRb7SBNlT3aVKHWdoymWKx9js3hdvVhQ",
            "Emo", "https://music.youtube.com/playlist?list=PLYdzwhiBcly38vwunWGC2LqKcs4gvMyg9",
            "Rock", "https://music.youtube.com/playlist?list=PLOoXD-Y3d6-ymC_4tZbKxzpyPyZtW9mnc",
            "Alternative", "https://music.youtube.com/playlist?list=PLDwYaoLoi7Sq_4kZgE4v_c3vQYte-1reM"
    );

    private static final String api = "https://wonderland.sigmaclient.cloud/clientbackend/attero/music/yt.php?name=";

    public static List<PlaylistData> fetch() {
        List<PlaylistData> playlists = new ArrayList<>();

        try {
            for (var entry : PLAYLISTS.entrySet()) {
                String name = entry.getKey();

                JsonObject object = NetUtil.fetchJson(api + name).getAsJsonObject();
                playlists.add(new PlaylistData(object, name, "00/00/0000"));
            }
        } catch (Exception e) {
            Attero.LOGGER.error("Failed to fetch playlists", e);
        }

        return playlists;
    }
}