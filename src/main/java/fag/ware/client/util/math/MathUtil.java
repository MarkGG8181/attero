package fag.ware.client.util.math;

import net.minecraft.util.math.MathHelper;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class MathUtil {
    public static float wrap(float current, float target, float max) {
        float diff = MathHelper.wrapDegrees(target - current);
        if (diff > max) diff = max;
        if (diff < -max) diff = -max;
        return current + diff;
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(Double.toString(value));
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}