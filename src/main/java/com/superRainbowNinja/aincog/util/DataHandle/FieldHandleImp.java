package com.superRainbowNinja.aincog.util.DataHandle;


import com.superRainbowNinja.aincog.common.tileEntity.MachineFrameTile;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;

import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * Created by SuperRainbowNinja on 23/12/2016.
 */
public abstract class FieldHandleImp<T, R> implements IFieldHandle<T> {
    protected final Function<T, R> getter;
    protected final BiConsumer<T, R> setter;
    protected final String name;

    public FieldHandleImp(String nameIn, Function<T, R> getterIn, BiConsumer<T, R> setterIn) {
        getter = getterIn;
        setter = setterIn;
        name = nameIn;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void read(NBTTagCompound compound, T object) {
        setter.accept(object, readObject(compound));
    }

    @Override
    public Object read(NBTTagCompound tag) {
        return readObject(tag);
    }

    @Override
    public void read(ByteBuf buf, T object) {
        setter.accept(object, readObject(buf));
    }

    @Override
    public Object read(ByteBuf buf) {
        return readObject(buf);
    }

    @Override
    public void readFromCache(Object data, T object) {
        //this should be prtty safe provided subclasses are only used in a data bundle
        setter.accept(object, (R) data);
    }

    abstract R readObject(NBTTagCompound compound);
    abstract R readObject(ByteBuf buf);
}
