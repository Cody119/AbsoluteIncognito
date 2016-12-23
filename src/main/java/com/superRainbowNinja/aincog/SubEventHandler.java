package com.superRainbowNinja.aincog;

import com.superRainbowNinja.aincog.common.capabilites.LockableTankImp;
import com.superRainbowNinja.aincog.common.capabilites.PoweredWeaponCapImp;
import com.superRainbowNinja.aincog.common.items.PoweredWeapon;
import com.superRainbowNinja.aincog.common.items.TankComponent;
import com.superRainbowNinja.aincog.refrence.Reference;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Created by SuperRainbowNinja on 23/10/2016.
 */
public class SubEventHandler {
    public static final ResourceLocation SWORD_KEY = new ResourceLocation(Reference.MOD_ID + ":sword");
    public static final ResourceLocation TANK_KEY = new ResourceLocation(Reference.MOD_ID + ":tank");
    @SubscribeEvent
    public void onItemCreate(AttachCapabilitiesEvent.Item event) {
        if (event.getItem() instanceof PoweredWeapon) {
            event.addCapability(SWORD_KEY, new PoweredWeaponCapImp(event.getItemStack()));
        } else if (event.getItem() instanceof TankComponent) {
            event.addCapability(TANK_KEY, new LockableTankImp());
        }
    }
}
