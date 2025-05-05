package fag.ware.client.module.impl.render;

import fag.ware.client.event.data.Subscribe;
import fag.ware.client.event.impl.render.Render2DEvent;
import fag.ware.client.module.Module;
import fag.ware.client.module.data.ModuleCategory;
import fag.ware.client.module.data.ModuleInfo;
import fag.ware.client.module.data.setting.impl.ColorSetting;

import java.awt.*;

@ModuleInfo(name = "Watermark", category = ModuleCategory.RENDER, description = "Draws a watermark")
public class WatermarkModule extends Module {
    private final ColorSetting color = new ColorSetting("Color", new Color(255, 255, 255));

    @Subscribe
    public void onRender(Render2DEvent event) {
        event.getDrawContext().drawText(mc.textRenderer, "Fag", 5, 5, color.toInt(), false);
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }

    @Override
    public void onInit() {
        setEnabled(true);
    }
}
