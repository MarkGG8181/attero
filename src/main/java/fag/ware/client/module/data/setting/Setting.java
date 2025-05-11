package fag.ware.client.module.data.setting;

import fag.ware.client.Fagware;
import fag.ware.client.module.Module;
import lombok.Getter;
import lombok.Setter;

import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

@Getter
@Setter
public abstract class Setting<T> {
    private final String name;
    private T value;
    private final T defaultValue;

    private BooleanSupplier hidden = () -> false;
    private Consumer<T> onChange = null;

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

    public Setting<T> hide(BooleanSupplier hidden) {
        this.hidden = hidden;
        return this;
    }

    public Setting<T> onChange(Consumer<T> onChange) {
        this.onChange = onChange;
        return this;
    }

    public void setValue(T newValue) {
        this.value = newValue;
        if (onChange != null) onChange.accept(newValue);
    }
}
