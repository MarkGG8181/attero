package fag.ware.client.screen;

import fag.ml.packet.impl.CLoadConfigPacket;
import fag.ware.client.screen.data.ImGuiImpl;
import fag.ware.client.tracker.impl.AuthTracker;
import fag.ware.client.util.SystemUtil;
import imgui.ImGui;
import imgui.flag.ImGuiInputTextFlags;
import imgui.type.ImString;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class LoginScreen extends Screen {
    public LoginScreen() {
        super(Text.of("Login"));
    }

    private final ImString username = new ImString(System.getProperty("user.name"), 20);

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        super.render(context, mouseX, mouseY, deltaTicks);

        ImGuiImpl.draw(io -> {
            if (ImGui.begin("Authentication")) {
                ImGui.inputText("Username", username);

                if (ImGui.button("Login")) {
                    try {
                        AuthTracker.getInstance().initialize();
                        AuthTracker.getInstance().authenticate(username.get(), SystemUtil.getHWID());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            ImGui.end();
        });
    }
}
