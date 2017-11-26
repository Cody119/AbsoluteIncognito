package com.superRainbowNinja.aincog.common.inventorys;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;

/**
 * Created by SuperRainbowNinja on 25/04/2016.
 *
 * Fake is probs not quite right, but it does let you choose whats in it and how many slots there are
 * A lot of this was copyed from TileEntityChest
 * Maybe this should be moved?
 */
public class FakeInventory implements IInventory {
    private int slotCount;
    private String invName;
    private ItemStack slotStacks[];

    public FakeInventory(String name, int slotNum, ItemStack items[]) {
        slotCount = slotNum;
        invName = name;
        slotStacks = items;
    }

    @Override
    public int getSizeInventory() {
        return slotCount;
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return slotStacks[index];
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        if (slotStacks[index] != null)
        {
            if (slotStacks[index].stackSize <= count)
            {
                ItemStack itemStack1 = slotStacks[index];
                slotStacks[index] = null;
                //this.markDirty();
                return itemStack1;
            }
            else
            {
                ItemStack itemstack = slotStacks[index].splitStack(count);

                if (slotStacks[index].stackSize == 0)
                {
                    slotStacks[index] = null;
                }

                //this.markDirty();
                return itemstack;
            }
        }
        else
        {
            return null;
        }
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        if (this.slotStacks[index] != null)
        {
            ItemStack itemstack = slotStacks[index];
            slotStacks[index] = null;
            return itemstack;
        }
        else
        {
            return null;
        }
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        slotStacks[index] = stack;

        if (stack != null && stack.stackSize > getInventoryStackLimit()) {
            stack.stackSize = getInventoryStackLimit();
        }

        //this.markDirty();
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public void markDirty() {
        //Maybe put some kind of handle for this
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return true;
    }

    @Override
    public void openInventory(EntityPlayer player) {
        //Not sure what these are for exactly
    }

    @Override
    public void closeInventory(EntityPlayer player) {
        //Not sure what these are for exactly
    }

    //Used for automated thisStack insertion (not player adding of thisStack)
    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        //Not sure if this will be used, but just set it 2 true for now
        return true;
    }

    //I guess u can store extra info?
    @Override
    public int getField(int id) {
        return 0;
    }

    @Override
    public void setField(int id, int value) {

    }

    @Override
    public int getFieldCount() {
        return 0;
    }


    @Override
    public void clear() {
        for (int i = 0; i < getSizeInventory(); ++i) {
            this.slotStacks[i] = null;
        }
    }

    @Override
    public String getName() {
        return invName;
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }

    @Override
    public ITextComponent getDisplayName() {
        return null;
    }
}
