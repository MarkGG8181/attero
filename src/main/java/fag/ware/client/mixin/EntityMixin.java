package fag.ware.client.mixin;

import fag.ware.client.Fagware;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

/**
 * @author markuss
 */
@Mixin(Entity.class)
public abstract class EntityMixin {
    @Shadow public abstract float getYaw();

    @Shadow public abstract float getPitch();

    /**
     * @author markuss
     * @reason to make player's crosshair aligned with the modified rotations
     */
    @Overwrite
    public final Vec3d getRotationVec(float tickProgress) {
        if ((Object) this instanceof ClientPlayerEntity) {
            return getRotationVector(Fagware.INSTANCE.combatTracker.pitch, Fagware.INSTANCE.combatTracker.yaw);
        }

        return getRotationVector(getPitch(), getYaw());
    }

    /**
     * Turns the pitch and yaw into an instance of Vec3d (rotation vector)
     *
     * @param pitch the vector target pitch
     * @param yaw the vector target yaw
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
}