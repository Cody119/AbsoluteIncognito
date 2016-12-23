package com.superRainbowNinja.aincog.common.capabilites;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

/**
 * Created by SuperRainbowNinja on 8/12/2016.
 */
public interface ILockableTank extends IFluidHandler{
    @CapabilityInject(ILockableTank.class)
    public Capability<ILockableTank> LOCKABLE_TANK_CAPABILITY = null;

    void setState(LockableTankImp.OutputState state);

    //these 2 are generally implicitly overridden
    //void setFluid(FluidStack stack);
    //FluidStack getFluid();
    //int getCapacity();
}
