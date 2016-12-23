package com.superRainbowNinja.aincog.util;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Created by SuperRainbowNinja on 6/12/2016.
 */
public class ExactPosition implements IRenderPos{
    public int getX() {
        return x;
    }

    public void setX(int xIn) {
        x = rangeCheck(xIn);
    }

    public int getY() {
        return y;
    }

    public void setY(int yIn) {
        y = rangeCheck(yIn);
    }

    public int getZ() {
        return z;
    }

    public void setZ(int zIn) {
        z = rangeCheck(zIn);
    }

    protected int rangeCheck(int num) {
        return num >= 1 ? 1 : 0;
    }

    private int x;
    private int y;
    private int z;
    private int rotation;

    private boolean wholeLayer;

    public ExactPosition() {
        this(0, 0, 0);
    }

    public ExactPosition(int xIn, int yIn, int zIn) {
        this(xIn, yIn, zIn, false);
    }

    public ExactPosition(int xIn, int yIn, int zIn, boolean whole) {
        x = xIn;
        y = yIn;
        z = zIn;
        wholeLayer = whole;
        reCalcRotation();
    }

    public ExactPosition(ByteBuf buf) {
        this(buf.readInt(), buf.readInt(), buf.readInt(), buf.readBoolean());
    }

    public void toBytes(ByteBuf buf) {
        buf.writeInt(x);
        buf.writeInt(y);
        buf.writeInt(z);
        buf.writeBoolean(wholeLayer);
    }

    public ExactPosition(NBTTagCompound tag) {
        this(tag.getInteger("X"), tag.getInteger("Y"), tag.getInteger("Z"), tag.getBoolean("WHOLE"));
    }

    public NBTTagCompound getTag(NBTTagCompound tag) {
        tag.setInteger("X", x);
        tag.setInteger("Y", y);
        tag.setInteger("Z", z);
        tag.setBoolean("WHOLE", wholeLayer);
        return tag;
    }

    public boolean isTop() {
        return y == 1;
    }

    public boolean isWholeLayer() {
        return wholeLayer;
    }

    public int getRotation() {
        return rotation;
    }

    public ExactPosition asWholeLayer() {
        wholeLayer = true;
        return this;
    }

    public boolean compatible(EnumPosition pos) {
        return pos.isSingle && layerCheck(pos) && !isWholeLayer();
    }

    public boolean layerCheck(EnumPosition pos) {
        return pos.isTop == isTop();
    }

    public boolean layerCheck(ExactPosition pos) {
        return pos.isTop() == isTop();
    }

    @Override
    public float getXOffset() {
        return wholeLayer ? 0.5f : x == 0 ? 4f/16f : 12f/16f;
        //return wholeLayer ? 0.5f : x == 0 ? 5f/16f : 11f/16f;
    }

    @Override
    public float getYOffset() {
        return y == 0 ? 4f/16f : 12f/16f;
    }

    @Override
    public float getZOffset() {
        return wholeLayer ? 0.5f : z == 0 ? 4f/16f : 12f/16f;
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof ExactPosition && equals(((ExactPosition) obj)));
    }

    public boolean equals(ExactPosition obj) {
        if (obj == this) {
            return true;
        }
        return (x == obj.getX() && y == obj.getY() && z == obj.getZ() && wholeLayer == obj.isWholeLayer());
    }

    public static ExactPosition getPosFromCo2(float x, float y, float z) {
        return new ExactPosition(twoPartSplit(x), twoPartSplit(y), twoPartSplit(z));
    }

    public static final float THRESHOLD = 0.5f;

    private static int twoPartSplit(float num) {
        return num > THRESHOLD ? 1 : 0;
    }

    private void reCalcRotation() {
        if (wholeLayer) {
            rotation = 0;
            return;
        }
        if (x == 1) {
            if (z == 1) {
                rotation = 180;
            } else {
                rotation = 270;
            }
        } else if (z == 1) {
            rotation = 90;
        }
    }
}
