package com.superRainbowNinja.aincog.util;

/**
 * Created by SuperRainbowNinja on 5/12/2016.
 */
public enum Operation {
    NOP(0), FINISH(-1), START(1);
    public final int scale;
    Operation(int scaleIn) {
        scale = scaleIn;
    }
}
