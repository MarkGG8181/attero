package io.github.client.music.impl;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.github.client.Attero;
import io.github.client.music.AbstractData;
import io.github.client.util.java.NetUtil;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PlaylistData extends AbstractData {
    public final String playlistName;
    public final String playlistArtwork;
    public final String publishDate;

    public PlaylistData(@NotNull JsonArray jsonArray, String playlistName, String playlistArtwork, String publishDate) {
        super(jsonArray);
        this.playlistName = playlistName;
        this.playlistArtwork = playlistArtwork;
        this.publishDate = publishDate;
    }

    public static final Map<String, String> PLAYLISTS = Map.of(
            "CALIFORNIA GIRLS EP", "https://wonderland.sigmaclient.cloud/clientbackend/attero/music/CALIFORNIA%20GIRLS%20EP.json",
            "crybaby", "https://wonderland.sigmaclient.cloud/clientbackend/attero/music/crybaby.json",
            "Rebel", "https://wonderland.sigmaclient.cloud/clientbackend/attero/music/Rebel.json"
    );

    public static List<PlaylistData> fetch() {
        List<PlaylistData> playlists = new ArrayList<>();

        for (var entry : PLAYLISTS.entrySet()) {
            String name = entry.getKey();
            String url = entry.getValue();

            try {
                JsonArray allSongs = NetUtil.fetchJson(url).getAsJsonArray();
                //there's multiple of these in the json, but we'll just get the first results
                JsonObject firstSong = allSongs.getAsJsonArray().get(0).getAsJsonObject();

                //we get stuff from the first song, because in most cases the first songs have the same
                //artwork as the playlist itself & publish date
                //ikik nasty, but hey, it works :shrug:
                String artwork = firstSong.get("artwork_url").getAsString();
                String date = firstSong.get("created_at").getAsString();

                Attero.LOGGER.debug("Found {} (created: {})", name, date);
                playlists.add(new PlaylistData(allSongs, name, artwork, date));
            } catch (IOException | InterruptedException e) {
                Attero.LOGGER.error("Failed to fetch playlist {} content", name, e);
            }
        }

        return playlists;
    }
}