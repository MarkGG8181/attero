package fag.ware.client.screen;

import fag.ware.client.Fagware;
import fag.ware.client.module.AbstractModule;
import fag.ware.client.module.data.ModuleCategory;
import fag.ware.client.screen.data.ImGuiImpl;
import imgui.*;
import imgui.flag.*;
import lombok.AllArgsConstructor;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.util.HashMap;
import java.util.Map;

public class JelloClickScreen extends Screen {
    private final Map<ModuleCategory, ImVec2> positions = new HashMap<>();
    private final ImVec2 size = new ImVec2(230, 230);

    public JelloClickScreen() {
        super(Text.of("Jello"));
    }

    private boolean initialised;
    private SettingRenderer settingRenderer;

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        super.render(context, mouseX, mouseY, deltaTicks);

        if (!initialised) {
            positions.clear();
            int columns = 3;
            int spacing = 12;
            int startX = 20;
            int startY = 20;

            int x = startX;
            int y = startY;
            int col = 0;

            for (ModuleCategory category : ModuleCategory.values()) {
                positions.put(category, new ImVec2(x, y));

                col++;
                if (col >= columns) {
                    col = 0;
                    x = startX;
                    y += (int) (size.y + spacing);
                } else {
                    x += (int) (size.x + spacing);
                }
            }

            initialised = true;
        }

        ImGuiImpl.draw(io -> {
            ImGuiImpl.applyDarkTheme();
            for (ModuleCategory category : ModuleCategory.values()) {
                ImVec2 position = positions.get(category);

                ImGui.pushFont(ImGuiImpl.INTER_REGULAR_17);
                ImGui.setNextWindowPos(position, ImGuiCond.Once);
                ImGui.setNextWindowSize(size); //once not here to prevent from resizing the windows

                if (ImGui.begin(category.getName() + "##1", ImGuiWindowFlags.NoDocking | ImGuiWindowFlags.NoResize | ImGuiWindowFlags.NoCollapse)) {
                    ImVec2 newPosition = ImGui.getWindowPos();
                    position.set(newPosition);

                    for (AbstractModule module : Fagware.INSTANCE.moduleTracker.getByCategory(category)) {
                        ImGui.pushID(module.toString());

                        boolean isToggled = module.isEnabled();
                        if (isToggled) {
                            ImGui.pushStyleColor(ImGuiCol.Button, 0.20f, 0.50f, 0.90f, 1.0f);       // base
                            ImGui.pushStyleColor(ImGuiCol.ButtonHovered, 0.25f, 0.55f, 0.95f, 1.0f); // hover
                            ImGui.pushStyleColor(ImGuiCol.ButtonActive, 0.15f, 0.45f, 0.85f, 1.0f); // pressed
                        }

                        if (ImGui.button(module.toString(), -1, 0)) {
                            module.toggle();
                        }

                        if (ImGui.isItemHovered() && ImGui.isMouseClicked(ImGuiMouseButton.Right)) {
                            module.setExpanded(true);
                            settingRenderer = new SettingRenderer(module);
                        }

                        if (isToggled) {
                            ImGui.popStyleColor(3);
                        }

                        ImGui.popID();
                    }
                }

                ImGui.end();

                if (settingRenderer != null) {
                    settingRenderer.render(io);
                }

                ImGui.popFont();
            }
        });
    }

    @AllArgsConstructor
    private static class SettingRenderer {
        public AbstractModule parent;

        public void render(ImGuiIO io) {
            ImGui.setNextWindowPos(new ImVec2(io.getDisplaySize().x * 0.5f, io.getDisplaySize().y * 0.5f), ImGuiCond.Always, new ImVec2(0.5f,0.5f));
            ImGui.setNextWindowSize(430, 500);

            if (ImGui.begin("Settings", ImGuiWindowFlags.NoDocking | ImGuiWindowFlags.NoResize | ImGuiWindowFlags.NoCollapse | ImGuiWindowFlags.NoTitleBar)) {

                ImGui.pushFont(ImGuiImpl.INTER_REGULAR_30);

                ImDrawList drawList = ImGui.getForegroundDrawList();
                ImVec2 winPos = ImGui.getWindowPos();
                drawList.addText(new ImVec2(winPos.x + 5, winPos.y - ImGui.getTextLineHeight() - 5), ImColor.rgb(255, 255, 255), parent.toString());

                ImGui.popFont();

                if (!ImGui.isWindowFocused()) {
                    Fagware.INSTANCE.screenTracker.getByClass(JelloClickScreen.class).settingRenderer = null;
                }
            }
            ImGui.end();
        }
    }
}