package io.github.client.util.game;

import io.github.client.tracker.impl.AuthTracker;
import io.github.client.util.interfaces.IMinecraft;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;

// Warnings pmo sm icl
@SuppressWarnings("ALL")
public class MovementUtil implements IMinecraft {

    /**
     * wtf even is this
     * @see MovementUtil#getActualSpeed()
     */
    @Deprecated
    public static float getSpeed() {
        return mc.player != null ? mc.player.speed : 0.0f;
    }

    /**
     * Checks if the player is moving on every axis except the vertical one.
     * @return Returns a boolean value if the player is moving horizontally or diagonally.
     */
    public static boolean isMoving() {
        return mc.player.input.getMovementInput().y != 0 || mc.player.input.getMovementInput().x != 0;
    }

    public static double getActualSpeed()
    {
        return Math.sqrt(Math.pow(getMotionX(), 2) + Math.pow(getMotionZ(), 2));
    }

    public static void setSpeed(double moveSpeed) {
        float yaw = mc.player.getYaw();
        double strafe = mc.player.input.getMovementInput().x;
        double forward = mc.player.input.getMovementInput().y;

        if (forward != 0.0D) {
            yaw += (strafe > 0.0D) ? (forward > 0.0D ? -45 : 45) : (strafe < 0.0D) ? (forward > 0.0D ? 45 : -45) : 0;
            strafe = 0.0D;
            forward = (forward > 0.0D) ? 1.0D : -1.0D;
        }

        if (strafe != 0.0D) {
            strafe = (strafe > 0.0D) ? 1.0D : -1.0D;
        }

        double radianYaw = Math.toRadians(yaw + 90.0F);
        double cosYaw = Math.cos(radianYaw);
        double sinYaw = Math.sin(radianYaw);

        double x = forward * moveSpeed * cosYaw + strafe * moveSpeed * sinYaw;
        double z = forward * moveSpeed * sinYaw - strafe * moveSpeed * cosYaw;
        mc.player.setVelocity(x, mc.player.getVelocity().y, z);
    }

    /**
     * @Author Graph
     * Horrid code.
     */
    public static void setMotionX(double motionX) {
        mc.player.setVelocity(motionX, mc.player.getVelocity().y, mc.player.getVelocity().z);
    }

    public static void setMotionY(double motionY) {
        mc.player.setVelocity(mc.player.getVelocity().x, motionY, mc.player.getVelocity().z);
    }

    public static void setMotionZ(double motionZ) {
        mc.player.setVelocity(mc.player.getVelocity().x, mc.player.getVelocity().y, motionZ);
    }

    public static double getMotionX()
    {
        return mc.player.getVelocity().x;
    }

    public static double getMotionY()
    {
        return mc.player.getVelocity().y;
    }

    public static double getMotionZ()
    {
        return mc.player.getVelocity().z;
    }

    public static void multiplyMotion(double x, double y, double z)
    {
        mc.player.setVelocity(
                mc.player.getVelocity().x * x,
                mc.player.getVelocity().y * y,
                mc.player.getVelocity().z * z
        );
    }

    public static void offsetPosition(double x, double y, double z)
    {
        mc.player.setPosition(mc.player.getPos().add(x, y, z));
    }

    public static int getSpeedAmplifier()
    {
        final StatusEffectInstance speed = mc.player.getStatusEffect(StatusEffects.SPEED);

        if (speed != null)
        {
            return speed.getAmplifier();
        }
        else
        {
            return 0;
        }
    }

    /**
     * Adjusted with the way that sigma handles shit (sowwy for pasting
     */
    public static int getSpeedAmplifier2()
    {
        int speed = getSpeedAmplifier();
        if (speed > 0) speed++;

        return speed;
    }

    public static double defaultSpeed()
    {
        double baseSpeed = AuthTracker.getInstance().values[5];
        int speed = getSpeedAmplifier2();

        if (speed != 0)
        {
            baseSpeed *= (1.0D + 0.2D * speed);
        }

        return baseSpeed;
    }

    /**
     * yes
     */
    public static void strafe()
    {
        setSpeed(getActualSpeed());
    }
}