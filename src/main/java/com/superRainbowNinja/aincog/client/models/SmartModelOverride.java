package com.superRainbowNinja.aincog.client.models;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
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
 * A generic implementation of ItemOverrideList made for use with SmartModel
 */
public abstract class SmartModelOverride extends ItemOverrideList {
    public SmartModelOverride() {
        super(new ArrayList<>());
    }

    //override this method
    public abstract IBakedModel handleItemState(SmartModel originalModel, ItemStack stack, World world, EntityLivingBase entity);

    //and this one
    public abstract List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand);

    @Override
    public IBakedModel handleItemState(IBakedModel originalModel, ItemStack stack, World world, EntityLivingBase entity) {
        return handleItemState((SmartModel) originalModel, stack, world, entity);
    }
}
