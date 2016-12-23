package com.superRainbowNinja.aincog.util.DataHandle;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;

import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * Created by SuperRainbowNinja on 23/12/2016.
 */

public class IntegerHandle<T> extends FieldHandleImp<T, Integer> {
    public IntegerHandle(String nameIn, Function<T, Integer> getterIn, BiConsumer<T, Integer> setterIn) {
        super(nameIn, getterIn, setterIn);
    }

    @Override
    public void writeNBT(NBTTagCompound compound, T object) {
        compound.setInteger(name, getter.apply(object));
    }

    @Override
    public void readNBT(NBTTagCompound compound, T object) {
        setter.accept(object, compound.getInteger(name));
    }

    @Override
    public void toBytes(ByteBuf buf, T object) {
        buf.writeInt(getter.apply(object));
    }

    @Override
    public void fromBytes(ByteBuf buf, T object) {
        setter.accept(object, buf.readInt());
    }
}
