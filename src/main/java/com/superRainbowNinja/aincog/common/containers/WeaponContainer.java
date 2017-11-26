package com.superRainbowNinja.aincog.common.containers;

import com.superRainbowNinja.aincog.common.inventorys.FakeInventory;
import com.superRainbowNinja.aincog.common.items.ICore;
import com.superRainbowNinja.aincog.common.items.PoweredWeapon;
import com.superRainbowNinja.aincog.common.network.InventoryUpdate;
import com.superRainbowNinja.aincog.common.network.PacketHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagInt;

import javax.annotation.Nullable;

/**
 * Created by SuperRainbowNinja on 10/10/2016.
 *
 */
public class WeaponContainer  extends Container {
    //Maybe i dont need a refrence to this?
    private FakeInventory weaponInv;

    public int getWeaponSlot() {
        return weaponSlot;
    }

    public boolean isDirty() {
        return dirty;
    }

    public void swordUpdated() {
        dirty = false;
    }

    private boolean dirty = false;
    private int weaponSlot;
    private boolean canUse = true;
    private EntityPlayer player;

    //called by client (the gui)
    public WeaponContainer(EntityPlayer playerInv, int weaponSlotIn, boolean b) {
        this(playerInv, weaponSlotIn);
    }

    public WeaponContainer(EntityPlayer playerIn, int weaponSlotIn) {
        weaponSlot = weaponSlotIn;
        player = playerIn;

        IInventory playerInv = player.inventory;
        ItemStack weaponStack = playerInv.getStackInSlot(weaponSlot);

        weaponInv = new FakeInventory("container.sword", 1, new ItemStack[]{
                PoweredWeapon.getCore(weaponStack)
        });
        // Slot ID 0
        addSlotToContainer(new CoreSlot(weaponInv, 0, 80, 17 + 18, this));

        // Player Inventory, Slot 9-35, Slot IDs 1-27, co stolen from bedrock miners tutorial
        for (int y = 0; y < 3; ++y) {
            for (int x = 0; x < 9; ++x) {
                addSlotToContainer(new Slot(playerInv, x + y * 9 + 9, 8 + x * 18, 84 + y * 18));
            }
        }

        // Player Inventory, Slot 0-8, Slot IDs 28-36
        for (int x = 0; x < 9; ++x) {
            if (x == weaponSlotIn) {
                addSlotToContainer(new LockedSlot(playerInv, x, 8 + x * 18, 142));
            } else {
                addSlotToContainer(new Slot(playerInv, x, 8 + x * 18, 142));
            }
        }
    }

    public void slotChange() {
        if (player.inventory.getStackInSlot(weaponSlot) == null) {
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
            if (stack != null) {
                player.dropItem(stack, false);
            }
        }
        canUse = false;
    }

    @Override
    public void onContainerClosed(EntityPlayer playerIn) {
        //if (!playerIn.getEntityWorld().isRemote) {
            //PacketHandler.sendTo((EntityPlayerMP) playerIn, new InventoryUpdate(playerIn.inventory.getStackInSlot(weaponSlot), weaponSlot));
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
        ItemStack previous = null;
        Slot slot = this.inventorySlots.get(fromSlot);

        if (slot != null && slot.getHasStack()) {
            ItemStack current = slot.getStack();
            previous = current.copy();

            if (fromSlot < 1) {
                // From TE Inventory to Player Inventory
                if (!this.mergeItemStack(current, 1, 37, true))
                    return null;
            } else {
                // From Player Inventory to TE Inventory
                if (!this.mergeItemStack(current, 0, 1, false))
                    return null;
            }

            if (current.stackSize == 0)
                slot.putStack(null);
            else
                slot.onSlotChanged();

            if (current.stackSize == previous.stackSize)
                return null;
            slot.onPickupFromSlot(playerIn, current);
        }
        return previous;
    }

    public static class CoreSlot extends Slot {
        private WeaponContainer gui;

        public CoreSlot(IInventory inventoryIn, int index, int xPosition, int yPosition, WeaponContainer playerIn) {
            super(inventoryIn, index, xPosition, yPosition);
            gui = playerIn;
        }

        @Override
        public boolean isItemValid(@Nullable ItemStack stack) {
            return stack != null && stack.getItem() instanceof ICore;
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