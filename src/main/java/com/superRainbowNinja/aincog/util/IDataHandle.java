package com.superRainbowNinja.aincog.util;

import net.minecraft.nbt.NBTTagCompound;

/**
 * Created by SuperRainbowNinja on 23/12/2016.
 */
public interface IDataHandle <T> {
    String getName();
    NBTTagCompound writeNBT(NBTTagCompound compound);
    NBTTagCompound readNBT(NBTTagCompound compound);
}
