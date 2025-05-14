package fag.ware.client.module.data.setting;

import fag.ware.client.Fagware;
import fag.ware.client.module.AbstractModule;
import fag.ware.client.module.data.setting.impl.GroupSetting;
import lombok.Getter;
import lombok.Setter;

import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

@Getter
@Setter
public abstract class AbstractSetting<T> {
    private final String name;
    private T value;
    private final T defaultValue;

    private BooleanSupplier hidden = () -> false;
    private Consumer<T> onChange = null;

    private AbstractModule parent = null;
    private GroupSetting parentSetting = null;

    public AbstractSetting(String name, T value) {
        this(name, value, false);
    }

    public AbstractSetting(String name, T value, boolean noParent) {
        this.name = name;
        this.value = value;
        this.defaultValue = value;

        if (!noParent) {
            this.parent = Fagware.INSTANCE.moduleTracker.lastModule;
            this.parent.getSettings().add(this);
        }
    }

    public AbstractSetting<T> hide(BooleanSupplier hidden) {
        this.hidden = hidden;
        return this;
    }

    public AbstractSetting<T> onChange(Consumer<T> onChange) {
        this.onChange = onChange;
        return this;
    }

    public AbstractSetting<T> setParent(GroupSetting parent) {
        this.parentSetting = parent;
        return this;
    }

    public void setValue(T newValue) {
        this.value = newValue;
        if (onChange != null) onChange.accept(newValue);
    }
}
