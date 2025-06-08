package fag.ware.client.util.game;

import fag.ware.client.event.impl.interact.MoveInputEvent;
import fag.ware.client.tracker.impl.AuthTracker;
import fag.ware.client.tracker.impl.CombatTracker;
import fag.ware.client.util.interfaces.IMinecraft;
import fag.ware.client.util.math.FastNoiseLite;
import fag.ware.client.util.math.MathUtil;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;

import java.security.SecureRandom;

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

    public static float[] toRotation(Entity entity, float minSpeed, float maxSpeed) {
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

        float currentYaw = CombatTracker.getInstance().yaw;
        float currentPitch = CombatTracker.getInstance().pitch;

        float[] rots = new float[]{MathHelper.wrapDegrees(yaw), MathHelper.clamp(pitch, -90f, 90f)};

        SecureRandom random = new SecureRandom();
        float yawSpeed = random.nextFloat(minSpeed, maxSpeed);
        float pitchSpeed = random.nextFloat(minSpeed, maxSpeed);

        float deltaYaw = MathHelper.wrapDegrees(rots[0] - currentYaw);
        float deltaPitch = MathHelper.wrapDegrees(rots[1] - currentPitch);

        deltaYaw = MathHelper.clamp(deltaYaw, -yawSpeed, yawSpeed);
        deltaPitch = MathHelper.clamp(deltaPitch, -pitchSpeed, pitchSpeed);

        rots[0] = currentYaw + deltaYaw;
        rots[1] = currentPitch + deltaPitch;

        return patchGCD(rots, new float[]{CombatTracker.getInstance().yaw, CombatTracker.getInstance().pitch});
    }

    public static float[] patchGCD(final float[] currentRotation,
                                   final float[] newRotation) {
        final float f = sens * AuthTracker.getInstance().values[0] + AuthTracker.getInstance().values[1];

        final float gcd = f * f * f * AuthTracker.getInstance().values[2] * AuthTracker.getInstance().values[3];

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

    public static void correctMovement(final MoveInputEvent event, final float yaw) {
        if (event.getForward() == 0 && event.getStrafe() == 0) {
            return;
        }

        float realYaw = mc.player.getYaw();

        float moveX = event.getStrafe() * (float) Math.cos(Math.toRadians(realYaw)) - event.getForward() * (float) Math.sin(Math.toRadians(realYaw));
        float moveZ = event.getForward() * (float) Math.cos(Math.toRadians(realYaw)) + event.getStrafe() * (float) Math.sin(Math.toRadians(realYaw));

        double[] bestMovement = null;

        for (int forward = -1; forward <= 1; forward++) {
            for (int strafe = -1; strafe <= 1; strafe++) {
                if (forward == 0 && strafe == 0) continue;

                float newMoveX = strafe * (float) Math.cos(Math.toRadians(yaw)) - forward * (float) Math.sin(Math.toRadians(yaw));
                float newMoveZ = forward * (float) Math.cos(Math.toRadians(yaw)) + strafe * (float) Math.sin(Math.toRadians(yaw));

                float deltaX = newMoveX - moveX;
                float deltaZ = newMoveZ - moveZ;

                double dist = Math.sqrt(deltaX * deltaX + deltaZ * deltaZ);

                if (bestMovement == null || bestMovement[0] > dist) {
                    bestMovement = new double[]{dist, forward, strafe};
                }
            }
        }

        event.setForward((float) Math.round(bestMovement[1]));
        event.setStrafe((float) Math.round(bestMovement[2]));
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
        return switch (mc.player.getHorizontalFacing()) {
            case SOUTH -> -180;
            case NORTH -> 0;
            case EAST -> 90;
            case WEST -> -90;
            default -> mc.player.getYaw();
        };
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