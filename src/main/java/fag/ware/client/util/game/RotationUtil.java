package fag.ware.client.util.game;

import fag.ware.client.event.impl.MoveInputEvent;
import fag.ware.client.util.interfaces.IMinecraft;
import fag.ware.client.util.math.FastNoiseLite;
import fag.ware.client.util.math.MathUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;

public class RotationUtil implements IMinecraft {
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

    public static float[] toRotation(LivingEntity entity) {
        float time = (float) (System.currentTimeMillis() % 10000) / 1000.0f;

        float noiseValueX = noiseX.GetNoise(time, 0.0f) * 0.5f;
        float noiseValueY = noiseY.GetNoise(time, 100.0f) * 0.5f;
        float noiseValueZ = noiseZ.GetNoise(time, 200.0f) * 0.5f;

        double x = noiseValueX + entity.getPos().x + (entity.getPos().x - entity.lastX) - mc.player.getPos().x;
        double z = noiseValueZ + entity.getPos().z + (entity.getPos().z - entity.lastZ) - mc.player.getPos().z;
        double y = noiseValueY + (entity.getPos().y + entity.getHeight() - 0.5f) - (mc.player.getPos().y + mc.player.getStandingEyeHeight());

        double theta = Math.hypot(x, z);
        float yaw = (float) -Math.toDegrees(Math.atan2(x, z));
        float pitch = (float) Math.toDegrees(-Math.atan2(y, theta));

        return new float[]{MathHelper.wrapDegrees(yaw), MathHelper.clamp(pitch, -90f, 90f)};
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

    public static void silentStrafe(final MoveInputEvent event, float yaw) {
        final float forward = event.getForward();
        final float strafe = event.getStrafe();

        final double angle = MathHelper.wrapDegrees(Math.toDegrees(direction(mc.player.getYaw(), forward, strafe)));

        if (forward == 0 && strafe == 0) {
            return;
        }

        float closestForward = 0, closestStrafe = 0, closestDifference = Float.MAX_VALUE;

        for (float predictedForward = -1F; predictedForward <= 1F; predictedForward += 1F) {
            for (float predictedStrafe = -1F; predictedStrafe <= 1F; predictedStrafe += 1F) {
                if (predictedStrafe == 0 && predictedForward == 0) continue;

                final double predictedAngle = MathHelper.wrapDegrees(Math.toDegrees(direction(yaw, predictedForward, predictedStrafe)));
                final double difference = Math.abs(angle - predictedAngle);

                if (difference < closestDifference) {
                    closestDifference = (float) difference;
                    closestForward = predictedForward;
                    closestStrafe = predictedStrafe;
                }
            }

            event.setForward(closestForward);
            event.setStrafe(closestStrafe);
        }
    }


    public static float getMovementYaw() {
        boolean forward = mc.options.forwardKey.isPressed();
        boolean back = mc.options.backKey.isPressed();
        boolean left = mc.options.leftKey.isPressed();
        boolean right = mc.options.rightKey.isPressed();

        float moveX = 0;
        float moveZ = 0;

        if (forward) moveZ -= 1;
        if (back) moveZ += 1;
        if (left) moveX -= 1;
        if (right) moveX += 1;

        if (moveX == 0 && moveZ == 0) {
            return mc.player.getYaw();
        }

        float length = (float) Math.hypot(moveX, moveZ);
        moveX /= length;
        moveZ /= length;

        float yawRad = (float) Math.toRadians(mc.player.getYaw());
        float sin = (float) Math.sin(yawRad);
        float cos = (float) Math.cos(yawRad);

        float rotatedX = moveX * cos - moveZ * sin;
        float rotatedZ = moveX * sin + moveZ * cos;

        return (float) Math.toDegrees(Math.atan2(rotatedZ, rotatedX)) - 90;
    }

    public static float getAdjustedYaw() {
        switch (mc.player.getHorizontalFacing()) {
            case SOUTH:
                return -180;
            case NORTH:
                return 0;
            case EAST:
                return 90;
            case WEST:
                return -90;
            default:
                return mc.player.getYaw();
        }
    }


    public static float[] getSimpleRotations(BlockCache blockCache, float[] lastRotations) {
        double diffY = blockCache.pos().getY() + 0.5 - (mc.player.getY() + mc.player.getStandingEyeHeight());

        if (blockCache.facing() == Direction.UP) diffY += 0.5;
        if (blockCache.facing() == Direction.DOWN) diffY -= 0.5;

        float yaw = getMovementYaw();
        float pitch = (float) -Math.toDegrees(Math.atan2(diffY, 1)); // assume distance of 1 for fake pitch
        yaw = MathUtil.wrap(lastRotations[0], yaw, 30);
        pitch = MathUtil.wrap(lastRotations[1], pitch, 20);

        return new float[]{yaw, pitch};
    }

    public static float[] getGodbridgeRotations(BlockCache blockCache, float[] lastRotations) {
        double diffY = blockCache.pos().getY() + 0.5 - (mc.player.getY() + mc.player.getStandingEyeHeight());

        if (blockCache.facing() == Direction.UP) diffY += 0.5;
        if (blockCache.facing() == Direction.DOWN) diffY -= 0.5;

        float yaw = getMovementYaw();
        float pitch = (float) -Math.toDegrees(Math.atan2(diffY, 1)); // fake dist = 1

        if (lastRotations == null) {
            return new float[]{yaw, pitch};
        }

        yaw = MathUtil.wrap(lastRotations[0], yaw, 30);
        pitch = MathUtil.wrap(lastRotations[1], pitch, 20);

        return new float[]{yaw, pitch};
    }
}