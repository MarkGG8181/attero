package fag.ware.client.util.player;

import fag.ware.client.util.IMinecraft;

public class MovementUtil implements IMinecraft {

    public static float getSpeed() {
        return mc.player != null ? mc.player.speed : 0.0f;
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


}
