package com.superRainbowNinja.aincog.util;

import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

/**
 * Created by SuperRainbowNinja on 17/11/2017.
 */
public class AssortedUtil {
    private AssortedUtil() {}

    public static FluidStack fluidStack(String name, int quantity) {
        return new FluidStack(FluidRegistry.getFluid(name), quantity);
    }
}
