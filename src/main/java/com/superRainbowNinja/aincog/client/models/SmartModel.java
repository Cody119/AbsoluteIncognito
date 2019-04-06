package com.superRainbowNinja.aincog.client.models;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import javax.vecmath.Matrix4f;
import java.util.List;

/**
 * Created by SuperRainbowNinja on 7/10/2016.
 *
 * Might allow for customisation of the other properties later
 */
public class SmartModel implements IBakedModel {
    protected IBakedModel baseModel;

    public SmartModelOverride getItemOverrideList() {
        return itemOverrideList;
    }

    private SmartModelOverride itemOverrideList;

    public SmartModel(IBakedModel modelIn, SmartModelOverride overrideListIn) {
        baseModel = modelIn;
        itemOverrideList = overrideListIn;
    }

    public void setBaseModel(IBakedModel model) {
        baseModel = model;
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand) {
        return itemOverrideList.getQuads(state, side, rand);
    }

    @Override
    public boolean isAmbientOcclusion() {
        return baseModel.isAmbientOcclusion();
    }

    @Override
    public boolean isGui3d() {
        return baseModel.isGui3d();
    }

    @Override
    public boolean isBuiltInRenderer() {
        return false;
    }

    @Override
    public TextureAtlasSprite getParticleTexture() {
        return baseModel.getParticleTexture();
    }

    @Override
    public ItemCameraTransforms getItemCameraTransforms() {
        return baseModel.getItemCameraTransforms();
    }

    @Override
    public ItemOverrideList getOverrides() {
        return itemOverrideList;
    }

    @Override
    public Pair<? extends IBakedModel, Matrix4f> handlePerspective(ItemCameraTransforms.TransformType cameraTransformType) {
        return Pair.of(
                //IPerspectiveAwareModel extends IFlexibleBakedModel, so we don't need to implement it :P
                this,
                baseModel.handlePerspective(cameraTransformType).getRight()
        );
    }
}
