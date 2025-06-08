package fag.ware.client.mixin;

import fag.ware.client.event.impl.render.RenderEatingParticlesEvent;
import fag.ware.client.event.impl.world.JumpEvent;
import fag.ware.client.module.impl.render.AnimationsModule;
import fag.ware.client.tracker.impl.ModuleTracker;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static fag.ware.client.util.interfaces.IMinecraft.mc;

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
        if (mc.player != null || mc.world != null) {
            AnimationsModule animationsModule = ModuleTracker.getInstance().getByClass(AnimationsModule.class);
            if (animationsModule.isEnabled()) {
                cir.setReturnValue(animationsModule.swingSpeed.toInt());
            }
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