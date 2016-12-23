package com.superRainbowNinja.aincog.common.fluids;

/**
 * Created by SuperRainbowNinja on 18/12/2016.
 */
public class MoltenFluid extends AIFluid {
    public MoltenFluid(String inName, int colorIn) {
        super(inName, colorIn, "molten_fluid");

        //some base states, probs customised bassed on material?
        setDensity(2000);
        setViscosity(10000);
        setTemperature(1000);
        setLuminosity(10);
    }
}
