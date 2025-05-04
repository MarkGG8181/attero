package fag.ware.client.module.data;

import lombok.Getter;

@Getter
public enum ModuleCategory {
    COMBAT("Combat"),
    MOVEMENT("Movement"),
    RENDER("Render"),
    PLAYER("Player"),
    WORLD("World"),
    MISC("Misc");

    private final String string;

    ModuleCategory(String string) {
        this.string = string;
    }
}
