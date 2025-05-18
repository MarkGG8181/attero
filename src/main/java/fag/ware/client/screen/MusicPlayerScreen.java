package fag.ware.client.screen;

import fag.ware.client.screen.data.ImGuiImpl;

import fag.ware.client.util.music.*;

import imgui.ImGui;
import imgui.flag.ImGuiWindowFlags;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;

public class MusicPlayerScreen extends Screen {
    public MusicPlayerScreen() {
        super(Text.of("Music Player"));
    }

    @Override
    protected void init() {
        super.init();
    }

    public YoutubeSong currentSong;
    public YoutubePlaylist currentPlaylist;
    public int textureId;

    public String getThumbnailURL(YoutubeSong song) {
        return "https://i.ytimg.com/vi/" + song.id + "/mqdefault.jpg";
    }

    public void updatePlaylist(YoutubePlaylist playlist) {
        if (playlist.tracks.isEmpty()) {
            new Thread(() -> playlist.tracks.addAll(Arrays.stream(YoutubeThumbnailParser.getVideosFromPlaylist(playlist.id)).toList())).start();
        }

        currentPlaylist = playlist;
    }

    public void updateVideo(YoutubeSong video) {
        try {
            BufferedImage image = ImageIO.read(new URL(getThumbnailURL(video)));
            BufferedImage thumbnail = image
                    .getSubimage(0, 0, image.getWidth(), image.getHeight());

            textureId = ImGuiImpl.convertBufferedImageToTexture(thumbnail);
            currentSong = video;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        super.render(context, mouseX, mouseY, deltaTicks);

        ImGuiImpl.draw(io -> {
            ImGuiImpl.applyMarineTheme();
            if (ImGui.begin("Music Player", ImGuiWindowFlags.NoDocking)) {
                ImGui.setWindowSize(684, 385);
                ImGui.beginChild("Sidebar", 150, 270, true);

                for (YoutubePlaylist playlist : YoutubePlaylist.values()) {
                    float fullWidth = ImGui.getContentRegionAvailX();
                    if (ImGui.button(playlist.name(), fullWidth, 0)) {
                        updatePlaylist(playlist);
                    }
                }

                ImGui.endChild();

                ImGui.sameLine();
                ImGui.beginChild("Content", 0, 270, true);

                if (currentPlaylist != null) {
                    for (YoutubeSong track : currentPlaylist.tracks) {
                        boolean selected = currentSong != null && currentSong.equals(track);
                        if (ImGui.selectable(track.title, selected)) {
                            updateVideo(track);
                        }
                    }
                } else {
                    ImGui.text("No playlist selected.");
                }

                ImGui.endChild();


                if (currentSong != null) {
                    ImGui.separator();
                    ImGui.image(textureId, 60, 60);
                    ImGui.sameLine();
                    ImGui.text(currentSong.title + "\n" + currentSong.artist);
                }

            }
            ImGui.end();
        });
    }
}
