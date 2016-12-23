package com.superRainbowNinja.aincog.common.capabilites;

import com.superRainbowNinja.aincog.common.items.ICore;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

import javax.annotation.Nullable;

/**
 * Created by SuperRainbowNinja on 9/10/2016.
 *
 */
public interface IPoweredWeaponCap {
    @CapabilityInject(IPoweredWeaponCap.class)
    public Capability<IPoweredWeaponCap> POWERED_WEAPON_CAP = null;

    //Start of methods to implement
    void setRecentTimeStamp(long time);
    long getRecentTimeStamp();

    void setDimensionId(int id);
    int getDimensionId();

    void setState(boolean cur);
    boolean getState();

    //returning true indicates the core is broken (not out of energy, broken)
    boolean setCoreDamage(int dmg);
    int getCoreMaxDamage();
    int getCoreDamage();

    boolean trySetCore(ItemStack stack);
    void loseCore();

    void setCore(ItemStack stack);

    @Nullable
    ItemStack getCoreItemStack();
    @Nullable
    ICore getCoreItem();

    static IPoweredWeaponCap getCap(ItemStack stack) {
        return stack.getCapability(POWERED_WEAPON_CAP, EnumFacing.DOWN);
    }

    static boolean weaponIsOn(ItemStack stack) {
        return stack.getCapability(POWERED_WEAPON_CAP, EnumFacing.DOWN).weaponIsOn();
    }

    default boolean weaponIsOn() {
        return getState();
    }

    static void turnWeaponOff(ItemStack stack) {
        stack.getCapability(POWERED_WEAPON_CAP, EnumFacing.DOWN).turnWeaponOff();
    }

    default void turnWeaponOff() {
        setState(false);
    }

    static boolean hasCore(ItemStack stack) {
        return stack.getCapability(POWERED_WEAPON_CAP, EnumFacing.DOWN).hasCore();
    }

    default boolean hasCore() {
        return getCoreItemStack() != null;
        //return true;
    }
}
