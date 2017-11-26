package com.superRainbowNinja.aincog.common.capabilites;

import com.superRainbowNinja.aincog.common.items.ICore;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nullable;

/**
 * Created by SuperRainbowNinja on 26/11/2017.
 */
public class CoreContainerImp extends CacheCap implements ICoreContainer, ICapabilitySerializable<NBTTagCompound> {
    public static final String CORE_ITEM = "CORE_ITEM";
    public static final String CAP_KEY = "CORE_CONTAINER_CAP";

    protected ItemStack core;

    public CoreContainerImp() {
        super();
    }

    public CoreContainerImp(ItemStack stackIn) {
        super(stackIn);
    }

    @Override
    protected void cache() {
        NBTTagCompound compound = thisStack.getSubCompound(CAP_KEY, true);
        if (compound.getBoolean(CORE_ITEM)) {
            core = ItemStack.loadItemStackFromNBT(compound);
        }
    }

    @Override
    public boolean setCoreDamage(int dmg) {
        cacheCheck();
        if (core != null && ((ICore) core.getItem()).setCoreDamage(core, dmg)) {
            core.writeToNBT(thisStack.getSubCompound(CAP_KEY, true));
            return true;
        }
        return false;
    }

    @Override
    public int getCoreMaxDamage() {
        cacheCheck();
        return core != null ? ((ICore)core.getItem()).getMaxCoreDamage(core) : 0;
    }

    @Override
    public int getCoreDamage() {
        cacheCheck();
        return core != null ? ((ICore)core.getItem()).getCoreDamage(core) : 0;
    }

    @Override
    public boolean trySetCore(ItemStack stack, boolean simulate) {
        cacheCheck();
        if (!hasCore() && stack != null && stack.getItem() instanceof ICore) {
            if (!simulate){
                core = stack.copy();
                core.writeToNBT(thisStack.getSubCompound(CAP_KEY, true)).setBoolean(CORE_ITEM, true);
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void loseCore() {
        core = null;
        thisStack.getSubCompound(CAP_KEY, true).setBoolean(CORE_ITEM, false);
    }

    @Override
    public void setCore(ItemStack stack) {
        cacheCheck();
        if (stack != null && stack.getItem() instanceof ICore) {
            core = stack.copy();
            core.writeToNBT(thisStack.getSubCompound(CAP_KEY, true)).setBoolean(CORE_ITEM, true);
        } else {
            throw new RuntimeException("Tried to set invalid core into power weapon cap: " + stack);
        }
    }

    @Nullable
    @Override
    public ItemStack getCoreItemStack() {
        cacheCheck();
        return core;
    }

    @Nullable
    @Override
    public ICore getCoreItem() {
        cacheCheck();
        return core == null ? null : (ICore) core.getItem();
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == ICoreContainer.CORE_CONTAINER_CAP || capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;
    }

    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        return capability == ICoreContainer.CORE_CONTAINER_CAP || capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ?
                (T) this : null;
    }

    @Override
    public NBTTagCompound serializeNBT() {
        return null;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {

    }
}
