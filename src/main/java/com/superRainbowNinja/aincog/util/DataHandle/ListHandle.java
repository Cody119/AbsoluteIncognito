package com.superRainbowNinja.aincog.util.DataHandle;

import com.superRainbowNinja.aincog.util.BufferUtils;
import com.superRainbowNinja.aincog.util.ExactPosition;
import com.superRainbowNinja.aincog.util.NBTUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * Created by SuperRainbowNinja on 26/12/2016.
 */

public class ListHandle<T, R> extends FieldHandleImp<T,List<R>> {

    public static <X> ListHandle<X, ItemStack> getInvListHandle(String name, Function<X, List<ItemStack>> getterIn, BiConsumer<X, List<ItemStack>> setterIn) {
        return new ListHandle<X, ItemStack>(name, getterIn, setterIn, NBTUtils.getItemStackTag, NBTUtils.getItemStack, ByteBufUtils::writeItemStack, ByteBufUtils::readItemStack);
    }

    public static <X> ListHandle<X, ExactPosition> getPosListHandle(String name, Function<X, List<ExactPosition>> getterIn, BiConsumer<X, List<ExactPosition>> setterIn) {
        return new ListHandle<X, ExactPosition>(name, getterIn, setterIn, NBTUtils.getPositionTag, NBTUtils.getPosition, ExactPosition::readPosition, ExactPosition::new);
    }

    private Function<Integer, List<R>> listConstructor = ArrayList::new;
    private int tagId = NBTUtils.TAG_COMPOUND;
    private Function<R, NBTBase> tagMaker;
    private Function<NBTBase, R> tagReader;
    private BiConsumer<ByteBuf, R> writer;
    private Function<ByteBuf, R> reader;

    public ListHandle(String nameIn, Function<T, List<R>> getterIn, BiConsumer<T, List<R>> setterIn,
                      Function<R, NBTBase> tagMakerIn, Function<NBTBase, R> tagReaderIn,
                      BiConsumer<ByteBuf, R> writerIn, Function<ByteBuf, R> readerIn) {
        super(nameIn, getterIn, setterIn);
        tagMaker = tagMakerIn;
        tagReader = tagReaderIn;
        writer = writerIn;
        reader = readerIn;
    }

    public ListHandle<T, R> setListConstructor(Function<Integer, List<R>> listConstructorIn) {
        listConstructor = listConstructorIn;
        return this;
    }

    public ListHandle<T, R> setTagId(int tagIdIn) {
        tagId = tagIdIn;
        return this;
    }

    @Override
    public void write(NBTTagCompound compound, T object) {
        NBTUtils.writeList(getter.apply(object), compound, name, tagMaker);
    }

    @Override
    public void write(ByteBuf buf, T object) {
        BufferUtils.writeList(buf, getter.apply(object), writer);
    }

    @Override
    List<R> readObject(NBTTagCompound compound) {
        return NBTUtils.readList(compound, name, tagReader, tagId, listConstructor);
    }

    @Override
    List<R> readObject(ByteBuf buf) {
        return  BufferUtils.readList(buf, reader, listConstructor);
    }
}
