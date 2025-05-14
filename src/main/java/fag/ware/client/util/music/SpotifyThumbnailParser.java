package fag.ware.client.util.music;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import fag.ware.client.Fagware;
import org.apache.commons.io.IOUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class SpotifyThumbnailParser {
    private static final String API_URL = "https://www.spotifycover.art/api/apee?inputType=tracks&id=";

    public static String getThumbnailURL(SpotifyPlaylistParser.TrackInfo track) {
        try {
            JsonObject json = getJson(new URL(API_URL + track.id));
            if (json.has("album")) {
                JsonObject album = json.get("album").getAsJsonObject();
                if (album.has("images")) {
                    JsonArray images = album.getAsJsonArray("images");
                    String url300 = null;

                    for (JsonElement imageElement : images) {
                        JsonObject imageObj = imageElement.getAsJsonObject();
                        int width = imageObj.get("width").getAsInt();
                        int height = imageObj.get("height").getAsInt();

                        if (width == 64 && height == 64) {
                            url300 = imageObj.get("url").getAsString();
                            break;
                        }
                    }

                    if (url300 != null) {
                        return url300;
                    }
                }
            }
        } catch (IOException ignored) {
        }

        return "https://i.scdn.co/image/ab67616d0000485118e4c2913a55fa0de4d2a0a5";
    }

    public static JsonObject getJson(URL url) throws IOException {
        String json = IOUtils.toString(url, StandardCharsets.UTF_8);
        return new JsonParser().parse(json).getAsJsonObject();
    }

    public static BufferedImage getBufferedImage(String urlStr) {
        try {
            URL imageUrl = new URL(urlStr);
            return ImageIO.read(imageUrl);
        } catch (Exception e) {
            Fagware.LOGGER.error("Failed to read image from: {}", urlStr, e);
        }
        return null;
    }
}