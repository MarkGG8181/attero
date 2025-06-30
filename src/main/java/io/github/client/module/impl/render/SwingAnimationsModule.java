package io.github.client.module.impl.render;

import io.github.client.event.data.Subscribe;
import io.github.client.event.impl.render.HandSwingDurationEvent;
import io.github.client.module.AbstractModule;
import io.github.client.module.data.ModuleCategory;
import io.github.client.module.data.ModuleInfo;
import io.github.client.module.data.setting.impl.NumberSetting;

@ModuleInfo(
        name = "SwingAnimations",
        description = "Modifies your swinging animations",
        category = ModuleCategory.RENDER
)
public class SwingAnimationsModule extends AbstractModule {
    private final NumberSetting swingSpeed = new NumberSetting("Swing Speed", 12, 1, 25);

    @Subscribe
    public void onHandSwing(HandSwingDurationEvent event) {
        event.speed = (swingSpeed.max.intValue() - swingSpeed.toInt() + swingSpeed.min.intValue());
    }
}