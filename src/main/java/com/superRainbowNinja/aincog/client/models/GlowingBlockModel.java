package com.superRainbowNinja.aincog.client.models;

import com.google.common.collect.ImmutableList;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import javax.vecmath.Matrix4f;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by SuperRainbowNinja on 24/10/2016.
 *
 * TODO this and its sub need to be updted, mostly the nullQuads needs to go (just return an empty list), and change array lists to immutable ones
 */
public class GlowingBlockModel implements IBakedModel {
    protected ImmutableList<BakedQuad>[] quads;
    protected ImmutableList<BakedQuad> nullQuads;
    protected TextureAtlasSprite sprite;
    protected boolean gui3D;
    protected boolean builtInRender;
    protected ItemCameraTransforms cameraTransforms;
    protected ItemOverrideList overrideList;

    public GlowingBlockModel(IBakedModel oldModel, List<BakedQuad> glowingQuads, TextureAtlasSprite spriteIn) {

        quads = new ImmutableList[6];

        for (EnumFacing f : EnumFacing.values()) {
            int index = f.getIndex();
            List<BakedQuad> oldQuads = oldModel.getQuads(null, f, 0);
            ArrayList<BakedQuad> newQuads = new ArrayList<>(oldQuads.size() + 1);
            newQuads.addAll(oldQuads);
            newQuads.add(glowingQuads.get(index));
            quads[index] = ImmutableList.copyOf(newQuads);
        }

        nullQuads = ImmutableList.copyOf(oldModel.getQuads(null, null, 0));

        sprite = spriteIn;
        gui3D = oldModel.isGui3d();
        builtInRender = oldModel.isBuiltInRenderer();
        cameraTransforms = oldModel.getItemCameraTransforms();
        overrideList = oldModel.getOverrides();

        camMatNone = oldModel.handlePerspective(ItemCameraTransforms.TransformType.NONE).getRight();
        camMatThirdLeftHand = oldModel.handlePerspective(ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND).getRight();
        camMatThirdRightHand = oldModel.handlePerspective(ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND).getRight();
        camMatFirstLeftHand = oldModel.handlePerspective(ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND).getRight();
        camMatFirstRightHand = oldModel.handlePerspective(ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND).getRight();
        camMatHead = oldModel.handlePerspective(ItemCameraTransforms.TransformType.HEAD).getRight();
        camMatGUI = oldModel.handlePerspective(ItemCameraTransforms.TransformType.GUI).getRight();
        camMatGround = oldModel.handlePerspective(ItemCameraTransforms.TransformType.GROUND).getRight();
        camMatFixed = oldModel.handlePerspective(ItemCameraTransforms.TransformType.FIXED).getRight();
    }

    protected Matrix4f camMatNone;
    protected Matrix4f camMatThirdLeftHand;
    protected Matrix4f camMatThirdRightHand;
    protected Matrix4f camMatFirstLeftHand;
    protected Matrix4f camMatFirstRightHand;
    protected Matrix4f camMatHead;
    protected Matrix4f camMatGUI;
    protected Matrix4f camMatGround;
    protected Matrix4f camMatFixed;

    @Override
    public Pair<? extends IBakedModel, Matrix4f> handlePerspective(ItemCameraTransforms.TransformType cameraTransformType) {
        Matrix4f ret;
        switch (cameraTransformType) {
            case NONE:
                ret = camMatNone;
                break;
            case THIRD_PERSON_LEFT_HAND:
                ret = camMatThirdLeftHand;
                break;
            case THIRD_PERSON_RIGHT_HAND:
                ret = camMatThirdRightHand;
                break;
            case FIRST_PERSON_LEFT_HAND:
                ret = camMatFirstLeftHand;
                break;
            case FIRST_PERSON_RIGHT_HAND:
                ret = camMatFirstRightHand;
                break;
            case HEAD:
                ret = camMatHead;
                break;
            case GUI:
                ret = camMatGUI;
                break;
            case GROUND:
                ret = camMatGround;
                break;
            case FIXED:
                ret = camMatFixed;
                break;
            default:
                ret = null;
                break;
        }
        return Pair.of(this, ret);
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand) {
        if (side != null) {
            return quads[side.getIndex()];
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
        return gui3D;
    }

    @Override
    public boolean isBuiltInRenderer() {
        return false;
    }

    @Override
    public TextureAtlasSprite getParticleTexture() {
        return sprite;
    }

    @Override
    public ItemCameraTransforms getItemCameraTransforms() {
        return cameraTransforms;
    }

    @Override
    public ItemOverrideList getOverrides() {
        return overrideList;
    }
}
