package com.superRainbowNinja.aincog;

import com.superRainbowNinja.aincog.common.network.GuiHandler;
import com.superRainbowNinja.aincog.common.capabilites.*;
import com.superRainbowNinja.aincog.common.network.PacketHandler;
import com.superRainbowNinja.aincog.proxys.CommonProxy;
import com.superRainbowNinja.aincog.refrence.Reference;
import com.superRainbowNinja.aincog.server.DebugCommand;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;


/* Update TODO:
 *      No more null items, use ItemStack.Empty
 *      Redo energy caps fun fun fun
 *      Make sure caps have default implementations that maybe do something?
 *
*/

@Mod(modid = Reference.MOD_ID, version = Reference.VERSION)
public class AbsoluteIncognito {

    @Mod.Instance(Reference.MOD_ID)
    public static AbsoluteIncognito instance;

    @SidedProxy(clientSide = Reference.PROXY_CLIENT, serverSide = Reference.PROXY_SERVER)
    public static CommonProxy proxy;

    public static final SubEventHandler handle = new SubEventHandler();
    public static final RegisterEventHandle REGISTER_EVENT_HANDLE = new RegisterEventHandle();

    static { FluidRegistry.enableUniversalBucket(); }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        AIncogData.init();

        MinecraftForge.EVENT_BUS.register(handle);
        MinecraftForge.EVENT_BUS.register(REGISTER_EVENT_HANDLE);
        proxy.regEvents();
        proxy.preInit();
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
