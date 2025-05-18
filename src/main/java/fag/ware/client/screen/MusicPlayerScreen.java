package fag.ware.client.screen;

import com.jfposton.ytdlp.YtDlpException;
import fag.ware.client.Fagware;
import fag.ware.client.screen.data.ImGuiImpl;

import fag.ware.client.util.music.YoutubePlaylistLoader;
import fag.ware.client.util.music.YoutubePlaylist;

import fag.ware.client.util.music.YoutubePlaylistParser;
import imgui.ImGui;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class MusicPlayerScreen extends Screen {
    public MusicPlayerScreen() {
        super(Text.of("Music Player"));
    }

    @Override
    protected void init() {
        super.init();
        try {
            YoutubePlaylistLoader.loadPlaylists(false);
        } catch (YtDlpException e) {
            Fagware.LOGGER.warn("Failed to load playlist", e);
        }
    }

    private YoutubePlaylistParser.TrackInfo currentTrack = null;

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        super.render(context, mouseX, mouseY, deltaTicks);

        ImGuiImpl.draw(io -> {
            ImGuiImpl.applyMarineTheme();
            if (ImGui.begin("Music Player")) {
                if (ImGui.beginTabBar("TABBAR")) {
                    for (YoutubePlaylist playlist : YoutubePlaylist.values()) {
                        if (ImGui.beginTabItem(playlist.name)) {
                            YoutubePlaylistLoader.loadPlaylistOnce(playlist);
                            if (ImGui.beginChild("TrackGrid", 0, 300, true)) {
                                var columns = 8;
                                var count = 0;

                                for (var track : playlist.tracks) {
                                    if (track.textureId != -1) {
                                        boolean press = ImGui.imageButton(track.textureId, 48, 48, 0, 1, 1, 0, 2);

                                        if (press) {
                                            currentTrack = track;
                                        }

                                        count++;
                                        if (count % columns != 0) {
                                            ImGui.sameLine();
                                        }
                                    }
                                }
                                ImGui.endChild();
                            }
                            ImGui.endTabItem();
                        }
                    }

                    ImGui.separator();

                    var startX = ImGui.getCursorPosX();
                    var startY = ImGui.getCursorPosY();

                    ImGui.image(currentTrack == null ? 3 : currentTrack.textureId, 100, 100, 0, 1, 1, 0);

                    ImGui.setCursorPosX(startX + 110);
                    ImGui.setCursorPosY(startY);
                    ImGui.text(currentTrack == null ? "Title" : currentTrack.title);

                    ImGui.setCursorPosX(startX + 110);
                    ImGui.setCursorPosY(startY + ImGui.getTextLineHeight());
                    ImGui.text(currentTrack == null ? "Author" : currentTrack.artist);

                    ImGui.setCursorPosX(startX + 110);
                    ImGui.setCursorPosY(startY + ImGui.getTextLineHeight() * 2);
                    ImGui.text("Duration: 00:00 - 03:00");

                    ImGui.endTabBar();
                }
            }
            ImGui.end();
        });
    }
}
