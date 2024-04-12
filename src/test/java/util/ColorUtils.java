package util;

import com.formdev.flatlaf.util.HSLColor;

import java.awt.*;

public abstract class ColorUtils {
    public static boolean isColorDark(Color color) {
        return color.getRed()<128 && color.getGreen()<128 && color.getBlue()<128;
    }
    public static Color saturate(Color color, float amount){
        HSLColor newColor = new HSLColor(HSLColor.fromRGB(color));
        return newColor.adjustSaturation(amount);
    }
}
