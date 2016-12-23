package com.superRainbowNinja.aincog.util.DataHandle;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Created by SuperRainbowNinja on 23/12/2016.
 */
public interface IFieldHandle<T> {
    String getName();
    void writeNBT(NBTTagCompound compound, T object);
    void readNBT(NBTTagCompound compound, T object);
    void toBytes(ByteBuf buf, T object);
    void fromBytes(ByteBuf buf, T object);
}
