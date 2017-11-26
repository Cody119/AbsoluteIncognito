package com.superRainbowNinja.aincog.common.machineLogic;

import com.superRainbowNinja.aincog.common.capabilites.ILockableTank;
import com.superRainbowNinja.aincog.common.capabilites.LockableTankImp;
import com.superRainbowNinja.aincog.common.items.BasicCore;
import com.superRainbowNinja.aincog.common.tileEntity.MachineFrameTile;
import com.superRainbowNinja.aincog.util.LogHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

import java.util.ArrayList;

/**
 * Created by SuperRainbowNinja on 19/12/2016.
 */
public abstract class FluidLogic extends BaseLogic {
    protected ILockableTank tank;

    protected void setTank(MachineFrameTile tile) {
        ArrayList<ItemStack> components = tile.getComponentItems();
        for (ItemStack stack : components) {
            if (stack.hasCapability(ILockableTank.LOCKABLE_TANK_CAPABILITY, EnumFacing.DOWN)) {
                tank = stack.getCapability(ILockableTank.LOCKABLE_TANK_CAPABILITY, EnumFacing.DOWN);
                tank.setState(LockableTankImp.OutputState.IO);
                return;
            }
        }
        LogHelper.errorLog("EROOOOORRRR tank not found at " + LogHelper.getPosString(tile.getPos()));
        tank = new LockableTankImp(0);
    }

    @Override
    public void postDeserialize(MachineFrameTile tileIn) {
        super.postDeserialize(tileIn);
        setTank(tile);
    }

    @Override
    public void initMachine(MachineFrameTile tileIn) {
        super.initMachine(tileIn);
        setTank(tile);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing)
    {
        return capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing)
    {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
            return (T) tank;
        return null;
    }

}
