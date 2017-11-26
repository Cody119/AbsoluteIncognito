package com.superRainbowNinja.aincog.util.DataHandle;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Created by SuperRainbowNinja on 23/12/2016.
 */
public interface IFieldHandle<T> {
    String getName();
    void write(NBTTagCompound compound, T object);
    void read(NBTTagCompound compound, T object);
    Object read(NBTTagCompound tag);
    void write(ByteBuf buf, T object);
    void read(ByteBuf buf, T object);
    Object read(ByteBuf buf);
    void read(Object data, T object);
}
