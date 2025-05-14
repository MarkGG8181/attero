package fag.ware.client.module;

import fag.ware.client.Fagware;
import fag.ware.client.module.data.ModuleInfo;
import fag.ware.client.module.data.setting.AbstractSetting;
import fag.ware.client.util.IMinecraft;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public abstract class AbstractModule implements IMinecraft {
    private final ModuleInfo info;
    private boolean enabled, expanded;
    private int keybind = 0;

    private List<AbstractSetting<?>> settings = new ArrayList<>();

    public AbstractModule() {
        if (!getClass().isAnnotationPresent(ModuleInfo.class)) {
            throw new RuntimeException("Module " + getClass().getName() + " is missing @ModuleInfo");
        }

        info = getClass().getAnnotation(ModuleInfo.class);
        Fagware.INSTANCE.moduleTracker.lastModule = this;
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
            Fagware.BUS.register(this);
            onEnable();
        } else {
            onDisable();
            Fagware.BUS.unregister(this);
        }
    }

    public abstract void onEnable();

    public abstract void onDisable();

    public void onInit() {}

    public AbstractSetting<?> getSettingByName(String input) {
        return settings.stream()
                .filter(s -> s.getName().equalsIgnoreCase(input))
                .findFirst()
                .orElse(null);
    }
}