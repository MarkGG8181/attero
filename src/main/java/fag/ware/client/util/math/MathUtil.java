package fag.ware.client.util.math;

import net.minecraft.util.math.MathHelper;

public class MathUtil {
    public static float wrap(float current, float target, float max) {
        float diff = MathHelper.wrapDegrees(target - current);
        if (diff > max) diff = max;
        if (diff < -max) diff = -max;
        return current + diff;
    }
}
