package com.superRainbowNinja.aincog;

import com.superRainbowNinja.aincog.proxys.ClientProxy;
import com.superRainbowNinja.aincog.proxys.CommonProxy;
import com.superRainbowNinja.aincog.proxys.ServerProxy;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Created by SuperRainbowNinja on 17/12/2017.
 */
public class RegisterEventHandle {
    @SubscribeEvent
    public void registerBlocks(RegistryEvent.Register<Block> event) {
        AbsoluteIncognito.proxy.registerBlocks(event);
    }

    @SubscribeEvent
    public void registerItems(RegistryEvent.Register<Item> event) {
        AbsoluteIncognito.proxy.registerItems(event);
    }

    @SubscribeEvent
    public void registerModel(ModelRegistryEvent event) {
        AbsoluteIncognito.proxy.getClientProxy().registerModels();
    }
}
