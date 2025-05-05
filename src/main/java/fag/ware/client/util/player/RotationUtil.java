package fag.ware.client.util.player;

import fag.ware.client.util.IMinecraft;
import fag.ware.client.util.math.FastNoiseLite;
import net.minecraft.entity.LivingEntity;
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
}
