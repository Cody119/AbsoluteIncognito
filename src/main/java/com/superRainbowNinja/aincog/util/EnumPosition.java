package com.superRainbowNinja.aincog.util;

/**
 * Created by SuperRainbowNinja on 13/11/2016.
 *
 */
public enum EnumPosition {
    TOP(false, true),
    BOTTOM(false, false),
    TOP_SINGLE(true, true),
    BOTTOM_SINGLE(true, false);

    public final boolean isSingle;
    public final boolean isTop;
    EnumPosition(boolean single, boolean top) {
        isSingle = single;
        isTop = top;
    }

    /*
    TOP(1, 0.5f, 11f/16f, 0.5f, false, true),
    BOTTOM(2, 0.5f, 5f/16f, 0.5f, false, false),
    TOP_SINGLE(3, 0.5f, 11f/16f, 0.5f, true, true),
    BOTTOM_SINGLE(4, 0.5f, 5f/16f, 0.5f, true, false),

    TOP_NW(5, 0.5f, 11f/16f, 0.5f, true, true),
    TOP_NE(6, 0.5f, 11f/16f, 0.5f, true, true),
    TOP_SW(7, 0.5f, 11f/16f, 0.5f, true, true),
    TOP_SE(8, 0.5f, 11f/16f, 0.5f, true, true),
    BOTTOM_NW(9, 0.5f, 5f/16f, 0.5f, true, false),
    BOTTOM_NE(10, 0.5f, 5f/16f, 0.5f, true, false),
    BOTTOM_SW(11, 0.5f, 5f/16f, 0.5f, true, false),
    BOTTOM_SE(12, 0.5f, 5f/16f, 0.5f, true, false);

    public static final float THRESHOLD = 0.5f;

    public final float renderXOffSet;
    public final float renderYOffSet;
    public final float renderZOffSet;
    public final boolean isSingle;
    public final boolean isTop;
    public final int id;
    EnumPosition(int idIn, float offXSet, float offYSet, float offZSet, boolean single, boolean top) {
        id = idIn;
        renderXOffSet = offXSet;
        renderYOffSet = offYSet;
        renderZOffSet = offZSet;
        isSingle = single;
        isTop = top;
    }

    public boolean willFitIn(EnumPosition container) {
        if (this == container) {
            return true;
        }
        switch (container) {
            case TOP:
                return isTop;
            case BOTTOM:
                return  !isTop;
            case TOP_SINGLE:
                return isSingle && isTop;
            case BOTTOM_SINGLE:
                return isSingle && !isTop;

        }
        return false;
    }

    public static EnumPosition getFromId(int id) {
        for (EnumPosition pos : values()) {
            if (pos.id == id) {
                return pos;
            }
        }
        return EnumPosition.TOP;
    }

    public static EnumPosition getPosFromCo4(float x, float y, float z) {
        if (x > THRESHOLD) {
            if (y > THRESHOLD) {
                if (z > THRESHOLD) {
                    return TOP_SE;
                } else {
                    return TOP_NE;
                }
            } else {
                if (z > THRESHOLD) {
                    return BOTTOM_SE;
                } else {
                    return BOTTOM_NE;
                }
            }
        } else {
            if (y > THRESHOLD) {
                if (z > THRESHOLD) {
                    return TOP_SW;
                } else {
                    return TOP_NW;
                }
            } else {
                if (z > THRESHOLD) {
                    return BOTTOM_SW;
                } else {
                    return BOTTOM_NW;
                }
            }
        }
    }
    */
}
