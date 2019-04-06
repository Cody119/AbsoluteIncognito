package com.superRainbowNinja.aincog.common.containers;

import com.superRainbowNinja.aincog.common.inventorys.FakeInventory;
import com.superRainbowNinja.aincog.common.items.CoreContainerItem;
import com.superRainbowNinja.aincog.common.items.ICore;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;

/**
 * Created by SuperRainbowNinja on 10/10/2016.
 *
 */
public class CoreContainer extends Container {
    //Maybe i dont need a refrence to this?
    private FakeInventory itemInv;

    public int getItemSlot() {
        return itemSlot;
    }

    public boolean isDirty() {
        return dirty;
    }

    public void itemUpdated() {
        dirty = false;
    }

    private boolean dirty = false;
    private int itemSlot;
    private boolean canUse = true;
    private EntityPlayer player;

    public int tmp;

    //called by client (the gui)
    public CoreContainer(EntityPlayer playerInv, int weaponSlotIn, boolean b) {
        this(playerInv, weaponSlotIn);
    }

    public CoreContainer(EntityPlayer playerIn, int weaponSlotIn) {
        itemSlot = weaponSlotIn;
        player = playerIn;

        IInventory playerInv = player.inventory;
        ItemStack weaponStack = playerInv.getStackInSlot(itemSlot);

        itemInv = new FakeInventory("container.sword", 1, new ItemStack[]{
                CoreContainerItem.getCore(weaponStack)
        });
        // Slot ID 0
        addSlotToContainer(new CoreSlot(itemInv, 0, 80, 17 + 18, this));

        // Player Inventory, Slot 9-35, Slot IDs 1-27, co stolen from bedrock miners tutorial
        for (int y = 0; y < 3; ++y) {
            for (int x = 0; x < 9; ++x) {
                addSlotToContainer(new Slot(playerInv, x + y * 9 + 9, 8 + x * 18, 84 + y * 18));
            }
        }

        // Player Inventory, Slot 0-8, Slot IDs 28-36
        for (int x = 0; x < 9; ++x) {
            if (x == weaponSlotIn) {
                tmp = inventorySlots.size();
                addSlotToContainer(new LockedSlot(playerInv, x, 8 + x * 18, 142));
            } else {
                addSlotToContainer(new Slot(playerInv, x, 8 + x * 18, 142));
            }
        }
    }

    public void slotChange() {
        if (player.inventory.getStackInSlot(itemSlot).isEmpty()) {
            // This means the item disappeared from where we
            // excepted it to be.... not good
            // the code is in place 2 throw the whatever is in the core thisStack into the world
            // although since the sword has been moved without being updated this will likely dupe it,
            // so should be fine not doing that
            closeInv(false);
        } else {
            dirty = true;
        }
    }

    public void closeInv(boolean throwItem) {
        if (throwItem) {
            ItemStack stack = inventorySlots.get(0).getStack();
            if (!stack.isEmpty()) {
                player.dropItem(stack, false);
            }
        }
        canUse = false;
    }

    @Override
    public void onContainerClosed(EntityPlayer playerIn) {
        //if (!playerIn.getEntityWorld().isRemote) {
            //PacketHandler.sendTo((EntityPlayerMP) playerIn, new InventoryUpdate(playerIn.inventory.getStackInSlot(itemSlot), itemSlot));
        //}
        super.onContainerClosed(playerIn);
    }

    //Called every tick (maybe?, its called a lot) to c if the player can still use the inv, if this is false
    //the inv closes
    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return canUse;
    }

    //Shift click behaviour
    //Look through this later, its prty standared for inventorys
    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int fromSlot) {
//        ItemStack previous = null;
//        Slot slot = this.inventorySlots.get(fromSlot);
//
//        if (slot != null && slot.getHasStack()) {
//            ItemStack current = slot.getStack();
//            previous = current.copy();
//
//            if (fromSlot < 1) {
//                // From TE Inventory to Player Inventory
//                if (!this.mergeItemStack(current, 1, 37, true))
//                    return null;
//            } else {
//                // From Player Inventory to TE Inventory
//                if (!this.mergeItemStack(current, 0, 1, false))
//                    return null;
//            }
//
//            if (current.getCount() == 0)
//                slot.putStack(null);
//            else
//                slot.onSlotChanged();
//
//            if (current.getCount() == previous.getCount())
//                return null;
//            slot.onPickupFromSlot(playerIn, current);
//        }
//        return previous;
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(fromSlot);

        if (slot != null && slot.getHasStack()) {
            ItemStack current = slot.getStack();
            itemstack = current.copy();

            if (fromSlot < 1) {
                if (!this.mergeItemStack(current, 1, this.inventorySlots.size(), true))
                {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(current, 0, 1, false)) {
                return ItemStack.EMPTY;
            }

            if (current.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            }
            else {
                slot.onSlotChanged();
            }
        }

        return itemstack;
    }

    public static class CoreSlot extends Slot {
        private CoreContainer gui;

        public CoreSlot(IInventory inventoryIn, int index, int xPosition, int yPosition, CoreContainer playerIn) {
            super(inventoryIn, index, xPosition, yPosition);
            gui = playerIn;
        }

        @Override
        public boolean isItemValid(ItemStack stack) {
            return stack.getItem() instanceof ICore;
        }

        @Override
        public void onSlotChanged() {
            super.onSlotChanged();
            gui.slotChange();
        }
    }

    //Locked slot
    public static class LockedSlot extends Slot {
        public LockedSlot(IInventory inventoryIn, int index, int xPosition, int yPosition) {
            super(inventoryIn, index, xPosition, yPosition);
        }

        @Override
        public boolean canTakeStack(EntityPlayer playerIn)
        {
            return false;
        }
    }
}