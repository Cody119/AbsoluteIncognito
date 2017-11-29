package com.superRainbowNinja.aincog;

import com.superRainbowNinja.aincog.common.capabilites.*;
import com.superRainbowNinja.aincog.common.items.*;
import com.superRainbowNinja.aincog.refrence.Reference;
import com.superRainbowNinja.aincog.util.LogHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Created by SuperRainbowNinja on 23/10/2016.
 */
public class SubEventHandler {
    public static final ResourceLocation SWORD_KEY = new ResourceLocation(Reference.MOD_ID + ":sword");
    public static final ResourceLocation TANK_KEY = new ResourceLocation(Reference.MOD_ID + ":tank");
    public static final ResourceLocation CORE_KEY = new ResourceLocation(Reference.MOD_ID + ":core_holder");
    public static final ResourceLocation MULTI_TOOL = new ResourceLocation(Reference.MOD_ID + ":multi_tool");
    public static final ResourceLocation MULTI_TOOL_SWORD = new ResourceLocation(Reference.MOD_ID + ":multi_tool_sword");

    @SubscribeEvent
    public void onItemCreate(AttachCapabilitiesEvent.Item event) {
        if (event.getItem() instanceof PoweredWeapon) {
            event.addCapability(SWORD_KEY, new PoweredWeaponCapImp(event.getItemStack()));
        } else if (event.getItem() instanceof TankComponent) {
            event.addCapability(TANK_KEY, new LockableTankImp());
        } else if (event.getItem() instanceof MultiTool) {
            event.addCapability(CORE_KEY, new CoreContainerImp(event.getItemStack()));
        } else if (event.getItem() == AIncogData.BLUE_BASIC_CORE) {
            event.addCapability(MULTI_TOOL, new MultiToolCapableImp());
        } else if (event.getItem() == AIncogData.RED_BASIC_CORE) {
            event.addCapability(MULTI_TOOL_SWORD, new MultiToolBladeCap());
        }
    }
}
