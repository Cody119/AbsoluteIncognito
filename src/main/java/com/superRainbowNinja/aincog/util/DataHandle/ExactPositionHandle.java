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
    public void writeNBT(NBTTagCompound compound, T object) {
        compound.setTag(name, getter.apply(object).getTag(new NBTTagCompound()));
    }

    @Override
    public void readNBT(NBTTagCompound compound, T object) {
        setter.accept(object, new ExactPosition(compound));
    }

    @Override
    public void toBytes(ByteBuf buf, T object) {
        getter.apply(object).toBytes(buf);
    }

    @Override
    public void fromBytes(ByteBuf buf, T object) {
        setter.accept(object, new ExactPosition(buf));
    }
}
