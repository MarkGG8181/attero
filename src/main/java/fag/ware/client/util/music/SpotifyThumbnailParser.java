package fag.ware.client.util.music;

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
    private static final String API_URL = "https://embed.spotify.com/oembed?url=spotify%3Atrack%3A";

    public static String getThumbnailURL(SpotifyPlaylistParser.TrackInfo track) {
        String url = "https://image-cdn-ak.spotifycdn.com/image/ab67616d00001e02379bfc899f2870b7f55f6aef";

        try {
            JsonObject json = getJson(new URL(API_URL + track.id));
            if (json.has("thumbnail_url")) {
                url = json.get("thumbnail_url").getAsString();
            }
        } catch (IOException e) {
            Fagware.LOGGER.error("Failed to get thumbnail url for {}", track.title, e);
        }

        return url;
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