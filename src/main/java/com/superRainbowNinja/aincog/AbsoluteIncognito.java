package com.superRainbowNinja.aincog;

import com.superRainbowNinja.aincog.common.machineLogic.ArcFurnaceLogic;
import com.superRainbowNinja.aincog.common.network.GuiHandler;
import com.superRainbowNinja.aincog.common.capabilites.*;
import com.superRainbowNinja.aincog.common.network.PacketHandler;
import com.superRainbowNinja.aincog.proxys.CommonProxy;
import com.superRainbowNinja.aincog.refrence.Reference;
import com.superRainbowNinja.aincog.server.DebugCommand;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

@Mod(modid = Reference.MOD_ID, version = Reference.VERSION)
public class AbsoluteIncognito {

    static {
        FluidRegistry.enableUniversalBucket();
    }

    public static final SubEventHandler handle = new SubEventHandler();

    @Mod.Instance(Reference.MOD_ID)
    public static AbsoluteIncognito instance;

    @SidedProxy(clientSide = Reference.PROXY_CLIENT, serverSide = Reference.PROXY_SERVER)
    public static CommonProxy proxy;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        AIncogData.intiStatics();

        MinecraftForge.EVENT_BUS.register(handle);
        proxy.regEvents();
        proxy.initRegistryEntry();
        proxy.initTEs();
        AIncogCapabilityRegister.registerCapabilities();
        PacketHandler.initMessages();
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GuiHandler());
        proxy.init();
        AIncogData.initRecipes();
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {


        proxy.postInit();
        proxy.clearCache();
    }

    @EventHandler
    public void server(FMLServerStartingEvent event) {
        event.registerServerCommand(new DebugCommand());
    }
}
