package com.superRainbowNinja.aincog.common.capabilites;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

import javax.annotation.Nullable;

/**
 * Created by SuperRainbowNinja on 9/10/2016.
 *
 *
 */
public class PoweredWeaponCapImp extends CoreContainerImp implements IPoweredWeaponCap {
    public static final String DIM_ID_KEY = "DIM_ID";
    public static final String REC_TIME_KEY = "REC_TIME";
    public static final String WEAPON_STATE = "WEAPON_STATE";

    public static final String POWERED_WEAPON_CAP_KEY = "POWERED_WEAPON_CAP";

    private long recTimeStamp = 0;
    private int dimId = 0;
    private boolean state = false;

    public PoweredWeaponCapImp() {
        super();
    }

    public PoweredWeaponCapImp(ItemStack weaponIn) {
        super(weaponIn);
    }

    @Override
    public void cache() {
        super.cache();
        if (core != null) {
            NBTTagCompound compound = thisStack.getSubCompound(POWERED_WEAPON_CAP_KEY, true);
            if (state = compound.getBoolean(WEAPON_STATE)) {
                recTimeStamp = compound.getLong(REC_TIME_KEY);
                dimId = compound.getInteger(DIM_ID_KEY);
            }
        }
    }

    @Override
    public void setRecentTimeStamp(long time) {
        cacheCheck();
        recTimeStamp = time;
        thisStack.getSubCompound(POWERED_WEAPON_CAP_KEY, true).setLong(REC_TIME_KEY, time);
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
        thisStack.getSubCompound(POWERED_WEAPON_CAP_KEY, true).setInteger(DIM_ID_KEY, id);
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
        thisStack.getSubCompound(POWERED_WEAPON_CAP_KEY, true).setBoolean(WEAPON_STATE, cur);
    }

    @Override
    public boolean getState() {
        cacheCheck();
        return state;
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == POWERED_WEAPON_CAP || super.hasCapability(capability, facing);
    }

    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        return (capability == POWERED_WEAPON_CAP) ? (T) this : super.getCapability(capability, facing);
    }
}
