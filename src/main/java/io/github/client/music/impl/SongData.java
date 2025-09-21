package io.github.client.music.impl;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.github.client.Attero;
import io.github.client.music.AbstractData;

import java.util.ArrayList;
import java.util.List;

public class SongData extends AbstractData {
    public final String songName;
    public final String songArtwork;
    public final String publishDate;

    public int texId = 3;

    public SongData(JsonObject jsonObject, String songName, String songArtwork, String publishDate) {
        super(jsonObject);
        this.songName = songName;
        this.songArtwork = songArtwork;
        this.publishDate = publishDate;
    }

    public static List<SongData> fetch(PlaylistData playlist) {
        List<SongData> songs = new ArrayList<>();

        if (playlist.jsonObject == null) {
            return songs;
        }

        try {
            JsonArray items = playlist.jsonObject.getAsJsonArray("items");

            for (JsonElement item : items) {
                if (!item.isJsonObject()) continue;

                JsonObject itemObj = item.getAsJsonObject();
                JsonObject snippet = itemObj.getAsJsonObject("snippet");

                String title = snippet.getAsJsonPrimitive("title").getAsString();

                if (title.equalsIgnoreCase("Deleted video") || title.equalsIgnoreCase("Private video")) {
                    continue;
                }

                // Remove bracketed content: [ ... ] and ( ... )
                title = title.replaceAll("\\s*\\[[^\\]]*\\]", "")
                        .replaceAll("\\s*\\([^)]*\\)", "");

                // Remove featuring/ft/feat phrases (case-insensitive)
                title = title.replaceAll("(?i)\\s*(ft\\.?|feat\\.?|featuring)\\s+[^-–—|]+", "");

                title = title.replaceAll("(?i)Official Music Video", "").trim();
                title = title.replaceAll("(?i)Official Video", "").trim();
                title = title.replaceAll("(?i)Lyrics", "").trim();

                // Trim whitespace
                title = title.trim();
                //chatgpt genned these btw ^^^

                String publishedAt = snippet.getAsJsonPrimitive("publishedAt").getAsString();

                String artwork = "";
                JsonObject thumbnails = snippet.getAsJsonObject("thumbnails");

                if (thumbnails != null && thumbnails.has("default")) {
                    artwork = thumbnails.getAsJsonObject("default")
                            .getAsJsonPrimitive("url")
                            .getAsString();
                }

                songs.add(new SongData(itemObj, title, artwork, publishedAt));
            }
        } catch (Exception e) {
            Attero.LOGGER.error("Failed to fetch songs from {}", playlist.playlistName, e);
        }

        return songs;
    }
}