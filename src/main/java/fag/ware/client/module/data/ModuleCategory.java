package fag.ware.client.module.data;

import lombok.Getter;

@Getter
public enum ModuleCategory {
    COMBAT("Combat"),
    MOVEMENT("Movement"),
    RENDER("Render"),
    PLAYER("Player"),
    WORLD("World"),
    MISC("Misc"),
    CONFIGS("Configs");

    private final String name;

    ModuleCategory(String name) {
        this.name = name;
    }
}
