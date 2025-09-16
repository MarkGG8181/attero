package io.github.client.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import io.github.client.tracker.impl.RotationTracker;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import static io.github.client.util.interfaces.IMinecraft.mc;

/**
 * @author markuss
 */
@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin<S extends LivingEntityRenderState> {
    @ModifyExpressionValue(method = "updateRenderState(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/client/render/entity/state/LivingEntityRenderState;F)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/LivingEntityRenderer;clampBodyYaw(Lnet/minecraft/entity/LivingEntity;FF)F"))
    private float changeBodyYaw(final float original, final LivingEntity living, final S state, final float tickDelta) {
        if (living != mc.player)
            return original;

        if (RotationTracker.rotating) {
            return MathHelper.lerpAngleDegrees(tickDelta, RotationTracker.prevYaw, RotationTracker.yaw);
        }

        return original;
    }

    @ModifyExpressionValue(method = "updateRenderState(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/client/render/entity/state/LivingEntityRenderState;F)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/MathHelper;lerpAngleDegrees(FFF)F"))
    private float changeHeadYaw(final float original, final LivingEntity living, final S state, final float tickDelta) {
        if (living != mc.player)
            return original;

        if (RotationTracker.rotating) {
            return MathHelper.lerpAngleDegrees(tickDelta, RotationTracker.prevYaw, RotationTracker.yaw);
        }

        return original;
    }

    @ModifyExpressionValue(method = "updateRenderState(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/client/render/entity/state/LivingEntityRenderState;F)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;getLerpedPitch(F)F"))
    private float changePitch(final float original, final LivingEntity living, final S state, final float tickDelta) {
        if (living != mc.player)
            return original;

        if (RotationTracker.rotating) {
            return MathHelper.lerpAngleDegrees(tickDelta, RotationTracker.prevPitch, RotationTracker.pitch);
        }

        return original;
    }
}