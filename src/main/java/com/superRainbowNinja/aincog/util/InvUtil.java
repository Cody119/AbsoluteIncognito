package com.superRainbowNinja.aincog.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemHandlerHelper;

import java.util.function.Predicate;

/**
 * Created by SuperRainbowNinja on 8/12/2016.
 */
public class InvUtil {

    private InvUtil() {}

    public static boolean insertIntoSlotFromPlayer(EntityPlayer player, IInventory inv, int slot, ItemStack heldItem, Predicate<Item> test) {
        if (test.test(heldItem.getItem())) {
            return insertIntoSlotFromPlayer(player, inv, slot, heldItem);
        }
        return false;
    }

    public static boolean insertIntoSlotFromPlayer(EntityPlayer player, IInventory inv, int slot, ItemStack heldItem) {
        ItemStack stack = inv.getStackInSlot(slot);
        if (stack == null) {
            inv.setInventorySlotContents(slot, heldItem.copy());
            player.inventory.removeStackFromSlot(player.inventory.currentItem);
            return true;
        } else if (ItemHandlerHelper.canItemStacksStack(heldItem, stack)){
            int amount = Math.min(stack.getMaxStackSize() - stack.stackSize, heldItem.stackSize);
            stack.stackSize += amount;
            heldItem.stackSize -= amount;
            if (heldItem.stackSize == 0) {
                player.inventory.removeStackFromSlot(player.inventory.currentItem);
            }
            if (amount != 0) {
                return true;
            }
        }
        return false;
    }

    public static boolean insertIntoInvFromPlayer(EntityPlayer player, IInventory inv, int slot, ItemStack heldItem) {
        if (insertIntoSlotFromPlayer(player, inv, slot, heldItem)) {
            return true;
        } else {
            for (int i = 0; i < inv.getSizeInventory(); i++) {
                if (i == slot)
                    continue;
                if (insertIntoSlotFromPlayer(player, inv, i, heldItem))
                    return true;
            }
        }
        return false;
    }
}
