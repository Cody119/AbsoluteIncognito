package com.superRainbowNinja.aincog.util.DataHandle;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;

import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * Created by SuperRainbowNinja on 26/12/2016.
 */
public class BooleanHandle<T> extends FieldHandleImp<T, Boolean> {
    public BooleanHandle(String nameIn, Function<T, Boolean> getterIn, BiConsumer<T, Boolean> setterIn) {
        super(nameIn, getterIn, setterIn);
    }

    @Override
    public void writeNBT(NBTTagCompound compound, T object) {
        compound.setBoolean(name, getter.apply(object));
    }

    @Override
    public void readNBT(NBTTagCompound compound, T object) {
        setter.accept(object, compound.getBoolean(name));
    }

    @Override
    public void toBytes(ByteBuf buf, T object) {
        buf.writeBoolean(getter.apply(object));
    }

    @Override
    public void fromBytes(ByteBuf buf, T object) {
        setter.accept(object, buf.readBoolean());
    }
}
