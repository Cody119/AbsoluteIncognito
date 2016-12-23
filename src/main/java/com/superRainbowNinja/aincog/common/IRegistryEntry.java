package com.superRainbowNinja.aincog.common;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by SuperRainbowNinja on 9/12/2016.
 */
public interface IRegistryEntry {
    void registerObjects();
    @SideOnly(Side.CLIENT)
    void registerModels();
}
