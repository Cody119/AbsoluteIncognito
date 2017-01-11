package com.superRainbowNinja.aincog.util.DataHandle;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;

import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * Created by SuperRainbowNinja on 23/12/2016.
 *
 * this class needs the values array for the enum (gotten by calling [Your Enum].values()
 */

public class EnumHandler<T, R extends Enum<R>> extends FieldHandleImp<T, R> {
    protected R[] values;

    public EnumHandler(String nameIn, Function<T, R> getterIn, BiConsumer<T, R> setterIn, R[] valuesIn) {
        super(nameIn, getterIn, setterIn);
        values = valuesIn;
    }

    private R get(int i) {
        return values[i];
    }
    private int get(R val) {
        return val.ordinal();
    }

    @Override
    public void write(NBTTagCompound compound, T object) {
        compound.setInteger(name, get(getter.apply(object)));
    }

    @Override
    public void write(ByteBuf buf, T object) {
        buf.writeInt(get(getter.apply(object)));
    }

    @Override
    R readObject(NBTTagCompound compound) {
        return get(compound.getInteger(name));
    }

    @Override
    R readObject(ByteBuf buf) {
        return get(buf.readInt());
    }
}
