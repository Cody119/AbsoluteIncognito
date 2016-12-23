package com.superRainbowNinja.aincog.common.machineLogic;

import com.superRainbowNinja.aincog.common.tileEntity.MachineFrameTile;
import io.netty.buffer.ByteBuf;

/**
 * Created by SuperRainbowNinja on 2/12/2016.
 */
public interface IMachineLogicProvider {
    //check if this tile entity is valid for this machine logic (this can be used for multiple machine logic's)
    IMachineLogic validMachine(MachineFrameTile tile);
    //get a list of names of every logic returnable by this
    String[] getLogics();
    //deserialise a logic for use on the client side
    IMachineLogic deserializeLogic(String name, ByteBuf buf);

    IMachineLogic getLogicByName(String name);
}
