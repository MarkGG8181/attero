package fag.ware.client.util.math;

import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.math.MathHelper;

import java.awt.*;

public class ColorUtil {

    public static MutableText createGradientText(String text, Color startColor, Color endColor) {
        MutableText result = Text.empty();
        int length = text.length();

        for (int i = 0; i < length; i++) {
            float ratio = (float) i / (length - 1);
            int red = MathHelper.lerp(ratio, startColor.getRed(), endColor.getRed());
            int green = MathHelper.lerp(ratio, startColor.getGreen(), endColor.getGreen());
            int blue = MathHelper.lerp(ratio, startColor.getBlue(), endColor.getBlue());

            Style style = Style.EMPTY.withColor(TextColor.fromRgb((red << 16) | (green << 8) | blue));
            result.append(Text.literal(String.valueOf(text.charAt(i))).setStyle(style));
        }

        return result;
    }

    public static int toImGuiColor(Color color) {
        return (color.getAlpha() << 24) | (color.getBlue() << 16) | (color.getGreen() << 8) | color.getRed();
    }
}
