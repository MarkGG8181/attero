package fag.ware.client.mixin;

import fag.ware.client.event.impl.JumpEvent;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

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
}