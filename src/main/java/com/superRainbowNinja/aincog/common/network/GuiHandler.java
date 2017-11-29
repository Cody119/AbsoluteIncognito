package com.superRainbowNinja.aincog.common.network;

import com.superRainbowNinja.aincog.client.gui.WeaponGui;
import com.superRainbowNinja.aincog.common.containers.CoreContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

/**
 * Created by SuperRainbowNinja on 10/10/2016.
 */
public class GuiHandler implements IGuiHandler {
    public static final int WEAPON_GUI = 0;

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if (ID == WEAPON_GUI) return new CoreContainer(player, player.inventory.currentItem);
        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if (ID == WEAPON_GUI) return new WeaponGui(player, player.inventory.currentItem);
        return null;
    }
}
