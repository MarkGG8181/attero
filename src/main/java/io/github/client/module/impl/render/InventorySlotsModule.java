package io.github.client.module.impl.render;

import io.github.client.event.data.Subscribe;
import io.github.client.event.impl.render.DrawSlotEvent;
import io.github.client.module.AbstractModule;
import io.github.client.module.data.ModuleCategory;
import io.github.client.module.data.ModuleInfo;

@ModuleInfo(name = "InventorySlots", category = ModuleCategory.RENDER, description = "Shows slot numbers")
public class InventorySlotsModule extends AbstractModule {
    @Subscribe
    private void drawSlot(DrawSlotEvent event) {
        var slot = event.slot;
        event.context.drawText(mc.textRenderer, String.valueOf(slot.getIndex()), slot.x, slot.y + mc.textRenderer.fontHeight, -1, false);
    }
}