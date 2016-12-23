package com.superRainbowNinja.aincog.client.models;

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
 * TODO might generate textures dynamically in a session but will keep them cached (maybe have config option) (not so wrapped on this idea anymore, maybe)
 */
public class LaserSwordModel extends SmartModelOverride {

    private IBakedModel handle;
    private IBakedModel blade;
    private IBakedModel core;

    private boolean hasCore;
    private boolean isOn;

    public LaserSwordModel(IBakedModel handleIn, IBakedModel bladeIn, IBakedModel coreIn) {
        handle = handleIn;
        blade = bladeIn;
        core = coreIn;
    }

    @Override
    public IBakedModel handleItemState(SmartModel originalModel, ItemStack stack, World world, EntityLivingBase entity) {
        IPoweredWeaponCap cap = IPoweredWeaponCap.getCap(stack);
        hasCore = cap.hasCore();
        isOn = cap.weaponIsOn();

        return originalModel;
    }
//this could be faster
    @Override
    public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand) {
        List<BakedQuad> tmp = new ArrayList<BakedQuad>(handle.getQuads(state, side, rand));
        if (hasCore) {
            tmp.addAll(core.getQuads(state, side, rand));
        }
        if (isOn) {
            tmp.addAll(blade.getQuads(state, side, rand));
        }
        return tmp;
    }
}
