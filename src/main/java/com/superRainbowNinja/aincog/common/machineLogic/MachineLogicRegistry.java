package com.superRainbowNinja.aincog.common.machineLogic;

import com.google.common.collect.Maps;
import com.superRainbowNinja.aincog.common.tileEntity.MachineFrameTile;
import com.superRainbowNinja.aincog.util.NBTUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;

import java.nio.channels.ByteChannel;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by SuperRainbowNinja on 2/12/2016.
 */
public enum MachineLogicRegistry {
    INSTANCE;

    public static final String KEY_LOGIC = "LOGIC";
    public static final String KEY_LOGIC_NAME = "LOGIC_NAME";

    ArrayList<IMachineLogicProvider> logicProviders;
    Map<String, IMachineLogicProvider> providerMap;

    MachineLogicRegistry() {
        logicProviders = new ArrayList<>();
        providerMap = Maps.newHashMap();
        /*
        register(new IMachineLogicProvider() {
            @Override
            public IMachineLogic validMachine(MachineFrameTile tile) {
                return IMachineLogic.CLIENT_MACHINE;
            }

            @Override
            public String[] getLogics() {
                return new String[]{IMachineLogic.CLIENT_MACHINE.getName()};
            }

            @Override
            public IMachineLogic deserializeLogic(String name, ByteBuf buf) {
                return IMachineLogic.CLIENT_MACHINE;
            }
        });
        */
        register(new GeneratorLogic.Provider());
        register(new ArcFurnaceLogic.Provider());
        register(new OreFormerLogic.Provider());
    }

    public void register(IMachineLogicProvider provider) {
        logicProviders.add(provider);
        for (String name : provider.getLogics()) {
            providerMap.put(name, provider);
        }
    }

    public static void serializeLogic(IMachineLogic logic, ByteBuf buf) {
        if (logic != null) {
            ByteBufUtils.writeUTF8String(buf, logic.getName());
            logic.serialize(buf);
        } else {
            ByteBufUtils.writeUTF8String(buf, "");
        }
    }

    public IMachineLogic deserializeLogic(ByteBuf buf) {
        String name = ByteBufUtils.readUTF8String(buf);
        if (name.equals("")) return null;
        IMachineLogicProvider provider = providerMap.get(name);
        if (provider != null) {
            return provider.deserializeLogic(name, buf);
        }
        return null;
    }

    public static NBTTagCompound writeLogic(NBTTagCompound compound, IMachineLogic logic) {
        if (logic != null) {
            compound.setString(KEY_LOGIC_NAME, logic.getName());
            NBTUtils.writeObject(compound, KEY_LOGIC, logic::writeToNBT);
        } else {
            compound.setString(KEY_LOGIC_NAME, "");
        }
        return compound;
    }

    public IMachineLogic readLogic(NBTTagCompound compound) {
        String name = compound.getString(KEY_LOGIC_NAME);
        //if (name.equals("")) return null;
        IMachineLogic logic = MachineLogicRegistry.INSTANCE.getLogic(name);
        if (logic != null) {
            NBTUtils.readObject(compound, KEY_LOGIC, (nbt) -> logic.readFromNBT(nbt));
        }
        return logic;
    }

    public IMachineLogic tryGetLogic(MachineFrameTile m) {
        for (IMachineLogicProvider logicProvider : logicProviders) {
            IMachineLogic logic = logicProvider.validMachine(m);
            if (logic != null) {
                return logic;
            }
        }
        return null;
    }

    //same as above but ensures that the name is the same as well
    //should be used when reloading the machine logic
    public IMachineLogic tryGetLogic(MachineFrameTile m, String name) {
        for (IMachineLogicProvider logicProvider : logicProviders) {
            IMachineLogic logic = logicProvider.validMachine(m);
            if (logic != null && name.equals(logic.getName())) {
                System.out.println("name request confirmed");
                return logic;
            }
        }
        System.out.println("request failed name");
        return null;
    }

    public IMachineLogic getLogic(String name) {
        IMachineLogicProvider provider = providerMap.get(name);
        if (provider != null) {
            return provider.getLogicByName(name);
        }
        return null;
    }
}
