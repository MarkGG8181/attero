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

        return patchGCD(new float[]{yaw, pitch}, new float[] {RotationTracker.yaw, RotationTracker.pitch});
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

        return patchGCD(new float[]{yaw, pitch}, new float[]{RotationTracker.yaw, RotationTracker.pitch});
    }

    public static float getGCD() {
        float sensitivity = mc.options.getMouseSensitivity().getValue().floatValue();
        float f = sensitivity * 0.6f + 0.2f;
        float gcd = f * f * f * 8.0f;

        boolean spyglass = mc.options.getPerspective().isFirstPerson() && mc.player.isUsingSpyglass();
        return spyglass ? (f * f * f) : gcd;
    }

    public static float[] snapRotation(float[] currentRotation, float[] targetRotation) {
        float gcd = getGCD() * 0.15f;

        float deltaYaw = MathHelper.wrapDegrees(targetRotation[0] - currentRotation[0]);
        float deltaPitch = targetRotation[1] - currentRotation[1];

        float snappedYaw = currentRotation[0] + Math.round(deltaYaw / gcd) * gcd;
        float snappedPitch = currentRotation[1] + Math.round(deltaPitch / gcd) * gcd;

        snappedPitch = MathHelper.clamp(snappedPitch, -90f, 90f);
        return new float[] {snappedYaw, snappedPitch};
    }

    public static float[] patchGCD(final float[] target, final float[] current) {
        return snapRotation(current, target);
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
        float forward = event.forward;
        float strafe = event.strafe;

        double angle = MathHelper.wrapDegrees(Math.toDegrees(direction(mc.player.getYaw(), forward, strafe)));

        if (forward == 0 && strafe == 0) {
            return;
        }

        float closestForward = 0, closestStrafe = 0, closestDifference = Float.MAX_VALUE;

        for (float predictedForward = -1F; predictedForward <= 1F; predictedForward += 1F) {
            for (float predictedStrafe = -1F; predictedStrafe <= 1F; predictedStrafe += 1F) {
                if (predictedStrafe == 0 && predictedForward == 0) continue;

                double predictedAngle = MathHelper.wrapDegrees(Math.toDegrees(direction(yaw, predictedForward, predictedStrafe)));
                double difference = Math.abs(angle - predictedAngle);

                if (difference < closestDifference) {
                    closestDifference = (float) difference;
                    closestForward = predictedForward;
                    closestStrafe = predictedStrafe;
                }
            }

            event.forward = closestForward;
            event.strafe = closestStrafe;
        }
    }
}