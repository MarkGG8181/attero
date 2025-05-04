package fag.ware.client.module.impl.render;

import fag.ware.client.event.data.Subscribe;
import fag.ware.client.event.impl.render.Render2DEvent;
import fag.ware.client.module.Module;
import fag.ware.client.module.data.ModuleCategory;
import fag.ware.client.module.data.ModuleInfo;

import java.awt.*;

@ModuleInfo(name = "Watermark", category = ModuleCategory.RENDER, description = "Draws a watermark")
public class WatermarkModule extends Module {
    @Subscribe
    public void onRender(Render2DEvent event) {
        event.getDrawContext().drawText(mc.textRenderer, "Fag", 5, 5, Color.WHITE.getRGB(), false);
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
