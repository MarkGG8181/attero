package fag.ware.client.util.music;

import java.util.ArrayList;
import java.util.List;

public enum YoutubePlaylist {
    EMO("RDCLAK5uy_lyDluX1HPaTL5bX3T0cSc9X3bdqWmuK9M", "Emo"),
    ROCK("RDCLAK5uy_njvsGKIUycy_a4h7zTS8upbKhHcMVzHFM", "Rock"),
    EDM("RDCLAK5uy_n0oLcyKJhNW8BmrnMySAoVuLjRZfgozG0", "EDM"),
    POP("RDCLAK5uy_nDL8KeBrUagwyISwNmyEiSfYgz1gVCesg", "Pop"),
    LATIN("RDCLAK5uy_ntQM2JdqsobUzxHV4fPen-aWL_-JCCpsc", "Latin"),
    CHILL("RDCLAK5uy_lRr70m6uTvLpxAG3G6Yuc41cJBIXODAws", "Chill");

    public final String playlistId, name;
    public List<YoutubePlaylistParser.TrackInfo> tracks = new ArrayList<>();

    YoutubePlaylist(String playlistId, String name) {
        this.playlistId = playlistId;
        this.name = name;
    }
}
