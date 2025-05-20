package fag.ware.client.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import fag.ware.client.tracker.impl.CombatTracker;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Objects;

import static fag.ware.client.util.interfaces.IMinecraft.mc;

/**
 * @author kibty
 */
@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin<T extends LivingEntity, S extends LivingEntityRenderState, M extends EntityModel<? super S>> {

    @ModifyExpressionValue(method = "updateRenderState(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/client/render/entity/state/LivingEntityRenderState;F)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/LivingEntityRenderer;clampBodyYaw(Lnet/minecraft/entity/LivingEntity;FF)F"))
    private float changeBodyYaw(final float original, final LivingEntity living, final S state, final float tickDelta) {
        if (living != mc.player)
            return original;

        if (Objects.nonNull(CombatTracker.getInstance().target)) {
            return MathHelper.lerpAngleDegrees(tickDelta, CombatTracker.getInstance().prevYaw, CombatTracker.getInstance().yaw);
        }

        return original;
    }

    @ModifyExpressionValue(method = "updateRenderState(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/client/render/entity/state/LivingEntityRenderState;F)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/MathHelper;lerpAngleDegrees(FFF)F"))
    private float changeHeadYaw(final float original, final LivingEntity living, final S state, final float tickDelta) {
        if (living != mc.player)
            return original;

        if (Objects.nonNull(CombatTracker.getInstance().target)) {
            return MathHelper.lerpAngleDegrees(tickDelta, CombatTracker.getInstance().prevYaw, CombatTracker.getInstance().yaw);
        }

        return original;
    }

    @ModifyExpressionValue(method = "updateRenderState(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/client/render/entity/state/LivingEntityRenderState;F)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;getLerpedPitch(F)F"))
    private float changePitch(final float original, final LivingEntity living, final S state, final float tickDelta) {
        if (living != mc.player)
            return original;

        if (Objects.nonNull(CombatTracker.getInstance().target)) {
            return MathHelper.lerpAngleDegrees(tickDelta, CombatTracker.getInstance().prevPitch, CombatTracker.getInstance().pitch);
        }

        return original;
    }
}