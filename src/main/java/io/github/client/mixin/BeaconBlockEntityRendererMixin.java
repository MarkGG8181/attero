package io.github.client.mixin;

import io.github.client.event.impl.render.RenderBeaconBeamEvent;
import net.minecraft.block.entity.BeamEmitter;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BeaconBlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author markuss
 */
@Mixin(BeaconBlockEntityRenderer.class)
public abstract class BeaconBlockEntityRendererMixin<T extends BlockEntity & BeamEmitter> implements BlockEntityRenderer<T> {
    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private void onRender(T entity, float tickProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, Vec3d cameraPos, CallbackInfo ci) {
        RenderBeaconBeamEvent renderBeaconBeamEvent = new RenderBeaconBeamEvent();
        renderBeaconBeamEvent.post();

        if (renderBeaconBeamEvent.cancelled) {
            ci.cancel();
        }
    }
}