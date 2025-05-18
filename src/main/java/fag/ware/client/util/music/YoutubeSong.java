package fag.ware.client.util.music;

import java.awt.image.BufferedImage;

public class YoutubeSong {
    public final String title;
    public final String artist;
    public final String url;
    public String id;
    public BufferedImage bufferedImage;
    public int textureId = -1;

    public YoutubeSong(String title, String artist, String id) {
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
