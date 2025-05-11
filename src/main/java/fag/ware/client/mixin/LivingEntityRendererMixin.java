package fag.ware.client.mixin;

import fag.ware.client.Fagware;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * @author markuss
 */
@Mixin(LivingEntityRenderer.class)
public class LivingEntityRendererMixin {
    @Redirect(
            method = "updateRenderState(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/client/render/entity/state/LivingEntityRenderState;F)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/util/math/MathHelper;lerpAngleDegrees(FFF)F"
            )
    )
    private float replaceYawLerp(float delta, float lastYaw, float yaw, LivingEntity livingEntity) {
        if (livingEntity == MinecraftClient.getInstance().player && !(MinecraftClient.getInstance().currentScreen instanceof InventoryScreen)) {
            return MathHelper.lerpAngleDegrees(delta, Fagware.INSTANCE.combatTracker.prevYaw, Fagware.INSTANCE.combatTracker.yaw);
        }

        return MathHelper.lerpAngleDegrees(delta, lastYaw, yaw);
    }

    @Inject(method = "clampBodyYaw", at = @At("HEAD"), cancellable = true)
    private static void clampBodyYawReplace(LivingEntity entity, float degrees, float tickProgress, CallbackInfoReturnable<Float> cir) {
        if (entity == MinecraftClient.getInstance().player && !(MinecraftClient.getInstance().currentScreen instanceof InventoryScreen)) {
            float fakeBodyYaw = MathHelper.lerpAngleDegrees(tickProgress, Fagware.INSTANCE.combatTracker.prevBodyYaw, Fagware.INSTANCE.combatTracker.bodyYaw);
            float h = MathHelper.clamp(MathHelper.wrapDegrees(degrees - fakeBodyYaw), -85.0F, 85.0F);
            float clamped = degrees - h;
            if (Math.abs(h) > 50.0F) {
                clamped += h * 0.2F;
            }
            cir.setReturnValue(clamped);
        }
    }

    @Redirect(
            method = "updateRenderState(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/client/render/entity/state/LivingEntityRenderState;F)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/LivingEntity;getLerpedPitch(F)F"
            )
    )
    private float replaceYawLerp(LivingEntity instance, float tickProgress) {
        if (instance == MinecraftClient.getInstance().player && !(MinecraftClient.getInstance().currentScreen instanceof InventoryScreen)) {
            return tickProgress == 1.0F ? Fagware.INSTANCE.combatTracker.pitch : MathHelper.lerp(tickProgress, Fagware.INSTANCE.combatTracker.prevPitch, Fagware.INSTANCE.combatTracker.pitch);
        }
        return tickProgress == 1.0F ? instance.getPitch() : MathHelper.lerp(tickProgress, instance.lastPitch, instance.getPitch());
    }
}