package fag.ware.client.util.music;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.jfposton.ytdlp.YtDlp;
import com.jfposton.ytdlp.YtDlpException;
import com.jfposton.ytdlp.YtDlpRequest;
import com.jfposton.ytdlp.YtDlpResponse;
import fag.ware.client.Fagware;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class YoutubePlaylistParser {

    private static final Gson GSON = new Gson();

    public static class TrackInfo {
        public final String title;
        public final String artist;
        public final String url;
        public final String id;
        public BufferedImage bufferedImage;
        public int textureId = -1;

        public TrackInfo(String title, String artist, String id) {
            this.title = title;
            this.artist = artist;
            this.id = id;
            this.url = "https://www.youtube.com/watch?v=" + id;
        }

        @Override
        public String toString() {
            return title + " - " + artist + " (" + url + ")";
        }
    }

    public static List<TrackInfo> getTracksFromYtDlp(String playlistUrl) throws YtDlpException {
        List<TrackInfo> tracks = new ArrayList<>();
        YtDlpRequest request = new YtDlpRequest(playlistUrl, System.getProperty("java.io.tmpdir"));
        request.setOption("flat-playlist");
        request.setOption("dump-json");
        request.setOption("ignore-errors");

        YtDlpResponse response = YtDlp.execute(request);
        String output = response.getOut();

        if (output == null || output.isEmpty()) return tracks;

        for (String line : output.split("\\R")) {
            try {
                JsonObject obj = GSON.fromJson(line, JsonObject.class);
                String id = getAsStringOrDefault(obj, "id", null);
                if (id == null) {
                    continue; // skip invalid
                }

                String title = getAsStringOrDefault(obj, "title", "Unknown Title");
                String artist = getAsStringOrDefault(obj, "uploader", "Unknown Artist");

                tracks.add(new TrackInfo(title, artist, id));
            } catch (Exception e) {
                Fagware.LOGGER.error("Failed to get track", e);
            }
        }

        return tracks;
    }

    private static String getAsStringOrDefault(JsonObject obj, String key, String defaultValue) {
        if (!obj.has(key)) return defaultValue;
        JsonElement elem = obj.get(key);
        if (elem == null || elem.isJsonNull()) return defaultValue;
        try {
            return elem.getAsString();
        } catch (Exception e) {
            return defaultValue;
        }
    }
}