package fag.ware.client.module.data.setting;

import fag.ware.client.Fagware;
import fag.ware.client.module.Module;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class Setting<T> {
    private final String name;
    private T value;
    private final T defaultValue;

    private final Module parent;

    public Setting(String name, T value) {
        this.name = name;
        this.value = value;
        this.defaultValue = value;

        this.parent = Fagware.INSTANCE.moduleTracker.lastModule;
        this.parent.getSettings().add(this);
    }
}
