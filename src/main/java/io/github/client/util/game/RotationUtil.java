package io.github.client.util.game;

import io.github.client.event.impl.interact.MoveInputEvent;
import io.github.client.tracker.impl.AuthTracker;
import io.github.client.tracker.impl.CombatTracker;
import io.github.client.util.interfaces.IMinecraft;
import io.github.client.util.math.FastNoiseLite;
import io.github.client.util.math.MathUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

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

    public static float[] toRotation(Vec3d target, float minSpeed, float maxSpeed) {
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
        final float forward = event.getForward();
        final float strafe = event.getStrafe();

        // moveFlying method in Entity.java
        float f = forward * forward + strafe * strafe;
        if (f < 1.0E-4F) {
            return;
        }

        f = MathHelper.sqrt(f);
        if (f < 1.0F) {
            f = 1.0F;
        }

        final float normalizedForward = forward / f;
        final float normalizedStrafe = strafe / f;
        final float realYaw = mc.player.getYaw();

        final float PI = (float) Math.PI;

        final float realSin = MathHelper.sin(realYaw * PI / 180.0F);
        final float realCos = MathHelper.cos(realYaw * PI / 180.0F);
        final float desiredX = normalizedStrafe * realCos - normalizedForward * realSin;
        final float desiredZ = normalizedForward * realCos + normalizedStrafe * realSin;

        final float targetSin = MathHelper.sin(yaw * PI / 180.0F);
        final float targetCos = MathHelper.cos(yaw * PI / 180.0F);

        float bestForward = 0.0F;
        float bestStrafe = 0.0F;
        float closestDistSq = Float.MAX_VALUE;

        final float sprintFavorFactor = 1.05F; // basically so it always tries to sprint if it can

        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i == 0 && j == 0) continue;

                float candidateMag = MathHelper.sqrt((float) (i * i + j * j));
                final float normI = i / candidateMag;
                final float normJ = j / candidateMag;

                final float resultX = normJ * targetCos - normI * targetSin;
                final float resultZ = normI * targetCos + normJ * targetSin;

                final float dX = desiredX - resultX;
                final float dZ = desiredZ - resultZ;
                float distSq = dX * dX + dZ * dZ;

                if (forward > 0 && i <= 0) {
                    distSq *= sprintFavorFactor;
                }

                if (distSq < closestDistSq) {
                    closestDistSq = distSq;
                    bestForward = i;
                    bestStrafe = j;
                }
            }
        }

        event.setForward(bestForward);
        event.setStrafe(bestStrafe);
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

    public static float clampBodyYaw(LivingEntity entity, float degrees, float tickProgress) {
        Entity var4 = entity.getVehicle();
        if (var4 instanceof LivingEntity livingEntity) {
            float f = MathHelper.lerpAngleDegrees(tickProgress, livingEntity.lastBodyYaw, livingEntity.bodyYaw);
            float g = 85.0F;
            float h = MathHelper.clamp(MathHelper.wrapDegrees(degrees - f), -85.0F, 85.0F);
            f = degrees - h;
            if (Math.abs(h) > 50.0F) {
                f += h * 0.2F;
            }

            return f;
        } else {
            return MathHelper.lerpAngleDegrees(tickProgress, entity.lastBodyYaw, entity.bodyYaw);
        }
    }
}