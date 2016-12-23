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

    enum CoreType {BASIC, NORMAL, ADVANCED}

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
}
