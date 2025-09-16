package io.github.client.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import io.github.client.event.impl.player.CanWalkOnLiquidEvent;
import io.github.client.event.impl.render.HandSwingDurationEvent;
import io.github.client.event.impl.render.RenderEatingParticlesEvent;
import io.github.client.event.impl.world.JumpEvent;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static io.github.client.util.interfaces.IMinecraft.mc;

/**
 * @author markuss
 */
@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @ModifyReturnValue(method = "canWalkOnFluid", at = @At("RETURN"))
    private boolean onCanWalkOnFluid(boolean original, FluidState fluidState) {
        if ((Object) this != mc.player) return original;

        CanWalkOnLiquidEvent event = new CanWalkOnLiquidEvent(fluidState, original);
        event.post();

        return event.walkOnFluid;
    }

    @Redirect(method = "jump", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;getYaw()F"))
    private float getYaw(LivingEntity instance) {
        JumpEvent jumpEvent = new JumpEvent(instance, instance.getYaw());
        jumpEvent.post();

        return jumpEvent.yaw;
    }

    @Inject(method = "getHandSwingDuration", at = @At("RETURN"), cancellable = true)
    public void getHandSwingDuration(CallbackInfoReturnable<Integer> cir) {
        HandSwingDurationEvent handSwingDurationEvent = new HandSwingDurationEvent(cir.getReturnValue());
        handSwingDurationEvent.post();

        cir.setReturnValue(handSwingDurationEvent.speed);
    }

    @Inject(method = "spawnItemParticles", at = @At("HEAD"), cancellable = true)
    private void spawnItemParticles(ItemStack stack, int count, CallbackInfo info) {
        RenderEatingParticlesEvent renderEatingParticlesEvent = new RenderEatingParticlesEvent();
        renderEatingParticlesEvent.post();

        if (renderEatingParticlesEvent.cancelled && stack.getComponents().contains(DataComponentTypes.FOOD)) {
            info.cancel();
        }
    }
}