package io.github.client.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import io.github.client.event.impl.render.RenderSignTextEvent;
import net.minecraft.client.render.block.entity.AbstractSignBlockEntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/**
 * @author markuss
 */
@Mixin(AbstractSignBlockEntityRenderer.class)
public abstract class AbstractSignBlockEntityRendererMixin {
    @ModifyExpressionValue(method = "renderText", at = @At(value = "CONSTANT", args = {"intValue=4", "ordinal=1"}))
    private int loopTextLengthProxy(int i) {
        RenderSignTextEvent renderSignTextEvent = new RenderSignTextEvent();
        renderSignTextEvent.post();

        if (renderSignTextEvent.isCancelled()) {
            return 0;
        }

        return i;
    }
}