package io.github.client.music.impl;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.github.client.music.AbstractData;

import java.util.ArrayList;
import java.util.List;

public class SongData extends AbstractData {
    public final String songName;
    public final String songArtwork;
    public final String publishDate;

    public SongData(JsonArray jsonArray, String songName, String songArtwork, String publishDate) {
        super(jsonArray);
        this.songName = songName;
        this.songArtwork = songArtwork;
        this.publishDate = publishDate;
    }

    public static List<SongData> fetch(PlaylistData playlist) {
        List<SongData> songs = new ArrayList<>();

        JsonArray allSongs = playlist.jsonArray;

        allSongs.forEach(songElement -> {
            JsonObject song = songElement.getAsJsonObject();

            if (song != null) {
                String name = song.get("title").getAsString();
                String artwork = song.get("artwork_url").getAsString();
                String date = song.get("display_date").getAsString();

                songs.add(new SongData(null, name, artwork, date));
            }
        });

        return songs;
    }
}