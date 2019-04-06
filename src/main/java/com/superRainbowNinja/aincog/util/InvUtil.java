package com.superRainbowNinja.aincog.util;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemHandlerHelper;

import java.util.List;
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
        if (stack.isEmpty()) {
            inv.setInventorySlotContents(slot, heldItem.copy());
            player.inventory.removeStackFromSlot(player.inventory.currentItem);
            return true;
        } else if (ItemHandlerHelper.canItemStacksStack(heldItem, stack)){
            int amount = Math.min(stack.getMaxStackSize() - stack.getCount(), heldItem.getCount());
            stack.setCount(stack.getCount() + amount);
            heldItem.setCount(heldItem.getCount() - amount);
            if (heldItem.getCount() == 0) {
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

    public static void spawnItemStacks(World worldIn, BlockPos pos, Vec3d to, List<ItemStack> stacks) {
        for (ItemStack stack : stacks) {
            spawnItemStack(worldIn, pos, to, stack);
        }
    }

    public static void spawnItemStack(World worldIn, BlockPos pos, Vec3d to, ItemStack stack) {
        Vec3d direction = to.subtract(new Vec3d(pos));
        Vec3d norm = direction.normalize();
        Vec3d spawnPos = norm.add(new Vec3d(pos));

        EntityItem entityitem = new EntityItem(worldIn, spawnPos.x, spawnPos.y, spawnPos.z, stack);

        Vec3d velocity = norm.scale(direction.lengthVector()*0.5);
        entityitem.motionX = velocity.x;
        entityitem.motionY = velocity.y;
        entityitem.motionZ = velocity.z;
        worldIn.spawnEntity(entityitem);
    }

}
