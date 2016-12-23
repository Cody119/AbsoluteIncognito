package com.superRainbowNinja.aincog.common.tileEntity;

import com.superRainbowNinja.aincog.common.network.MachineInfo;

/**
 * Created by SuperRainbowNinja on 12/11/2016.
 *
 */
public interface IMachineInfoProvider {
    void updateMachine(MachineInfo info);
    MachineInfo getMachineInfo();
}
