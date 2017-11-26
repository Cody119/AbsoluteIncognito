package com.superRainbowNinja.aincog.common.machineLogic;

import com.superRainbowNinja.aincog.common.tileEntity.MachineFrameTile;

/**
 * Created by SuperRainbowNinja on 26/11/2017.
 */
public abstract class BaseLogic implements IMachineLogic {
    protected MachineFrameTile tile;

    @Override
    public MachineFrameTile getTile() {
        return tile;
    }

    @Override
    public void postDeserialize(MachineFrameTile tileIn) {
        tile = tileIn;
    }

    @Override
    public void initMachine(MachineFrameTile tileIn) {
        tile = tileIn;
    }
}
