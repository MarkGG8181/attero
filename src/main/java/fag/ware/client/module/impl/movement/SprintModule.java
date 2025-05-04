package fag.ware.client.module.impl.movement;

import fag.ware.client.event.data.Subscribe;
import fag.ware.client.event.impl.UpdateEvent;
import fag.ware.client.module.Module;
import fag.ware.client.module.data.ModuleCategory;
import fag.ware.client.module.data.ModuleInfo;
import org.lwjgl.glfw.GLFW;

@ModuleInfo(name = "Sprint", category = ModuleCategory.MOVEMENT, description = "Makes you always sprint")
public class SprintModule extends Module {

    @Subscribe
    public void onUpdate(UpdateEvent event) {

    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }

    @Override
    public void onInit() {
        setKeybind(GLFW.GLFW_KEY_R);
    }
}
