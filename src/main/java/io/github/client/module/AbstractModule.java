package io.github.client.module;

import io.github.client.Attero;
import io.github.client.module.data.ModuleInfo;
import io.github.client.module.data.setting.AbstractSetting;
import io.github.client.tracker.impl.ModuleTracker;
import io.github.client.util.java.interfaces.IMinecraft;
import io.github.client.util.java.math.anim.Animation;
import io.github.client.util.java.math.anim.EnumTransition;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public abstract class AbstractModule implements IMinecraft {
    private Animation x = new Animation(0, EnumTransition.SQRT, 1f);
    private Animation y = new Animation(0, EnumTransition.SQRT, 1f);
    private List<Integer> keybinds = new ArrayList<>();
    private boolean enabled, expanded;
    private final ModuleInfo info;

    private List<AbstractSetting<?>> settings = new ArrayList<>();

    public AbstractModule() {
        if (!getClass().isAnnotationPresent(ModuleInfo.class)) {
            throw new RuntimeException("Module " + getClass().getName() + " is missing @ModuleInfo");
        }

        info = getClass().getAnnotation(ModuleInfo.class);
        ModuleTracker.INSTANCE.lastModule = this;
    }

    @Override
    public String toString() {
        return info.name();
    }

    public boolean isExpanded() {
        if (getSettings().isEmpty())
            return false;

        return expanded;
    }

    public void toggle() {
        setEnabled(!isEnabled());
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;

        if (enabled) {
            Attero.BUS.register(this);
            onEnable();
        } else {
            onDisable();
            Attero.BUS.unregister(this);
        }
    }

    public void onEnable() {}

    public void onDisable() {}

    public void onInit() {}

    public String getSuffix() {
        return null;
    }

    public AbstractSetting<?> getSettingByName(String input) {
        return settings.stream()
                .filter(s -> s.getName().equalsIgnoreCase(input))
                .findFirst()
                .orElse(null);
    }

    public boolean finishedAnimating()
    {
        return x.getValue() < 5;
    }

}