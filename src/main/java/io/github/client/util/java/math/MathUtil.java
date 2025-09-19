package io.github.client.util.java.math;

import net.minecraft.util.math.MathHelper;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class MathUtil {
    private static final DateTimeFormatter DISPLAY_FORMATTER = DateTimeFormatter.ofPattern("MMM dd, yyyy").withZone(ZoneId.systemDefault());

    public static String formatTime(String time) {
        String formattedDate;
        try {
            Instant instant = Instant.parse(time); // parse ISO8601
            formattedDate = DISPLAY_FORMATTER.format(instant);
        } catch (Exception e) {
            formattedDate = time;
        }

        return formattedDate;
    }

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