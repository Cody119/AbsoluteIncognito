package com.superRainbowNinja.aincog.common.items;

import com.superRainbowNinja.aincog.common.capabilites.ILockableTank;
import com.superRainbowNinja.aincog.util.EnumPosition;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

/**
 * Created by SuperRainbowNinja on 8/12/2016.
 */
public class TankComponent extends AIItemBase implements IMachineComponent{
    public TankComponent() {
        super("comp_tank");
        setMaxStackSize(1);
    }

    @Override
    public AIItemBase getItem() {
        return this;
    }

    public EnumPosition[] COMP_POS = new EnumPosition[]{EnumPosition.BOTTOM, EnumPosition.TOP};

    @Override
    public EnumPosition[] getComponentPlaces() {
        return COMP_POS;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
        IFluidTankProperties[] fluidInfo = stack.<ILockableTank>getCapability(ILockableTank.LOCKABLE_TANK_CAPABILITY, EnumFacing.DOWN).getTankProperties();
        if (fluidInfo.length > 0) {
            if (fluidInfo[0].getContents() != null) {
                tooltip.add(fluidInfo[0].getContents().getLocalizedName());
                tooltip.add(fluidInfo[0].getContents().amount + "/" + fluidInfo[0].getCapacity());
            }
        }
    }
}
