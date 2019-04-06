package com.superRainbowNinja.aincog.util;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.*;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Created by SuperRainbowNinja on 6/10/2016.
 *
 * Nbt helper, pretty self explanatory
 */
public final class NBTUtils {
    private NBTUtils() {}

    public static final int TAG_COMPOUND = new NBTTagCompound().getId();

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

    public static void writeObject(NBTTagCompound compound, String key, Consumer<NBTTagCompound> writer) {
        NBTTagCompound newTag = new NBTTagCompound();
        writer.accept(newTag);
        compound.setTag(key, newTag);
    }

    public static void readObject(NBTTagCompound compound, String key, Consumer<NBTTagCompound> reader) {
        reader.accept(compound.getCompoundTag(key));
    }

    public static <T> void writeList(List<T> list, NBTTagCompound compound, String saveName, Function<T, NBTBase> tagMaker) {
        //compound.setInteger(saveName + ":Length", list.size());
        NBTTagList nbttaglist = new NBTTagList();
        for (int i = 0; i < list.size(); ++i) {
            nbttaglist.appendTag(tagMaker.apply(list.get(i)));
        }
        compound.setTag(saveName, nbttaglist);
    }

    public static <T> List<T> readList(NBTTagCompound compound, String loadName, Function<NBTBase, T> tagReader, int tagNumber, Function<Integer, List<T>> listMaker) {
        NBTTagList nbttaglist = compound.getTagList(loadName, tagNumber);
        List<T> ret = listMaker.apply(nbttaglist.tagCount());//compound.getInteger(loadName + ":Length"));

        for (int i = 0; i < nbttaglist.tagCount(); ++i) {
            ret.add(tagReader.apply(nbttaglist.get(i)));
        }
        return ret;
    }

    public static <T> void writeArray(T[] inv, NBTTagCompound compound, String saveName, Function<T, NBTBase> tagMaker) {
        compound.setInteger(saveName + ":Length", inv.length);
        NBTTagList nbttaglist = new NBTTagList();
        for (int i = 0; i < inv.length; ++i) {
            if (inv[i] != null) {
                NBTTagCompound nbttagcompound = new NBTTagCompound();
                nbttagcompound.setByte("Slot", (byte)i);
                nbttagcompound.setTag("Content", tagMaker.apply(inv[i]));
                nbttaglist.appendTag(nbttagcompound);
            }
        }
        compound.setTag(saveName, nbttaglist);
    }

    public static <T> T[] readArray(NBTTagCompound compound, String loadName, Function<NBTBase, T> tagReader, int tagNumber, Function<Integer, T[]> arrayMaker) {
        NBTTagList nbttaglist = compound.getTagList(loadName, tagNumber);

        T[] ret = arrayMaker.apply(compound.getInteger(loadName + ":Length"));

        for (int i = 0; i < nbttaglist.tagCount(); ++i) {
            NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
            int j = nbttagcompound.getByte("Slot");

            if (j >= 0 && j < ret.length) {
                ret[j] = tagReader.apply(nbttagcompound.getCompoundTag("Content"));
            }
        }
        return ret;
    }

    //These 2 methods should mirror writeArray so they may be used interchangably
    public static void writeInventory(IInventory inv, NBTTagCompound compound, String saveName) {
        int size = inv.getSizeInventory();
        compound.setInteger(saveName + ":length", size);
        NBTTagList nbttaglist = new NBTTagList();
        for (int i = 0; i < size; ++i) {
            ItemStack stack = inv.getStackInSlot(i);
            if (stack != null) {
                NBTTagCompound nbttagcompound = new NBTTagCompound();
                nbttagcompound.setByte("Slot", (byte)i);
                nbttagcompound.setTag("Content", stack.writeToNBT(new NBTTagCompound()));
                nbttaglist.appendTag(nbttagcompound);
            }
        }
        compound.setTag(saveName, nbttaglist);
    }

    public static int readInvLength(NBTTagCompound compound, String loadName) {
        return compound.getInteger(loadName + ":length");
    }

    public static void readInventory(IInventory inv, NBTTagCompound compound, String loadName) {
        int size = inv.getSizeInventory();
        NBTTagList list = compound.getTagList(loadName, TAG_COMPOUND);
        for (int i = 0; i < list.tagCount(); i++) {
            NBTTagCompound nbttagcompound = list.getCompoundTagAt(i);
            int j = nbttagcompound.getByte("Slot");

            if (j >= 0 && j < size) {
                inv.setInventorySlotContents(j, new ItemStack(nbttagcompound.getCompoundTag("Content")));
            }
        }
    }

    public static final Function<NBTBase, ItemStack> getItemStack = (tag) -> new ItemStack((NBTTagCompound) tag);
    public static final Function<ItemStack, NBTBase> getItemStackTag = (item) -> item.writeToNBT(new NBTTagCompound());

    public static final Function<NBTBase, ExactPosition> getPosition = (tag) -> new ExactPosition((NBTTagCompound) tag);
    public static final Function<ExactPosition, NBTBase> getPositionTag = (pos) -> pos.getTag(new NBTTagCompound());


    public static ItemStack[] readInventoryArray(NBTTagCompound compound, String loadName) {
        return NBTUtils.<ItemStack>readArray(compound, loadName, getItemStack, TAG_COMPOUND, ItemStack[]::new);
    }

    public static void writeInventoryArray(ItemStack[] inv, NBTTagCompound compound, String saveName) {
        NBTUtils.<ItemStack>writeArray(inv, compound, saveName, getItemStackTag);
    }

    public static ArrayList<ItemStack> readInventory(NBTTagCompound compound, String loadName) {
        return (ArrayList<ItemStack>) NBTUtils.readList(compound, loadName, getItemStack, TAG_COMPOUND, ArrayList::new);
    }

    public static void writeInventory(List<ItemStack> inv, NBTTagCompound compound, String saveName) {
        NBTUtils.<ItemStack>writeList(inv, compound, saveName, getItemStackTag);
    }

    public static ArrayList<ExactPosition> readPositions(NBTTagCompound compound, String loadName) {
        return (ArrayList<ExactPosition>) NBTUtils.readList(compound, loadName, getPosition, TAG_COMPOUND, ArrayList::new);
    }

    public static void writePositions(List<ExactPosition> pos, NBTTagCompound compound, String saveName) {
        NBTUtils.writeList(pos, compound, saveName, getPositionTag);
    }
}
