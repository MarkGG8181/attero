package io.github.client.mixin;

import io.github.client.event.impl.render.RenderEntityEvent;
import io.github.client.event.impl.render.RenderFallingBlockEvent;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.FallingBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * @author markuss
 */
@Mixin(EntityRenderer.class)
public class EntityRendererMixin<T extends Entity> {
    @Inject(method = "shouldRender", at = @At("HEAD"), cancellable = true)
    private void shouldRender(T entity, Frustum frustum, double x, double y, double z, CallbackInfoReturnable<Boolean> cir) {
        if (entity instanceof FallingBlockEntity) {
            RenderFallingBlockEvent renderFallingBlockEvent = new RenderFallingBlockEvent();
            renderFallingBlockEvent.post();

            if (renderFallingBlockEvent.cancelled) {
                cir.setReturnValue(false);
            }
        } else {
            RenderEntityEvent renderEntityEvent = new RenderEntityEvent(entity);
            renderEntityEvent.post();

            if (renderEntityEvent.cancelled) {
                cir.setReturnValue(false);
            }
        }
    }
}