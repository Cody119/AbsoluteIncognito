package com.superRainbowNinja.aincog.common.fluids;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

/**
 * Created by SuperRainbowNinja on 9/12/2016.
 */
public class AIFluid extends Fluid {
    public final String name;
    public final int color;

    public AIFluid(String inName, int colorIn, String recourseName) {
        super(inName, new ResourceLocation("aincog", "blocks/fluids/" + recourseName + "_still"), new ResourceLocation("aincog", "blocks/fluids/" + recourseName + "_flow"));
        name = inName;
        color = colorIn;
        FluidRegistry.addBucketForFluid(this); //maybe i shouldn't register it here? but it has to be registered so probs not a big deal
    }

    @Override
    public int getColor() {
        return color;
    }
}
