package com.superRainbowNinja.aincog.common.capabilites;

import com.superRainbowNinja.aincog.common.items.ICore;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

import javax.annotation.Nullable;

/**
 * Created by SuperRainbowNinja on 9/10/2016.
 *
 * if ur having issues make sure ur serializing everything
 *
 *
 * TODO majour sync issues when using a dedicated server, might just save everything in the items nbt and cache it in this class (think nbt is auto synced, probs check)
 * TODO would like to find another fix but i dont think there is one
 *      A few notes when implementing that
 *          - In the onItemCreate event (when u attatch the cap) pass the itemstack to the cap constructor
 *          - cache the data in this class, but keep it synced with the nbt (cache for rendering and such)
 *
 */
public class PoweredWeaponCapImp implements IPoweredWeaponCap, ICapabilitySerializable<NBTTagCompound> {
    public static final String DIM_ID_KEY = "DIM_ID";
    public static final String REC_TIME_KEY = "REC_TIME";
    public static final String WEAPON_STATE = "WEAPON_STATE";
    public static final String CORE_ITEM = "CORE_ITEM";

    public static final String CAP_KEY = "POWERED_WEAPON_CAP";

    long recTimeStamp = 0;
    int dimId = 0;
    boolean state = false;
    ItemStack core = null;

    ItemStack weapon;
    boolean isCached;

    public PoweredWeaponCapImp() {
        System.out.println("error");
    }

    public PoweredWeaponCapImp(ItemStack weaponIn) {
        weapon = weaponIn;
        isCached = false;
    }

    public void cacheCheck() {
        if (!isCached) {
            loadFromCache();
        }
    }

    public void loadFromCache() {
        NBTTagCompound compund = weapon.getSubCompound(CAP_KEY, true);
        if (compund.getBoolean(CORE_ITEM)) {
            core = ItemStack.loadItemStackFromNBT(compund);
            if (state = compund.getBoolean(WEAPON_STATE)) {
                recTimeStamp = compund.getLong(REC_TIME_KEY);
                dimId = compund.getInteger(DIM_ID_KEY);
            }
        }
        isCached = true;
    }

    @Override
    public void setRecentTimeStamp(long time) {
        cacheCheck();
        recTimeStamp = time;
        weapon.getSubCompound(CAP_KEY, true).setLong(REC_TIME_KEY, time);
    }

    @Override
    public long getRecentTimeStamp() {
        cacheCheck();
        return recTimeStamp;
    }

    @Override
    public void setDimensionId(int id) {
        cacheCheck();
        dimId = id;
        weapon.getSubCompound(CAP_KEY, true).setInteger(DIM_ID_KEY, id);
    }

    @Override
    public int getDimensionId() {
        cacheCheck();
        return dimId;
    }

    @Override
    public void setState(boolean cur) {
        cacheCheck();
        state = cur;
        weapon.getSubCompound(CAP_KEY, true).setBoolean(WEAPON_STATE, cur);
    }

    @Override
    public boolean getState() {
        cacheCheck();
        return state;
    }

    @Override
    public boolean setCoreDamage(int dmg) {
        cacheCheck();
        if (core != null && ((ICore) core.getItem()).setCoreDamage(core, dmg)) {
            core.writeToNBT(weapon.getSubCompound(CAP_KEY, true));
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
    public boolean trySetCore(ItemStack stack) {
        cacheCheck();
        if (!hasCore() && stack != null && stack.getItem() instanceof ICore) {
            core = stack.copy();
            core.writeToNBT(weapon.getSubCompound(CAP_KEY, true)).setBoolean(CORE_ITEM, true);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void loseCore() {
        core = null;
        weapon.getSubCompound(CAP_KEY, true).setBoolean(CORE_ITEM, false);
    }

    @Override
    public void setCore(ItemStack stack) {
        cacheCheck();
        if (stack != null && stack.getItem() instanceof ICore) {
            core = stack.copy();
            core.writeToNBT(weapon.getSubCompound(CAP_KEY, true)).setBoolean(CORE_ITEM, true);
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
        return capability == POWERED_WEAPON_CAP;
    }

    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        return (capability == POWERED_WEAPON_CAP) ? (T) this : null;
    }

    @Override
    public NBTTagCompound serializeNBT() {
        /*
        NBTTagCompound tag = new NBTTagCompound();
        tag.setInteger(DIM_ID_KEY, dimId);
        tag.setLong(REC_TIME_KEY, recTimeStamp);
        tag.setBoolean(WEAPON_STATE, state);
        if (core != null)
            tag.setTag(CORE_ITEM, core.writeToNBT(new NBTTagCompound()));

        */
        return new NBTTagCompound();
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        /*
        dimId = nbt.getInteger(DIM_ID_KEY);
        recTimeStamp = nbt.getLong(REC_TIME_KEY);
        state = nbt.getBoolean(WEAPON_STATE);
        core = ItemStack.loadItemStackFromNBT(nbt.getCompoundTag(CORE_ITEM));
        */
    }
}
