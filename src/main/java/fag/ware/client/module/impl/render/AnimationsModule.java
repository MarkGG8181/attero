package fag.ware.client.module.impl.render;

import fag.ware.client.event.data.Subscribe;
import fag.ware.client.event.impl.render.HandSwingDurationEvent;
import fag.ware.client.module.AbstractModule;
import fag.ware.client.module.data.ModuleCategory;
import fag.ware.client.module.data.ModuleInfo;
import fag.ware.client.module.data.setting.impl.NumberSetting;

@ModuleInfo(
        name = "Animations",
        description = "Modifies your hand animations",
        category = ModuleCategory.RENDER
)
public class AnimationsModule extends AbstractModule {
    private final NumberSetting swingSpeed = new NumberSetting("Swing Speed", 12, 1, 25);

    @Subscribe
    public void onHandSwing(HandSwingDurationEvent event) {
        event.setSpeed(swingSpeed.toInt());
    }
}