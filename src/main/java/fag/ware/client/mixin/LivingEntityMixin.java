package fag.ware.client.mixin;

import fag.ware.client.event.impl.render.HandSwingDurationEvent;
import fag.ware.client.event.impl.render.RenderEatingParticlesEvent;
import fag.ware.client.event.impl.world.JumpEvent;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * @author markuss
 */
@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    @Redirect(method = "jump", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;getYaw()F"))
    private float getYaw(LivingEntity instance) {
        JumpEvent jumpEvent = new JumpEvent(instance, instance.getYaw());
        jumpEvent.post();

        return jumpEvent.getYaw();
    }

    @Inject(method = "getHandSwingDuration", at = @At("HEAD"), cancellable = true)
    public void getHandSwingDuration(CallbackInfoReturnable<Integer> cir) {
        HandSwingDurationEvent handSwingDurationEvent = new HandSwingDurationEvent();
        handSwingDurationEvent.post();

        if (handSwingDurationEvent.isCancelled()) {
            cir.setReturnValue(handSwingDurationEvent.getSpeed());
        }
    }

    @Inject(method = "spawnItemParticles", at = @At("HEAD"), cancellable = true)
    private void spawnItemParticles(ItemStack stack, int count, CallbackInfo info) {
        RenderEatingParticlesEvent renderEatingParticlesEvent = new RenderEatingParticlesEvent();
        renderEatingParticlesEvent.post();

        if (renderEatingParticlesEvent.isCancelled() && stack.getComponents().contains(DataComponentTypes.FOOD)) {
            info.cancel();
        }
    }
}