package fag.ware.client.util.music;

import fag.ware.client.Fagware;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.net.URL;

public class YoutubeThumbnailParser {
    public static String getThumbnailURL(YoutubePlaylistParser.TrackInfo track) {
        return "https://i3.ytimg.com/vi/" + track.id + "/maxresdefault.jpg";
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