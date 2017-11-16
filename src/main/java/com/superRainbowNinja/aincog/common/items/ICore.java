package com.superRainbowNinja.aincog.common.items;

import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;

/**
 * Created by SuperRainbowNinja on 10/10/2016.
 *
 * All cores need to implement this
 */
public interface ICore {
    boolean coreIsFunctional(ItemStack stack);

    //Extreme will probably only be attained through overclocking
    // (for cores in this mod anyway, if i ever add some 3rd party integration
    // then maybe some of those will be)
    enum CoreType {PITIFUL, BASIC, NORMAL, ADVANCED, EXTREME}

    int getMaxCoreDamage(ItemStack core);
    int getCoreDamage(ItemStack core);
    //return true to break the core
    boolean setCoreDamage(ItemStack core, int dmg);
    //core sensitive due to overclocking
    CoreType getCoreType(@Nullable ItemStack core);

    int getPrimaryColor(ItemStack core);
    int getSecondaryColor(ItemStack core);
    public int getOutlineColor(ItemStack core);

    float getEfficiency(ItemStack core);
    float getSpeed(ItemStack core);
    float getStrength(ItemStack core);
}
