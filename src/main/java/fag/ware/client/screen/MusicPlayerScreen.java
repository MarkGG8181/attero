package fag.ware.client.screen;

import fag.ware.client.screen.data.ImGuiImpl;
import fag.ware.client.util.music.SpotifyLoader;
import fag.ware.client.util.music.SpotifyPlaylists;
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
        SpotifyLoader.loadPlaylists(false);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        super.render(context, mouseX, mouseY, deltaTicks);

        ImGuiImpl.draw(io -> {
            ImGuiImpl.applyMarineTheme();
            if (ImGui.begin("Music Player")) {
                if (ImGui.beginTabBar("TABBAR")) {
                    for (SpotifyPlaylists playlist : SpotifyPlaylists.values()) {
                        if (ImGui.beginTabItem(playlist.name)) {
                            SpotifyLoader.loadPlaylistOnce(playlist);
                            if (ImGui.beginChild("TrackGrid", 0, 300, true)) {
                                int columns = 8;
                                int count = 0;

                                for (var track : playlist.tracks) {
                                    if (track.textureId != -1) {
                                        ImGui.imageButton(track.textureId, 48, 48);

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

                    float startX = ImGui.getCursorPosX();
                    float startY = ImGui.getCursorPosY();

                    ImGui.image(3, 100, 100);

                    ImGui.setCursorPosX(startX + 110);
                    ImGui.setCursorPosY(startY);
                    ImGui.text("Title");

                    ImGui.setCursorPosX(startX + 110);
                    ImGui.setCursorPosY(startY + ImGui.getTextLineHeight());
                    ImGui.text("Author");

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
