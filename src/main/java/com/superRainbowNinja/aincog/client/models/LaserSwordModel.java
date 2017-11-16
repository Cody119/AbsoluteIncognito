package com.superRainbowNinja.aincog.client.models;

import com.google.common.collect.ImmutableList;
import com.superRainbowNinja.aincog.common.capabilites.IPoweredWeaponCap;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by SuperRainbowNinja on 7/10/2016.
 *
 * Laser sword model generator
 *
 */
public class LaserSwordModel extends SmartModelOverride {

    private IBakedModel handle;
    private IBakedModel blade;
    private IBakedModel core;

    private final ImmutableList<BakedQuad> handleBare;
    private final ImmutableList<BakedQuad> handleCore;
    private final ImmutableList<BakedQuad> swordOn;

    private boolean hasCore;
    private boolean isOn;

    public LaserSwordModel(IBakedModel handleIn, IBakedModel bladeIn, IBakedModel coreIn) {
        handle = handleIn;
        blade = bladeIn;
        core = coreIn;

        ArrayList<BakedQuad> test = new ArrayList<>();
        test.addAll(handle.getQuads(null, null, 0));
        int one = test.size();
        test.addAll(core.getQuads(null, null, 0));
        int two = test.size();
        test.addAll(blade.getQuads(null, null, 0));

        swordOn = ImmutableList.copyOf(test);
        handleCore = swordOn.subList(0, two);
        handleBare = swordOn.subList(0, one);
    }

    @Override
    public IBakedModel handleItemState(SmartModel originalModel, ItemStack stack, World world, EntityLivingBase entity) {
        IPoweredWeaponCap cap = IPoweredWeaponCap.getCap(stack);
        hasCore = cap.hasCore();
        isOn = cap.weaponIsOn();

        return originalModel;
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand) {
//        List<BakedQuad> tmp = new ArrayList<BakedQuad>(handle.getQuads(state, side, rand));
//        if (hasCore) {
//            tmp.addAll(core.getQuads(state, side, rand));
//        }
//        if (isOn) {
//            tmp.addAll(blade.getQuads(state, side, rand));
//        }
//
        if (hasCore) {
            if (isOn) {
                return swordOn;
            } else {
                return handleCore;
            }
        }
        return handleBare;
    }
}
