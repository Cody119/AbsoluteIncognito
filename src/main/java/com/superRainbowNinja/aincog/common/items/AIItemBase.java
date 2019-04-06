package com.superRainbowNinja.aincog.common.items;

import com.superRainbowNinja.aincog.AIncogData;
import com.superRainbowNinja.aincog.common.IRegistryEntry;
import com.superRainbowNinja.aincog.refrence.Reference;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by SuperRainbowNinja on 4/10/2016.
 */
public class AIItemBase extends Item implements IRegistryEntry{
    public final String name;

    public AIItemBase(String name) {
        this.name = name;
        setRegistryName(name);
        setNoRepair();
        setCreativeTab(AIncogData.A_INCOG_C_Tab);
        setUnlocalizedName(getRegistryName().toString());
    }

    @Override
    public void registerItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().register(this);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerModels() {
        ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(this.getRegistryName(), "inventory"));
    }

/*
    @Override
    public net.minecraftforge.common.capabilities.ICapabilityProvider initCapabilities(ItemStack thisStack, NBTTagCompound nbt) {
        if (this == thisStack.getItem()) {
            System.out.println("Item Refresh");
        } else {
            System.out.println("Copy");
        }

        return null;
    }
    */
}
