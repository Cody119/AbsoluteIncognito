package com.superRainbowNinja.aincog.util.DataHandle;

import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;

import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * Created by SuperRainbowNinja on 23/12/2016.
 *
 */

public class ItemStackHandler<T> extends FieldHandleImp<T, ItemStack> {
    public ItemStackHandler(String nameIn, Function<T, ItemStack> getterIn, BiConsumer<T, ItemStack> setterIn) {
        super(nameIn, getterIn, setterIn);
    }

    @Override
    public void write(NBTTagCompound compound, T object) {
        ItemStack stack = getter.apply(object);
        if (stack != null)
            compound.setTag(name, stack.writeToNBT(new NBTTagCompound()));
    }

    @Override
    public void write(ByteBuf buf, T object) {
        ByteBufUtils.writeItemStack(buf, getter.apply(object));
    }

    @Override
    ItemStack readObject(NBTTagCompound compound) {
        return ItemStack.loadItemStackFromNBT(compound.getCompoundTag(name));
    }

    @Override
    ItemStack readObject(ByteBuf buf) {
        return  ByteBufUtils.readItemStack(buf);
    }
}
