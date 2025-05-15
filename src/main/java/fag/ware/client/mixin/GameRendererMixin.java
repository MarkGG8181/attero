package fag.ware.client.mixin;

import fag.ware.client.event.impl.render.RenderTotemAnimationEvent;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author markuss
 */
@Mixin(GameRenderer.class)
public class GameRendererMixin {
    @Inject(method = "showFloatingItem", at = @At("HEAD"), cancellable = true)
    private void onShowFloatingItem(ItemStack floatingItem, CallbackInfo info) {
        RenderTotemAnimationEvent renderTotemAnimationEvent = new RenderTotemAnimationEvent();
        renderTotemAnimationEvent.post();

        if (renderTotemAnimationEvent.isCancelled() && floatingItem.getItem() == Items.TOTEM_OF_UNDYING) {
            info.cancel();
        }
    }
}