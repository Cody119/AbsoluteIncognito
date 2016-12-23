package com.superRainbowNinja.aincog.common.capabilites;

import com.superRainbowNinja.aincog.util.NBTHelper;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

import javax.annotation.Nullable;

/**
 * Created by SuperRainbowNinja on 2/12/2016.
 */
public class LockableTankImp extends FluidTank implements ILockableTank, ICapabilitySerializable<NBTTagCompound> {
    private static final String LOCK_NBT = "TankLock";
    private OutputState lockState;

    @Override
    public void setState(OutputState state) {
        if (state != null) {
            lockState = state;
        }
    }

    public enum OutputState {
        IO, OUTPUT, INPUT, LOCKED;
    }

    public LockableTankImp() {
        this(Fluid.BUCKET_VOLUME);
    }

    public LockableTankImp(int capacity) {
        this(null, capacity);
    }

    public LockableTankImp(Fluid fluid, int amount, int capacity) {
        this(new FluidStack(fluid, amount), capacity);
    }

    public LockableTankImp(@Nullable FluidStack fluidStack, int capacity) {
        super(fluidStack, capacity);
        lockState = OutputState.IO;
    }



    public static LockableTankImp deserialize(ByteBuf buf, int size) {
        LockableTankImp tank = new LockableTankImp(size);
        tank.deserialize(buf);
        return tank;
    }

    public void serialize(ByteBuf buf) {
        buf.writeInt(lockState.ordinal());
        NBTHelper.writeFluid(buf, getFluid());
    }

    public void deserialize(ByteBuf buf) {
        lockState = OutputState.values()[buf.readInt()];
        setFluid(NBTHelper.readFluid(buf));
    }

    @Override
    public FluidTank readFromNBT(NBTTagCompound nbt) {
        lockState = OutputState.values()[nbt.getInteger(LOCK_NBT)];
        return super.readFromNBT(nbt);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        nbt.setInteger(LOCK_NBT, lockState.ordinal());
        return super.writeToNBT(nbt);
    }
    /* when the tank is locked drain/fill will fail*/
    @Override
    public int fill(FluidStack resource, boolean doFill) {
        return lockState == OutputState.IO || lockState == OutputState.INPUT ? super.fill(resource, doFill) : 0;
    }

    @Override
    public FluidStack drain(FluidStack resource, boolean doDrain) {
        return lockState == OutputState.IO || lockState == OutputState.OUTPUT ? super.drain(resource, doDrain) : null;
    }

    @Override
    public FluidStack drain(int maxDrain, boolean doDrain) {
        return lockState == OutputState.IO || lockState == OutputState.OUTPUT ? super.drain(maxDrain, doDrain) : null;
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY || capability == LOCKABLE_TANK_CAPABILITY;
    }

    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        return capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY || capability == LOCKABLE_TANK_CAPABILITY ?
                (T) this : null;
    }

    @Override
    public NBTTagCompound serializeNBT() {
        return writeToNBT(new NBTTagCompound());
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        readFromNBT(nbt);
    }


}
