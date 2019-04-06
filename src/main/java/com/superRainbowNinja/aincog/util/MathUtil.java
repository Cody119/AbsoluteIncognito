package com.superRainbowNinja.aincog.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;

import java.awt.*;
import java.util.function.Function;

/**
 * Created by SuperRainbowNinja on 15/11/2017.
 */
public final class MathUtil {
    private MathUtil() {}

    public static Function<Integer, Integer> generateColorSpecturm(final int time, final int ... colors) {

        double perColor1 = ((double)time)/colors.length;
        final int perColor = (int)Math.round(perColor1);

        final double[] grads = new double[colors.length];
        for (int i = 0; i < colors.length; i++) {
            int next = (i+1) % colors.length;
            grads[i] = (colors[next] - colors[i])/perColor1;
        }

        return (curTime) -> {
            int curTime1 = curTime % time;
            int curTime2 = curTime % perColor;
            if (curTime2 == 0) {
                return colors[curTime1/perColor];
            } else {
                return (int)Math.round(curTime2*grads[curTime1/perColor]);
            }
        };
    }

    public static int getRainbowColor() {
        WorldClient w = Minecraft.getMinecraft().world;
        if (w != null) {
            return Color.HSBtoRGB(((w.getTotalWorldTime() % 160)/230f), 0.99f, 0.48f);
        }

        return 0;
    }
}
