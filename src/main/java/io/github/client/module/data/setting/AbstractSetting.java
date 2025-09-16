package io.github.client.module.data.setting;

import io.github.client.module.AbstractModule;
import io.github.client.module.data.setting.impl.GroupSetting;
import io.github.client.tracker.impl.ModuleTracker;
import lombok.Getter;
import lombok.Setter;

import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

/**
 * @author markuss
 * @since 05/05/2025
 */
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
            this.parent = ModuleTracker.INSTANCE.lastModule;
            this.parent.getSettings().add(this);
        }
    }

    public <I extends AbstractSetting<?>> I hide(BooleanSupplier hidden) {
        this.hidden = hidden != null ? hidden : () -> false;
        return (I) this;
    }

    public AbstractSetting<T> onChange(Consumer<T> onChange) {
        this.onChange = onChange;
        return this;
    }

    public <I extends AbstractSetting<?>> I setParent(GroupSetting parent) {
        this.parentSetting = parent;
        return (I) this;
    }

    public void setValue(T newValue) {
        this.value = newValue;
        if (onChange != null) onChange.accept(newValue);
    }
}
