package fag.ware.client.screen.impl;

import fag.ware.client.module.data.ModuleCategory;
import fag.ware.client.util.imgui.ImGuiImpl;
import imgui.ImGui;
import imgui.ImVec2;
import imgui.flag.ImGuiCond;
import imgui.flag.ImGuiWindowFlags;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.util.HashMap;
import java.util.Map;

public class ClickScreen extends Screen {
    private final Map<ModuleCategory, ImVec2> positions = new HashMap<>();
    private final ImVec2 size = new ImVec2(200, 0);

    public ClickScreen() {
        super(Text.of("ClickScreen"));
    }

    private boolean initialised;

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        super.render(context, mouseX, mouseY, deltaTicks);

        if (!initialised) {
            positions.clear();
            int x = 20;
            for (ModuleCategory category : ModuleCategory.values()) {
                positions.put(category, new ImVec2(x, 20));
                System.out.println(positions.get(category).x);
                x += (int) (size.x + 12);
            }
            initialised = true;
        }

        ImGuiImpl.draw(io -> {
            for (ModuleCategory category : ModuleCategory.values()) {
                ImVec2 position = positions.get(category);

                ImGui.pushFont(ImGuiImpl.defaultFont);
                ImGui.setNextWindowPos(position, ImGuiCond.Once);
                ImGui.setNextWindowSize(size, ImGuiCond.Once);

                if (ImGui.begin(category.getName(), ImGuiWindowFlags.NoDocking)) {
                    ImVec2 newPosition = ImGui.getWindowPos();
                    position.set(newPosition);
                }

                ImGui.end();
                ImGui.popFont();
            }
        });
    }
}
