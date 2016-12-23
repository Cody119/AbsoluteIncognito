package com.superRainbowNinja.aincog.util.DataHandle;

import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;

import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * Created by SuperRainbowNinja on 23/12/2016.
 */

public class ItemStackHandler<T> extends FieldHandleImp<T, ItemStack> {
    public ItemStackHandler(String nameIn, Function<T, ItemStack> getterIn, BiConsumer<T, ItemStack> setterIn) {
        super(nameIn, getterIn, setterIn);
    }

    @Override
    public void writeNBT(NBTTagCompound compound, T object) {
        compound.setTag(name, getter.apply(object).writeToNBT(new NBTTagCompound()));
    }

    @Override
    public void readNBT(NBTTagCompound compound, T object) {
        setter.accept(object, ItemStack.loadItemStackFromNBT(compound.getCompoundTag(name)));
    }

    @Override
    public void toBytes(ByteBuf buf, T object) {
        ByteBufUtils.writeItemStack(buf, getter.apply(object));
    }

    @Override
    public void fromBytes(ByteBuf buf, T object) {
        setter.accept(object, ByteBufUtils.readItemStack(buf));
    }
}
