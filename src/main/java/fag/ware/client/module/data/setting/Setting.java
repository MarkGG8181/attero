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

    private Module parent = null;

    public Setting(String name, T value) {
        this(name, value, false);
    }

    public Setting(String name, T value, boolean noParent) {
        this.name = name;
        this.value = value;
        this.defaultValue = value;

        if (!noParent) {
            this.parent = Fagware.INSTANCE.moduleTracker.lastModule;
            this.parent.getSettings().add(this);
        }
    }
}
