package com.superRainbowNinja.aincog.util.DataHandle;

import com.superRainbowNinja.aincog.util.ExactPosition;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagList;

import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * Created by SuperRainbowNinja on 24/12/2016.
 */
public class ExactPositionHandle<T> extends FieldHandleImp<T, ExactPosition> {
    public ExactPositionHandle(String nameIn, Function<T, ExactPosition> getterIn, BiConsumer<T, ExactPosition> setterIn) {
        super(nameIn, getterIn, setterIn);
    }

    @Override
    public void write(NBTTagCompound compound, T object) {
        compound.setTag(name, getter.apply(object).getTag(new NBTTagCompound()));
    }

    @Override
    public void write(ByteBuf buf, T object) {
        getter.apply(object).toBytes(buf);
    }

    @Override
    ExactPosition readObject(NBTTagCompound compound) {
        return new ExactPosition(compound);
    }

    @Override
    ExactPosition readObject(ByteBuf buf) {
        return new ExactPosition(buf);
    }
}
