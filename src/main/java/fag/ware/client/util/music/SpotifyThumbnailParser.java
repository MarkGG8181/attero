package fag.ware.client.util.music;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.io.IOUtils;

import javax.imageio.ImageIO;
import java.awt.*;
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

                        if (width == 300 && height == 300) {
                            url300 = imageObj.get("url").getAsString();
                            System.out.println(url300);
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

        return "https://i.scdn.co/image/ab67616d00001e02d9380864539fcb5522506d7c";
    }

    public static JsonObject getJson(URL url) throws IOException {
        String json = IOUtils.toString(url, StandardCharsets.UTF_8);
        return new JsonParser().parse(json).getAsJsonObject();
    }

    public static BufferedImage getScaledImageFromUrl(String urlStr, int width, int height) throws IOException {
        URL url = new URL(urlStr);
        BufferedImage original = ImageIO.read(url);

        if (original == null) {
            throw new IOException("Failed to read image from URL: " + urlStr);
        }

        Image scaled = original.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        BufferedImage resized = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = resized.createGraphics();
        g2d.drawImage(scaled, 0, 0, null);
        g2d.dispose();

        return resized;
    }
}