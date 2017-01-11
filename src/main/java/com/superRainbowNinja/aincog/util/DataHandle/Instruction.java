package com.superRainbowNinja.aincog.util.DataHandle;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;

import java.util.function.Consumer;

/**
 * Created by SuperRainbowNinja on 24/12/2016.
 *
 * Used to execute code during the read process such as updating ceartin things
 */
public class Instruction<T> implements IFieldHandle<T> {
    private String name;
    private Consumer<T> func;

    public Instruction(String nameIn, Consumer<T> funcIn) {
        name = nameIn;
        func = funcIn;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void write(NBTTagCompound compound, T object) {}

    @Override
    public void read(NBTTagCompound compound, T object) {
        func.accept(object);
    }

    @Override
    public Object read(NBTTagCompound tag) {
        return null;
    }

    @Override
    public void write(ByteBuf buf, T object) {}

    @Override
    public void read(ByteBuf buf, T object) {
        func.accept(object);
    }

    @Override
    public Object read(ByteBuf buf) {
        return null;
    }

    @Override
    public void readFromCache(Object data, T object) {
        func.accept(object);
    }
}
