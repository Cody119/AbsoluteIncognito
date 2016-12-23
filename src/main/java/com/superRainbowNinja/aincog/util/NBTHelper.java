package com.superRainbowNinja.aincog.util;

import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.*;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.network.ByteBufUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Created by SuperRainbowNinja on 6/10/2016.
 *
 * Nbt helper, pretty self explanatory
 */
public final class NBTHelper {
    private NBTHelper() {} //This is how java.lang.Math does it, who am i to argue?

    //call before using any of the following functions
    public static void tagCheck(ItemStack itemStack) {
        if (!itemStack.hasTagCompound()) itemStack.setTagCompound(new NBTTagCompound());
    }

    public static boolean setBool(ItemStack itemStack, String key, boolean value) {
        itemStack.getTagCompound().setBoolean(key, value);
        return value;
    }

    public static boolean getBool(ItemStack itemStack, String key) {
        return itemStack.getTagCompound().getBoolean(key);
    }

    public static boolean invBool(ItemStack itemStack, String key) {
        return setBool(itemStack, key, !getBool(itemStack, key));
    }

    public static void setInt(ItemStack itemStack, String key, int value) {
        itemStack.getTagCompound().setInteger(key, value);
    }

    public static int getInt(ItemStack itemStack, String key) {
        return itemStack.getTagCompound().getInteger(key);
    }

    public static <T> void writeArray(T[] inv, NBTTagCompound compound, String saveName, ITagReaderWriter<T> tagMaker) {
        compound.setInteger(saveName + ":Length", inv.length);
        NBTTagList nbttaglist = new NBTTagList();
        for (int i = 0; i < inv.length; ++i)
        {
            if (inv[i] != null)
            {
                NBTTagCompound nbttagcompound = new NBTTagCompound();
                nbttagcompound.setByte("Slot", (byte)i);
                nbttagcompound.setTag("Content", tagMaker.getTag(inv[i]));
                nbttaglist.appendTag(nbttagcompound);
            }
        }
        compound.setTag(saveName, nbttaglist);
    }

    public static <T> T[] readArray(NBTTagCompound compound, String loadName, ITagReaderWriter<T> tagMaker, Function<Integer, T[]> arrayMaker) {
        NBTTagList nbttaglist = compound.getTagList(loadName, tagMaker.getTagType());
        T ret[] = arrayMaker.apply(compound.getInteger(loadName + ":Length"));

        for (int i = 0; i < nbttaglist.tagCount(); ++i)
        {
            NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
            int j = nbttagcompound.getByte("Slot");

            if (j >= 0 && j < ret.length)
            {
                ret[j] = tagMaker.getObject(nbttagcompound.getCompoundTag("Content"));
            }
        }

        return ret;
    }

    public static void writeObject(NBTTagCompound compound, String key, Consumer<NBTTagCompound> writer) {
        NBTTagCompound newTag = new NBTTagCompound();
        writer.accept(newTag);
        compound.setTag(key, newTag);
    }

    public static void readObject(NBTTagCompound compound, String key, Consumer<NBTTagCompound> reader) {
        reader.accept(compound.getCompoundTag(key));
    }

    public static <T> void writeList(List<T> list, NBTTagCompound compound, String saveName, ITagReaderWriter<T> tagMaker) {
        //compound.setInteger(saveName + ":Length", list.size());
        NBTTagList nbttaglist = new NBTTagList();
        for (int i = 0; i < list.size(); ++i) {
            nbttaglist.appendTag(tagMaker.getTag(list.get(i)));
        }
        compound.setTag(saveName, nbttaglist);
    }

    public static <T> List<T> readList(NBTTagCompound compound, String loadName, ITagReaderWriter<T> tagReader, Function<Integer, List<T>> listMaker) {
        NBTTagList nbttaglist = compound.getTagList(loadName, tagReader.getTagType());
        List<T> ret = listMaker.apply(nbttaglist.tagCount());//compound.getInteger(loadName + ":Length"));

        for (int i = 0; i < nbttaglist.tagCount(); ++i) {
            ret.add(tagReader.getObject(nbttaglist.get(i)));
        }
        return ret;
    }

    public interface ITagReaderWriter <T> {
        T getObject(NBTBase tag);
        NBTBase getTag(T object);
        //the tag id, can be obtained by callin the getId method on any tag type (must instantiate a new one if u dont have one)
        int getTagType();
    }

    private static final ITagReaderWriter<ItemStack> InvReadWriter = new ITagReaderWriter<ItemStack>() {
        @Override
        public ItemStack getObject(NBTBase tag) {
            return ItemStack.loadItemStackFromNBT((NBTTagCompound) tag);
        }

        @Override
        public NBTBase getTag(ItemStack object) {
            return object.writeToNBT(new NBTTagCompound());
        }

        @Override
        public int getTagType() {
            return (new NBTTagCompound()).getId();
        }
    };

    private static final ITagReaderWriter<ExactPosition> CompPosReadWriter = new ITagReaderWriter<ExactPosition>() {
        @Override
        public ExactPosition getObject(NBTBase tag) {
            return new ExactPosition((NBTTagCompound) tag);
        }

        @Override
        public NBTBase getTag(ExactPosition object) {
            return object.getTag(new NBTTagCompound());
        }

        @Override
        public int getTagType() {
            return (new NBTTagCompound()).getId();
        }
    };

    public static ItemStack[] readInventoryArray(NBTTagCompound compound, String loadName) {
        return NBTHelper.<ItemStack>readArray(compound, loadName, InvReadWriter, ItemStack[]::new);
    }

    public static void writeInventoryArray(ItemStack[] inv, NBTTagCompound compound, String saveName) {
        NBTHelper.<ItemStack>writeArray(inv, compound, saveName, InvReadWriter);
    }

    public static ArrayList<ItemStack> readInventory(NBTTagCompound compound, String loadName) {
        return (ArrayList<ItemStack>)NBTHelper.readList(compound, loadName, InvReadWriter, ArrayList::new);
    }

    public static void writeInventory(List<ItemStack> inv, NBTTagCompound compound, String saveName) {
        NBTHelper.<ItemStack>writeList(inv, compound, saveName, InvReadWriter);
    }

    public static ArrayList<ExactPosition> readPositions(NBTTagCompound compound, String loadName) {
        return (ArrayList<ExactPosition>)NBTHelper.readList(compound, loadName, CompPosReadWriter, ArrayList::new);
    }

    public static void writePositions(List<ExactPosition> pos, NBTTagCompound compound, String saveName) {
        NBTHelper.writeList(pos, compound, saveName, CompPosReadWriter);
    }




    //move
    public static void writeFluid(ByteBuf buf, FluidStack stack) {
        if (stack == null) {
            ByteBufUtils.writeUTF8String(buf, "");
            return;
        }
        ByteBufUtils.writeUTF8String(buf, stack.getFluid().getName());
        buf.writeInt(stack.amount);
        ByteBufUtils.writeTag(buf, stack.tag);
    }

    public static FluidStack readFluid(ByteBuf buf) {
        Fluid fluid = FluidRegistry.getFluid(ByteBufUtils.readUTF8String(buf));
        if (fluid != null) {
            return new FluidStack(fluid, buf.readInt(), ByteBufUtils.readTag(buf));
        }
        return null;
    }
}
