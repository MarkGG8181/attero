package fag.ware.client.module.impl.render;

import fag.ware.client.event.data.Subscribe;
import fag.ware.client.event.impl.render.DrawSlotEvent;
import fag.ware.client.module.AbstractModule;
import fag.ware.client.module.data.ModuleCategory;
import fag.ware.client.module.data.ModuleInfo;

@ModuleInfo(name = "InventorySlots", category = ModuleCategory.RENDER, description = "Shows slot numbers")
public class InventorySlotsModule extends AbstractModule {
    @Subscribe
    public void drawSlot(DrawSlotEvent event) {
        var slot = event.getSlot();
        event.getContext().drawText(mc.textRenderer, String.valueOf(slot.getIndex()), slot.x, slot.y + mc.textRenderer.fontHeight, -1, false);
    }
}