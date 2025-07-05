package io.github.client.mixin;

import io.github.client.event.impl.world.UpdateVelocityEvent;
import io.github.client.tracker.impl.RotationTracker;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author markuss
 */
@SuppressWarnings("ALL")
@Mixin(Entity.class)
public abstract class EntityMixin {
    @Shadow
    public abstract float getYaw();

    @Shadow
    public abstract float getPitch();

    @Shadow
    public abstract void setVelocity(Vec3d velocity);

    @Shadow
    public abstract Vec3d getVelocity();

    @Shadow
    protected static Vec3d movementInputToVelocity(Vec3d movementInput, float speed, float yaw) {
        return null;
    }

    /**
     * @author markuss
     * @reason to make player's crosshair aligned with the modified rotations
     */
    @Overwrite
    public final Vec3d getRotationVec(float tickProgress) {
        return getRotationVector(RotationTracker.pitch, RotationTracker.yaw);
    }

    @Shadow
    protected abstract Vec3d getRotationVector(float pitch, float yaw);

    @Inject(method = "updateVelocity", at = @At("HEAD"), cancellable = true)
    private void updateVelocity(float speed, Vec3d movementInput, CallbackInfo ci) {
        ci.cancel();

        if ((Object) this instanceof ClientPlayerEntity) {
            UpdateVelocityEvent updateVeloEvent = new UpdateVelocityEvent(this.getYaw(), (float) movementInput.x, (float) movementInput.z, speed);
            updateVeloEvent.post();

            float yaw = updateVeloEvent.yaw;
            double strafe = updateVeloEvent.strafe;
            double forward = updateVeloEvent.forward;
            float friction = updateVeloEvent.friction;

            Vec3d vec3d = movementInputToVelocity(new Vec3d(strafe, movementInput.y, forward), friction, yaw);
            this.setVelocity(this.getVelocity().add(vec3d));
        } else {
            Vec3d vec3d = movementInputToVelocity(movementInput, speed, this.getYaw());
            this.setVelocity(this.getVelocity().add(vec3d));
        }
    }
}