package io.github.client.mixin;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import io.github.client.event.impl.render.HasBlindnessEvent;
import io.github.client.event.impl.render.HasDarknessEvent;
import io.github.client.event.impl.render.RenderEntitiesGlowColorEvent;
import io.github.client.event.impl.render.RenderWorldBorderEvent;
import net.minecraft.client.render.*;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.border.WorldBorder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.awt.*;

/**
 * @author markuss
 */
@Mixin(WorldRenderer.class)
public abstract class WorldRendererMixin {
    @WrapWithCondition(method = "method_62216", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/WorldBorderRendering;render(Lnet/minecraft/world/border/WorldBorder;Lnet/minecraft/util/math/Vec3d;DD)V"))
    private boolean shouldRenderWorldBorder(WorldBorderRendering instance, WorldBorder border, Vec3d cameraPos, double viewDistanceBlocks, double farPlaneDistance) {
        RenderWorldBorderEvent renderWorldBorderEvent = new RenderWorldBorderEvent();
        renderWorldBorderEvent.post();

        return !renderWorldBorderEvent.cancelled;
    }

    @Inject(method = "hasBlindnessOrDarkness(Lnet/minecraft/client/render/Camera;)Z", at = @At("HEAD"), cancellable = true)
    private void hasBlindnessOrDarkness(Camera camera, CallbackInfoReturnable<Boolean> info) {
        HasDarknessEvent hasDarknessEvent = new HasDarknessEvent();
        hasDarknessEvent.post();
        HasBlindnessEvent hasBlindnessEvent = new HasBlindnessEvent();
        hasBlindnessEvent.post();

        if (hasDarknessEvent.cancelled || hasBlindnessEvent.cancelled) {
            info.setReturnValue(null);
        }
    }

    @WrapOperation(method = "renderEntities", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/OutlineVertexConsumerProvider;setColor(IIII)V"))
    private void setGlowColor(OutlineVertexConsumerProvider instance, int red, int green, int blue, int alpha, Operation<Void> original, @Local LocalRef<Entity> entity) {
        RenderEntitiesGlowColorEvent entitiesGlowColorEvent = new RenderEntitiesGlowColorEvent(entity.get(), new Color(red, green, blue, alpha));
        entitiesGlowColorEvent.post();

        if (entitiesGlowColorEvent.cancelled) {
            Color clr = entitiesGlowColorEvent.color;
            instance.setColor(clr.getRed(), clr.getGreen(), clr.getBlue(), clr.getAlpha());
        } else {
            original.call(instance, red, green, blue, alpha);
        }
    }
}