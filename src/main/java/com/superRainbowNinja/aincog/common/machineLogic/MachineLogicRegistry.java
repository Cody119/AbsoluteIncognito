package com.superRainbowNinja.aincog.common.machineLogic;

import com.google.common.collect.Maps;
import com.superRainbowNinja.aincog.common.tileEntity.MachineFrameTile;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import net.minecraftforge.fml.common.network.ByteBufUtils;

import java.nio.channels.ByteChannel;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by SuperRainbowNinja on 2/12/2016.
 */
public enum MachineLogicRegistry {
    INSTANCE;
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
    }

    public void register(IMachineLogicProvider provider) {
        logicProviders.add(provider);
        for (String name : provider.getLogics()) {
            providerMap.put(name, provider);
        }
    }

    public static void serializeLogic(IMachineLogic logic, ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, logic.getName());
        logic.serialize(buf);
    }

    public IMachineLogic deserializeLogic(ByteBuf buf) {
        String name = ByteBufUtils.readUTF8String(buf);
        IMachineLogicProvider provider = providerMap.get(name);
        if (provider != null) {
            return provider.deserializeLogic(name, buf);
        }
        return null;
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
