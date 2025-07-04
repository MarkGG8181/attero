package io.github.client.util.game;

import io.github.client.event.impl.interact.MoveInputEvent;
import io.github.client.tracker.impl.AuthTracker;
import io.github.client.tracker.impl.RotationTracker;
import io.github.client.util.java.interfaces.IMinecraft;
import io.github.client.util.java.math.FastNoiseLite;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class RotationUtil implements IMinecraft {
    private static final float sens = mc.options.getMouseSensitivity().getValue().floatValue();
    private static final FastNoiseLite noise = new FastNoiseLite();
    private static final FastNoiseLite noiseX = new FastNoiseLite();
    private static final FastNoiseLite noiseY = new FastNoiseLite();
    private static final FastNoiseLite noiseZ = new FastNoiseLite();

    static {
        noise.SetNoiseType(FastNoiseLite.NoiseType.OpenSimplex2);
        noise.SetFrequency(0.05f);

        noiseX.SetNoiseType(FastNoiseLite.NoiseType.Perlin);
        noiseX.SetFrequency(0.05f);
        noiseY.SetNoiseType(FastNoiseLite.NoiseType.OpenSimplex2);
        noiseY.SetFrequency(0.05f);
        noiseZ.SetNoiseType(FastNoiseLite.NoiseType.OpenSimplex2S);
        noiseZ.SetFrequency(0.05f);
    }

    public static float[] toRotation(Entity entity) {
        final float time = (float) (System.currentTimeMillis() % 10000) / 1000.0f;

        final float noiseValueX = noiseX.GetNoise(time, 0.0f) * 0.5f;
        final float noiseValueY = noiseY.GetNoise(time, 100.0f) * 0.5f;
        final float noiseValueZ = noiseZ.GetNoise(time, 200.0f) * 0.5f;

        final double x = noiseValueX + entity.getPos().x + (entity.getPos().x - entity.lastX) - mc.player.getPos().x;
        final double z = noiseValueZ + entity.getPos().z + (entity.getPos().z - entity.lastZ) - mc.player.getPos().z;
        final double y = noiseValueY + (entity.getPos().y + entity.getHeight() - 0.5f) - (mc.player.getPos().y + mc.player.getStandingEyeHeight());

        final double theta = Math.hypot(x, z);
        final float yaw = (float) -Math.toDegrees(Math.atan2(x, z));
        final float pitch = (float) Math.toDegrees(-Math.atan2(y, theta));

        final float[] rots = new float[]{MathHelper.wrapDegrees(yaw), MathHelper.clamp(pitch, -90f, 90f)};

        return patchGCD(rots, new float[] {RotationTracker.yaw, RotationTracker.pitch});
    }

    public static float[] toRotation(Vec3d target) {
        final float time = (float) (System.currentTimeMillis() % 10000) / 1000.0f;

        final float noiseValueX = noiseX.GetNoise(time, 0.0f) * 0.5f;
        final float noiseValueY = noiseY.GetNoise(time, 100.0f) * 0.5f;
        final float noiseValueZ = noiseZ.GetNoise(time, 200.0f) * 0.5f;

        final double x = noiseValueX + target.x - mc.player.getX();
        final double y = noiseValueY + target.y - (mc.player.getY() + mc.player.getEyeHeight(mc.player.getPose()));
        final double z = noiseValueZ + target.z - mc.player.getZ();

        final double theta = Math.hypot(x, z);
        final float yaw = (float) -Math.toDegrees(Math.atan2(x, z));
        final float pitch = (float) Math.toDegrees(-Math.atan2(y, theta));

        float[] rots = new float[]{MathHelper.wrapDegrees(yaw), MathHelper.clamp(pitch, -90f, 90f)};

        return patchGCD(rots, new float[]{RotationTracker.yaw, RotationTracker.pitch});
    }

    public static float[] patchGCD(final float[] currentRotation,
                                   final float[] newRotation) {
        final float f = sens * AuthTracker.INSTANCE.values[0] + AuthTracker.INSTANCE.values[1];

        final float gcd = f * f * f * AuthTracker.INSTANCE.values[2] * AuthTracker.INSTANCE.values[3];

        final float deltaYaw = currentRotation[0] - newRotation[0],
                deltaPitch = currentRotation[1] - newRotation[1];

        final float yaw = newRotation[0] + Math.round(deltaYaw / gcd) * gcd,
                pitch = newRotation[1] + Math.round(deltaPitch / gcd) * gcd;

        return new float[]{yaw, pitch};
    }

    public static double direction(float rotationYaw, final double moveForward, final double moveStrafing) {
        if (moveForward < 0F) rotationYaw += 180F;

        float forward = 1F;

        if (moveForward < 0F) forward = -0.5F;
        else if (moveForward > 0F) forward = 0.5F;

        if (moveStrafing > 0F) rotationYaw -= 90F * forward;
        if (moveStrafing < 0F) rotationYaw += 90F * forward;

        return Math.toRadians(rotationYaw);
    }

    public static void correctMovement(MoveInputEvent event, float yaw) {
        if (event.forward == 0F && event.strafe == 0F) {
            return;
        }

        double realYaw = mc.player.getYaw();

        double moveX = event.strafe * Math.cos(Math.toRadians(realYaw)) - event.forward * Math.sin(Math.toRadians(realYaw));
        double moveZ = event.forward * Math.cos(Math.toRadians(realYaw)) + event.strafe * Math.sin(Math.toRadians(realYaw));

        double[] bestMovement = null;

        for (int forward = -1; forward <= 1; forward++) {
            for (int strafe = -1; strafe <= 1; strafe++) {

                double newMoveX = strafe * Math.cos(Math.toRadians(yaw)) - forward * Math.sin(Math.toRadians(yaw));
                double newMoveZ = forward * Math.cos(Math.toRadians(yaw)) + strafe * Math.sin(Math.toRadians(yaw));

                double deltaX = newMoveX - moveX;
                double deltaZ = newMoveZ - moveZ;

                double dist = Math.sqrt(deltaX * deltaX + deltaZ * deltaZ);

                if (bestMovement == null || bestMovement[0] > dist) {
                    bestMovement = new double[]{dist, (double) forward, (double) strafe};
                }
            }
        }

        event.forward = Math.round(bestMovement[1]);
        event.strafe = Math.round(bestMovement[2]);
    }

    public static float getAdjustedYaw() {
        return switch (mc.player.getHorizontalFacing()) {
            case SOUTH -> -180;
            case NORTH -> 0;
            case EAST -> 90;
            case WEST -> -90;
            default -> mc.player.getYaw();
        };
    }
}