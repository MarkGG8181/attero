package fag.ware.client.module;

import fag.ware.client.Fagware;
import fag.ware.client.module.data.ModuleInfo;
import fag.ware.client.util.IMinecraft;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class Module implements IMinecraft {
    private final ModuleInfo info;
    private boolean enabled, expanded;
    private int keybind = 0;

    public Module() {
        if (!getClass().isAnnotationPresent(ModuleInfo.class)) {
            throw new RuntimeException("Module " + getClass().getName() + " is missing @ModuleInfo");
        }

        info = getClass().getAnnotation(ModuleInfo.class);
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
}