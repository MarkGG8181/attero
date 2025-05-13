package fag.ware.client.util.music;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class SpotifyPlaylistParser {

    public static class TrackInfo {
        public final String title;
        public final String artist;
        public final String url;
        public final String id;
        public BufferedImage bufferedImage;
        public int textureId = -1; // -1 means not uploaded yet

        public TrackInfo(String title, String artist, String id) {
            this.title = title;
            this.artist = artist;
            this.url = "https://open.spotify.com" + id;
            this.id = id.replaceAll("/track/", "");
        }

        @Override
        public String toString() {
            return title + " - " + artist + " (" + url + ")";
        }
    }

    public static List<TrackInfo> getTracksFromHtml(String html) {
        List<TrackInfo> tracks = new ArrayList<>();

        Document doc = Jsoup.parse(html);
        Elements rows = doc.select("div[role=row][aria-rowindex]");

        for (Element row : rows) {
            String rowIndex = row.attr("aria-rowindex");
            if ("1".equals(rowIndex)) continue; // skip header or invisible row

            Element trackLink = row.selectFirst("a[data-testid=internal-track-link]");
            Element titleEl = trackLink != null ? trackLink.selectFirst("div[data-encore-id=text]") : null;
            Element artistEl = row.selectFirst("div[data-testid=tracklist-row] a[href^=/artist/]");

            if (trackLink != null && titleEl != null && artistEl != null) {
                String url = trackLink.attr("href");
                String title = titleEl.text();
                String artist = artistEl.text();

                tracks.add(new TrackInfo(title, artist, url));
            }
        }

        return tracks;
    }
}