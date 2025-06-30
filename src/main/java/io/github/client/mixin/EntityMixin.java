package io.github.client.mixin;

import io.github.client.event.impl.world.UpdateVelocityEvent;
import io.github.client.tracker.impl.RotationTracker;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
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

    @Unique
    private static Vec3d movementInputToVelocity(Vec3d movementInput, float speed, float yaw) {
        double d = movementInput.lengthSquared();
        if (d < 1.0E-7) {
            return Vec3d.ZERO;
        } else {
            Vec3d vec3d = (d > 1.0 ? movementInput.normalize() : movementInput).multiply(speed);
            float f = MathHelper.sin(yaw * (float) (Math.PI / 180.0));
            float g = MathHelper.cos(yaw * (float) (Math.PI / 180.0));
            return new Vec3d(vec3d.x * g - vec3d.z * f, vec3d.y, vec3d.z * g + vec3d.x * f);
        }
    }

    /**
     * @author markuss
     * @reason to make player's crosshair aligned with the modified rotations
     */
    @Overwrite
    public final Vec3d getRotationVec(float tickProgress) {
        if ((Object) this instanceof ClientPlayerEntity && RotationTracker.rotating) {
            return getRotationVector(RotationTracker.pitch, RotationTracker.yaw);
        }

        return getRotationVector(getPitch(), getYaw());
    }

    /**
     * Turns the pitch and yaw into an instance of Vec3d (rotation vector)
     *
     * @param pitch the vector target pitch
     * @param yaw   the vector target yaw
     * @return a Vec3d rotation vector constructed from the pitch and yaw
     */
    @Unique
    private Vec3d getRotationVector(float pitch, float yaw) {
        float f = pitch * (float) (Math.PI / 180.0);
        float g = -yaw * (float) (Math.PI / 180.0);
        float h = MathHelper.cos(g);
        float i = MathHelper.sin(g);
        float j = MathHelper.cos(f);
        float k = MathHelper.sin(f);
        return new Vec3d(i * j, -k, h * j);
    }

    @Inject(method = "updateVelocity", at = @At("HEAD"), cancellable = true)
    private void updateVelocity(float speed, Vec3d movementInput, CallbackInfo ci) {
        ci.cancel();

        if ((Object) this instanceof ClientPlayerEntity) {
            UpdateVelocityEvent updateVeloEvent = new UpdateVelocityEvent(this.getYaw(), (float) movementInput.x, (float) movementInput.z, speed);
            updateVeloEvent.post();

            float yaw = updateVeloEvent.yaw;
            float strafe = updateVeloEvent.strafe;
            float forward = updateVeloEvent.forward;
            float friction = updateVeloEvent.friction;

            Vec3d vec3d = movementInputToVelocity(new Vec3d(strafe, movementInput.y, forward), friction, yaw);
            this.setVelocity(this.getVelocity().add(vec3d));
        } else {
            Vec3d vec3d = movementInputToVelocity(movementInput, speed, this.getYaw());
            this.setVelocity(this.getVelocity().add(vec3d));
        }
    }
}