package fag.ware.client.util.player;

import fag.ware.client.util.IMinecraft;

// Warnings pmo sm icl
@SuppressWarnings("ALL")
public class MovementUtil implements IMinecraft {

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
}