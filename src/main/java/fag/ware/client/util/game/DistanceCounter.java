package fag.ware.client.util.game;

import net.minecraft.entity.player.PlayerEntity;

public class DistanceCounter {
    private double distanceTravelled = 0;
    private double lastX, lastY, lastZ;
    private boolean initialized = false;

    public void tick(PlayerEntity player) {
        if (player == null) return;

        double x = player.getX();
        double y = player.getY();
        double z = player.getZ();

        if (!initialized) {
            lastX = x;
            lastY = y;
            lastZ = z;
            initialized = true;
            return;
        }

        double deltaX = x - lastX;
        double deltaY = y - lastY;
        double deltaZ = z - lastZ;
        double moved = Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ);

        distanceTravelled += moved;

        lastX = x;
        lastY = y;
        lastZ = z;
    }

    public double getTravelled() {
        return distanceTravelled;
    }

    public void reset() {
        distanceTravelled = 0;
    }
}