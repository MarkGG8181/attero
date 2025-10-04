package io.github.client.screen;

import imgui.flag.ImGuiInputTextFlags;
import io.github.client.Attero;
import io.github.client.imgui.ImGuiImpl;
import io.github.client.imgui.ImGuiScreen;
import io.github.client.tracker.impl.AuthTracker;
import imgui.ImGui;
import imgui.type.ImString;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

/**
 * @author markuss
 * @since 16/09/2025
 */
@ImGuiScreen
public class LoginScreen extends Screen {
    public LoginScreen() {
        super(Text.of("Login"));
    }

    private final ImString username = new ImString("", 20);
    private final ImString password = new ImString("", 128);

    @Override
    protected void init() {
        if (AuthTracker.INSTANCE.loginFile.username == null) {
            AuthTracker.INSTANCE.loginFile.load();
            if (AuthTracker.INSTANCE.loginFile.username != null) {
                username.set(AuthTracker.INSTANCE.loginFile.username);
                password.set(AuthTracker.INSTANCE.loginFile.password);
            }
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        super.render(context, mouseX, mouseY, deltaTicks);

        if (AuthTracker.INSTANCE.connected) {
            MinecraftClient.getInstance().setScreen(null);
            return;
        }

        ImGuiImpl.draw(io -> {
            if (ImGui.begin("Authentication")) {
                ImGui.inputText("Username", username);
                ImGui.inputText("Password", password, ImGuiInputTextFlags.Password);

                if (ImGui.button("Login")) {
                    try {
                        AuthTracker.INSTANCE.initialize();
                        AuthTracker.INSTANCE.authenticate(username.get(), password.get());
                    } catch (Exception e) {
                        Attero.LOGGER.error("Failed to login: ", e);
                    }
                }
            }
            ImGui.end();
        });
    }

    @Override public void applyBlur(DrawContext dc) { }
}
