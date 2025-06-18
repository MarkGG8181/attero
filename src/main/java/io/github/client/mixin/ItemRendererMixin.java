package io.github.client.mixin;

import io.github.client.event.impl.render.RenderItemEnchantmentGlintEvent;
import net.minecraft.client.render.item.ItemRenderState;
import net.minecraft.client.render.item.ItemRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

/**
 * @author markuss
 */
@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin {
    @ModifyVariable(
            method = "renderItem(Lnet/minecraft/item/ItemDisplayContext;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;II[ILjava/util/List;Lnet/minecraft/client/render/RenderLayer;Lnet/minecraft/client/render/item/ItemRenderState$Glint;)V",
            at = @At("HEAD"),
            argsOnly = true
    )
    private static ItemRenderState.Glint modifyEnchant(ItemRenderState.Glint glint) {
        RenderItemEnchantmentGlintEvent renderItemEnchantmentGlintEvent = new RenderItemEnchantmentGlintEvent();
        renderItemEnchantmentGlintEvent.post();

        if (renderItemEnchantmentGlintEvent.isCancelled()) {
            return ItemRenderState.Glint.NONE;
        }

        return glint;
    }
}