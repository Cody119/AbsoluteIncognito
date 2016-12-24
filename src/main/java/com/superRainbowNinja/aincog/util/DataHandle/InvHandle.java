package com.superRainbowNinja.aincog.util.DataHandle;

import com.superRainbowNinja.aincog.util.BufferUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.NBTTagCompound;

import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * Created by SuperRainbowNinja on 25/12/2016.
 */
public class InvHandle<T> extends FieldHandleImp<T, IInventory> {

    protected boolean dynamicInv;
    protected BiConsumer<T, Integer> sizeHandle;

    public InvHandle(String nameIn, Function<T, IInventory> getterIn) {
        super(nameIn, getterIn, null);
        dynamicInv = false;
    }

    public InvHandle(String nameIn, Function<T, IInventory> getterIn, BiConsumer<T, IInventory> setterIn) {
        super(nameIn, getterIn, setterIn);
        dynamicInv = true;
    }

    @Override
    public void writeNBT(NBTTagCompound compound, T object) {

    }

    @Override
    public void readNBT(NBTTagCompound compound, T object) {

    }

    @Override
    public void toBytes(ByteBuf buf, T object) {
        BufferUtils.writeInventory(buf, getter.apply(object), dynamicInv);
    }

    @Override
    public void fromBytes(ByteBuf buf, T object) {
        IInventory inv = getter.apply(object);
        if (dynamicInv) {
            setter.accept(object, );
        }
    }
}
