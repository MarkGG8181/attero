package fag.ware.client.util.music;

import fag.ware.client.Fagware;

import java.util.ArrayList;
import java.util.List;

public enum SpotifyPlaylists {
    GOTH_MIX("/assets/" + Fagware.MOD_ID + "/spotify/goth_mix.html", "Goth mix"),
    PUNK_MIX("/assets/" + Fagware.MOD_ID + "/spotify/punk_mix.html", "Punk mix"),
    EDM_MIX("/assets/" + Fagware.MOD_ID + "/spotify/edm_mix.html", "EDM mix"),
    POP_MIX("/assets/" + Fagware.MOD_ID + "/spotify/pop_mix.html", "Pop mix"),
    LATIN_MIX("/assets/" + Fagware.MOD_ID + "/spotify/latin_mix.html", "Latin mix"),
    CHILL_MIX("/assets/" + Fagware.MOD_ID + "/spotify/chill_mix.html", "Chill mix");

    public final String path, name;
    public List<SpotifyPlaylistParser.TrackInfo> tracks = new ArrayList<>();

    SpotifyPlaylists(String path, String name) {
        this.path = path;
        this.name = name;
    }
}
