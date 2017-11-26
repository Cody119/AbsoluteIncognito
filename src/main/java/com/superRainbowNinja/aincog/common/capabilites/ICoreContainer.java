package com.superRainbowNinja.aincog.common.capabilites;

import com.superRainbowNinja.aincog.common.items.ICore;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;

/**
 * Created by SuperRainbowNinja on 26/11/2017.
 */
public interface ICoreContainer extends IItemHandler {
    @CapabilityInject(ICoreContainer.class)
    Capability<IPoweredWeaponCap> CORE_CONTAINER_CAP = null;

    //returning true indicates the core is broken (not out of energy, broken)
    boolean setCoreDamage(int dmg);
    int getCoreMaxDamage();
    int getCoreDamage();

    boolean trySetCore(ItemStack stack, boolean simulate);
    void loseCore();

    void setCore(ItemStack stack);

    @Nullable
    ItemStack getCoreItemStack();
    @Nullable
    ICore getCoreItem();

    default boolean hasCore() {
        return getCoreItemStack() != null;
        //return true;
    }

    default int getSlots() {
        return 1;
    }

    default ItemStack getStackInSlot(int slot){
        return getCoreItemStack();
    }

    default ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        if (trySetCore(stack, simulate)) {
            return null;
        } else {
            return stack;
        }
    }

    default ItemStack extractItem(int slot, int amount, boolean simulate) {
        if (hasCore()) {
            ItemStack cur = getCoreItemStack();
            ItemStack ret = cur.splitStack(amount);
            if (cur.stackSize > 0) {
                setCore(cur); //this is mostly just to update the nbt and make sure nothing strange happens
            } else {
                loseCore();
            }
            return ret;
        } else {
            return null;
        }
    }

    static ICoreContainer getCap(ItemStack stack) {
        return stack.getCapability(CORE_CONTAINER_CAP, EnumFacing.DOWN);
    }

    static boolean hasCore(ItemStack stack) {
        return stack.getCapability(IPoweredWeaponCap.POWERED_WEAPON_CAP, EnumFacing.DOWN).hasCore();
    }

}
