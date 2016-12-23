package com.superRainbowNinja.aincog.client.models;

import com.google.common.collect.ImmutableList;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.IPerspectiveAwareModel;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import javax.vecmath.Matrix4f;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by SuperRainbowNinja on 24/10/2016.
 */
public class GlowingBlockItemModel extends GlowingBlockModel {
    private ItemModel itemModel;

    protected ImmutableList<BakedQuad>[] normalQuads;
    //protected static final ImmutableList<BakedQuad> noQuads = ImmutableList.of();

    public GlowingBlockItemModel(IPerspectiveAwareModel oldModel, List<BakedQuad> glowingQuads, List<BakedQuad> normalQuadsIn, TextureAtlasSprite spriteIn) {
        super(oldModel, glowingQuads, spriteIn);
        itemModel = new ItemModel();

        normalQuads = new ImmutableList[6];

        for (EnumFacing f : EnumFacing.values()) {
            int index = f.getIndex();
            List<BakedQuad> oldQuads = oldModel.getQuads(null, f, 0);
            ArrayList<BakedQuad> newQuads = new ArrayList<>(oldQuads.size() + 1);
            newQuads.addAll(oldQuads);
            newQuads.add(normalQuadsIn.get(index));
            normalQuads[index] = ImmutableList.copyOf(newQuads);
        }
    }

    @Override
    public Pair<? extends IBakedModel, Matrix4f> handlePerspective(ItemCameraTransforms.TransformType cameraTransformType) {
        IBakedModel ret;
        if (cameraTransformType == ItemCameraTransforms.TransformType.GROUND
                || cameraTransformType == ItemCameraTransforms.TransformType.FIXED
                || cameraTransformType == ItemCameraTransforms.TransformType.NONE) {
            ret = this;
        } else {
            ret = itemModel;
        }
        return Pair.of(ret, super.handlePerspective(cameraTransformType).getRight());
    }

    class ItemModel implements IBakedModel {

        @Override
        public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand) {
            if (side != null) {
                return normalQuads[side.getIndex()];
            } else {
                return nullQuads;
            }
        }

        @Override
        public boolean isAmbientOcclusion() {
            return false;
        }

        @Override
        public boolean isGui3d() {
            return GlowingBlockItemModel.this.isGui3d();
        }

        @Override
        public boolean isBuiltInRenderer() {
            return GlowingBlockItemModel.this.isBuiltInRenderer();
        }

        @Override
        public TextureAtlasSprite getParticleTexture() {
            return GlowingBlockItemModel.this.getParticleTexture();
        }

        @Override
        public ItemCameraTransforms getItemCameraTransforms() {
            return GlowingBlockItemModel.this.getItemCameraTransforms();
        }

        @Override
        public ItemOverrideList getOverrides() {
            return GlowingBlockItemModel.this.getOverrides();
        }
    }
}
