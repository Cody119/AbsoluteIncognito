package com.superRainbowNinja.aincog.util.DataHandle;

import com.mojang.realmsclient.util.Pair;
import com.superRainbowNinja.aincog.util.BufferUtils;
import com.superRainbowNinja.aincog.util.NBTUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * Created by SuperRainbowNinja on 25/12/2016.
 */
public class InvHandle<T> implements IFieldHandle<T> {

    protected String name;
    protected Function<T, IInventory> getter;
    protected BiConsumer<T, Integer> sizeHandle;

    public InvHandle(String nameIn, Function<T, IInventory> getterIn) {
        this(nameIn, getterIn, null);
    }

    public InvHandle(String nameIn, Function<T, IInventory> getterIn, BiConsumer<T, Integer> reSizer) {
        name = nameIn;
        getter = getterIn;
        sizeHandle = reSizer;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void write(NBTTagCompound compound, T object) {
        NBTUtils.writeInventory(getter.apply(object), compound, name);
    }

    @Override
    public void read(NBTTagCompound compound, T object) {
        if (sizeHandle != null) {
            sizeHandle.accept(object, NBTUtils.readInvLength(compound, name));
        }
        NBTUtils.readInventory(getter.apply(object), compound, name);
    }

    @Override
    public Object read(NBTTagCompound tag) {
        //return Pair.of(NBTUtils.readInvLength(tag, name), NBTUtils.readInventory(getter.apply(object), tag, name));
        return NBTUtils.readInventoryArray(tag, name);
    }

    @Override
    public void write(ByteBuf buf, T object) {
        BufferUtils.writeInventory(buf, getter.apply(object), sizeHandle != null);
    }

    @Override
    public void read(ByteBuf buf, T object) {
        IInventory inv = getter.apply(object);
        if (sizeHandle != null) {
            sizeHandle.accept(object, BufferUtils.readInvLength(buf));
        }
        BufferUtils.readInvItems(buf, inv);
    }

    @Override
    public Object read(ByteBuf buf) {
        return BufferUtils.readInvItems(buf);
    }

    @Override
    public void read(Object data, T object) {
        ItemStack[] inv = (ItemStack[])data;
        IInventory invMain = getter.apply(object);
        if (sizeHandle != null) {
            sizeHandle.accept(object, inv.length);
        }
        for (int i = 0; i < inv.length; i++) {
            invMain.setInventorySlotContents(i, inv[i]);
        }
    }
}
