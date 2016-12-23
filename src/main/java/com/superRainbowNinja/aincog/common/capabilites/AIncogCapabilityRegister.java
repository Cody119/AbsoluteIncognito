package com.superRainbowNinja.aincog.common.capabilites;

import com.superRainbowNinja.aincog.common.network.MachineInfo;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;

/**
 * Created by SuperRainbowNinja on 10/10/2016.
 *
 * register all capability's here
 */
public class AIncogCapabilityRegister {
    private AIncogCapabilityRegister() {}

    public static void registerCapabilities() {
        CapabilityManager.INSTANCE.register(IPoweredWeaponCap.class,
                //Storage provider, no idea what it is actually used for though
                new Capability.IStorage<IPoweredWeaponCap>(){
                    @Override
                    public NBTBase writeNBT(Capability<IPoweredWeaponCap> capability, IPoweredWeaponCap instance, EnumFacing side) {
                        System.out.println("Write");
                        //return new NBTTagInt(instance.getInitialTimeStamp());
                        return new NBTTagCompound();
                    }

                    @Override
                    public void readNBT(Capability<IPoweredWeaponCap> capability, IPoweredWeaponCap instance, EnumFacing side, NBTBase nbt) {
                        System.out.println("read");
                        //instance.setInitialTimeStamp(((NBTTagInt) nbt).getInt());
                    }
                },
                PoweredWeaponCapImp::new
        );
        CapabilityManager.INSTANCE.register(ILockableTank.class,
                //Storage provider, no idea what it is actually used for though
                new Capability.IStorage<ILockableTank>(){
                    @Override
                    public NBTBase writeNBT(Capability<ILockableTank> capability, ILockableTank instance, EnumFacing side) {
                        System.out.println("Write");
                        //return new NBTTagInt(instance.getInitialTimeStamp());
                        return new NBTTagCompound();
                    }

                    @Override
                    public void readNBT(Capability<ILockableTank> capability, ILockableTank instance, EnumFacing side, NBTBase nbt) {
                        System.out.println("read");
                        //instance.setInitialTimeStamp(((NBTTagInt) nbt).getInt());
                    }
                },
                LockableTankImp::new
        );
    }
}
