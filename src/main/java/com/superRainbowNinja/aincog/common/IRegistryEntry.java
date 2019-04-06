package com.superRainbowNinja.aincog.common;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by SuperRainbowNinja on 9/12/2016.
 */
public interface IRegistryEntry {
    //void registerObjects();
    default void registerBlocks(RegistryEvent.Register<Block> event) {}
    default void registerItems(RegistryEvent.Register<Item> event) {}
    @SideOnly(Side.CLIENT)
    default void registerModels() {}
}
