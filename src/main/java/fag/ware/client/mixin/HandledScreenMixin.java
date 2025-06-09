package fag.ware.client.mixin;

import fag.ware.client.event.impl.render.DrawSlotEvent;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HandledScreen.class)
public class HandledScreenMixin extends Screen {
    protected HandledScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "drawSlot", at = @At("TAIL"))
    public void visualizeId(DrawContext context, Slot slot, CallbackInfo ci) {
        DrawSlotEvent drawSlotEvent = new DrawSlotEvent(context, slot);
        drawSlotEvent.post();
    }
}