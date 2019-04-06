package com.superRainbowNinja.aincog.util;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.superRainbowNinja.aincog.client.models.GlowingBlockItemModel;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.SimpleBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumFacing;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * Created by SuperRainbowNinja on 12/10/2016.
 */
public class QuadUtil {
    public static final float QUAD_OFFSET = 0.005F;
    public static final int GLOWING_CONSTANT = 240;

    /* for the normal section of DefaultVertexFormats.ITEM */
    /* not sure what it is used for though */
    public static final int DOWN_N_CON = 33024;
    public static final int UP_N_CON = 32512;
    public static final int NORTH_N_CON = 8454144;
    public static final int SOUTH_N_CON = 8323072;
    public static final int WEST_N_CON = 129;
    public static final int EAST_N_CON = 127;


    private QuadUtil() {}

    public static IBakedModel addItemTint(IBakedModel model, int tintIndex) {
        return addItemTint(model, tintIndex, null);
    }
    //creates a new simple model model
    public static IBakedModel addItemTint(IBakedModel model, int tintIndex, IBlockState state) {
        List<BakedQuad> oldQuads = model.getQuads(state, null, 0L);
        ArrayList<BakedQuad> quads = new ArrayList<>(oldQuads.size());
        for (BakedQuad quad : oldQuads) {
            quads.add(getTintedQuad(quad, tintIndex));
        }
        EnumMap<EnumFacing, List<BakedQuad>> faceQuads = Maps.newEnumMap(EnumFacing.class);
        for (EnumFacing face : EnumFacing.values()) {
            faceQuads.put(face, ImmutableList.of());
        }

        return new SimpleBakedModel(quads, faceQuads,
                model.isAmbientOcclusion(), model.isGui3d(), model.getParticleTexture(), model.getItemCameraTransforms(), model.getOverrides());
    }

    public static BakedQuad getTintedQuad(BakedQuad oldQuad, int tintIndex) {
        return new BakedQuad(oldQuad.getVertexData(), tintIndex, oldQuad.getFace(), oldQuad.getSprite(), oldQuad.shouldApplyDiffuseLighting(), oldQuad.getFormat());
    }

    public static IBakedModel addGlowingQuads(IBakedModel oldModel, TextureAtlasSprite sprite) {
        return addGlowingQuads(oldModel, sprite, -1, -1);
    }

    public static IBakedModel addGlowingQuads(IBakedModel oldModel, TextureAtlasSprite sprite, int RGBA, int tint) {
        return addGlowingQuads(oldModel, sprite, oldModel.getParticleTexture(), RGBA, tint);
    }

    public static IBakedModel addGlowingQuads(IBakedModel oldModel, TextureAtlasSprite sprite, TextureAtlasSprite particleTexture, int RGBA, int tint) {
        return new GlowingBlockItemModel(oldModel,
                generateGlowingQuads(RGBA, tint, sprite),
                generateNormalQuads(RGBA, tint, sprite),
                particleTexture);
    }

    public static List<BakedQuad> generateGlowingQuads(int RGBA, int tint, TextureAtlasSprite sprite){
        List<BakedQuad> quads = new ArrayList<>(6);

        /* probs not needed but should ensure correct order*/
        for (int i = 0; i < 6; i++) {
            quads.add(null);
        }

        quads.set(EnumFacing.DOWN.getIndex(), genGlowingQuad(RGBA, tint, sprite, EnumFacing.DOWN));
        quads.set(EnumFacing.UP.getIndex(), genGlowingQuad(RGBA, tint, sprite, EnumFacing.UP));
        quads.set(EnumFacing.NORTH.getIndex(), genGlowingQuad(RGBA, tint, sprite, EnumFacing.NORTH));
        quads.set(EnumFacing.SOUTH.getIndex(), genGlowingQuad(RGBA, tint, sprite, EnumFacing.SOUTH));
        quads.set(EnumFacing.WEST.getIndex(), genGlowingQuad(RGBA, tint, sprite, EnumFacing.WEST));
        quads.set(EnumFacing.EAST.getIndex(), genGlowingQuad(RGBA, tint, sprite, EnumFacing.EAST));


        return quads;
    }

    public static List<BakedQuad> generateNormalQuads(int RGBA, int tint, TextureAtlasSprite sprite){
        List<BakedQuad> quads = new ArrayList<>(6);

        /* probs not needed but should ensure correct order*/
        for (int i = 0; i < 6; i++) {
            quads.add(null);
        }

        quads.set(EnumFacing.DOWN.getIndex(), genNormalQuad(RGBA, tint, sprite, EnumFacing.DOWN));
        quads.set(EnumFacing.UP.getIndex(), genNormalQuad(RGBA, tint, sprite, EnumFacing.UP));
        quads.set(EnumFacing.NORTH.getIndex(), genNormalQuad(RGBA, tint, sprite, EnumFacing.NORTH));
        quads.set(EnumFacing.SOUTH.getIndex(), genNormalQuad(RGBA, tint, sprite, EnumFacing.SOUTH));
        quads.set(EnumFacing.WEST.getIndex(), genNormalQuad(RGBA, tint, sprite, EnumFacing.WEST));
        quads.set(EnumFacing.EAST.getIndex(), genNormalQuad(RGBA, tint, sprite, EnumFacing.EAST));


        return quads;
    }

    public static BakedQuad genGlowingQuad(int RGBA, int tint, TextureAtlasSprite sprite, EnumFacing face) {
        int vertexSize = 7;
        int vecList[] = new int[28];

        switch (face) {
            case DOWN:
                putGlowingVertex(0, vecList, 0.0f, 0.0f - QUAD_OFFSET, 0.0f, sprite.getMinU(), sprite.getMaxV(), RGBA);
                putGlowingVertex(vertexSize, vecList, 1.0f, 0.0f - QUAD_OFFSET, 0.0f, sprite.getMaxU(), sprite.getMaxV(), RGBA);
                putGlowingVertex(2*vertexSize, vecList, 1.0f, 0.0f - QUAD_OFFSET, 1.0f, sprite.getMaxU(), sprite.getMinV(), RGBA);
                putGlowingVertex(3*vertexSize, vecList, 0.0f, 0.0f - QUAD_OFFSET, 1.0f, sprite.getMinU(), sprite.getMinV(), RGBA);
                break;
            case UP:
                putGlowingVertex(0, vecList, 0.0f, 1.0f + QUAD_OFFSET, 0.0f, sprite.getMinU(), sprite.getMinV(), RGBA);
                putGlowingVertex(vertexSize, vecList, 0.0f, 1.0f + QUAD_OFFSET, 1.0f, sprite.getMinU(), sprite.getMaxV(), RGBA);
                putGlowingVertex(2*vertexSize, vecList, 1.0f, 1.0f + QUAD_OFFSET, 1.0f, sprite.getMaxU(), sprite.getMaxV(), RGBA);
                putGlowingVertex(3*vertexSize, vecList, 1.0f, 1.0f + QUAD_OFFSET, 0.0f, sprite.getMaxU(), sprite.getMinV(), RGBA);
                break;
            case SOUTH:
                putGlowingVertex(0, vecList, 1.0f, 0.0f, 1.0f + QUAD_OFFSET, sprite.getMaxU(), sprite.getMaxV(), RGBA);
                putGlowingVertex(vertexSize, vecList, 1.0f, 1.0f, 1.0f + QUAD_OFFSET, sprite.getMaxU(), sprite.getMinV(), RGBA);
                putGlowingVertex(2*vertexSize, vecList, 0.0f, 1.0f, 1.0f + QUAD_OFFSET, sprite.getMinU(), sprite.getMinV(), RGBA);
                putGlowingVertex(3*vertexSize, vecList, 0.0f, 0.0f, 1.0f + QUAD_OFFSET, sprite.getMinU(), sprite.getMaxV(), RGBA);
                break;
            case NORTH:
                putGlowingVertex(0, vecList, 0.0f, 0.0f, 0.0f - QUAD_OFFSET, sprite.getMaxU(), sprite.getMaxV(), RGBA);
                putGlowingVertex(vertexSize, vecList, 0.0f, 1.0f, 0.0f - QUAD_OFFSET, sprite.getMaxU(), sprite.getMinV(), RGBA);
                putGlowingVertex(2*vertexSize, vecList, 1.0f, 1.0f, 0.0f - QUAD_OFFSET, sprite.getMinU(), sprite.getMinV(), RGBA);
                putGlowingVertex(3*vertexSize, vecList, 1.0f, 0.0f, 0.0f - QUAD_OFFSET, sprite.getMinU(), sprite.getMaxV(), RGBA);
                break;
            case WEST:
                putGlowingVertex(0, vecList, 0.0f - QUAD_OFFSET, 0.0f, 0.0f, sprite.getMinU(), sprite.getMaxV(), RGBA);
                putGlowingVertex(vertexSize, vecList, 0.0f - QUAD_OFFSET, 0.0f, 1.0f, sprite.getMaxU(), sprite.getMaxV(), RGBA);
                putGlowingVertex(2*vertexSize, vecList, 0.0f - QUAD_OFFSET, 1.0f, 1.0f, sprite.getMaxU(), sprite.getMinV(), RGBA);
                putGlowingVertex(3*vertexSize, vecList, 0.0f - QUAD_OFFSET, 1.0f, 0.0f, sprite.getMinU(), sprite.getMinV(), RGBA);
                break;
            case EAST:
                putGlowingVertex(0, vecList, 1.0f + QUAD_OFFSET, 0.0f, 0.0f, sprite.getMaxU(), sprite.getMaxV(), RGBA);
                putGlowingVertex(vertexSize, vecList, 1.0f + QUAD_OFFSET, 1.0f, 0.0f, sprite.getMaxU(), sprite.getMinV(), RGBA);
                putGlowingVertex(2*vertexSize, vecList, 1.0f + QUAD_OFFSET, 1.0f, 1.0f, sprite.getMinU(), sprite.getMinV(), RGBA);
                putGlowingVertex(3*vertexSize, vecList, 1.0f + QUAD_OFFSET, 0.0f, 1.0f, sprite.getMinU(), sprite.getMaxV(), RGBA);
                break;
        }

        return new BakedQuad(vecList, tint, face, sprite, false, DefaultVertexFormats.BLOCK);
    }

    public static BakedQuad genNormalQuad(int RGBA, int tint, TextureAtlasSprite sprite, EnumFacing face) {
        int vertexSize = 7;
        int vecList[] = new int[28];

        switch (face) {
            case DOWN:
                putNormalVertex(0, vecList, 0.0f, 0.0f - QUAD_OFFSET, 0.0f, sprite.getMinU(), sprite.getMaxV(), RGBA, face);
                putNormalVertex(vertexSize, vecList, 1.0f, 0.0f - QUAD_OFFSET, 0.0f, sprite.getMaxU(), sprite.getMaxV(), RGBA, face);
                putNormalVertex(2*vertexSize, vecList, 1.0f, 0.0f - QUAD_OFFSET, 1.0f, sprite.getMaxU(), sprite.getMinV(), RGBA, face);
                putNormalVertex(3*vertexSize, vecList, 0.0f, 0.0f - QUAD_OFFSET, 1.0f, sprite.getMinU(), sprite.getMinV(), RGBA, face);
                break;
            case UP:
                putNormalVertex(0, vecList, 0.0f, 1.0f + QUAD_OFFSET, 0.0f, sprite.getMinU(), sprite.getMinV(), RGBA, face);
                putNormalVertex(vertexSize, vecList, 0.0f, 1.0f + QUAD_OFFSET, 1.0f, sprite.getMinU(), sprite.getMaxV(), RGBA, face);
                putNormalVertex(2*vertexSize, vecList, 1.0f, 1.0f + QUAD_OFFSET, 1.0f, sprite.getMaxU(), sprite.getMaxV(), RGBA, face);
                putNormalVertex(3*vertexSize, vecList, 1.0f, 1.0f + QUAD_OFFSET, 0.0f, sprite.getMaxU(), sprite.getMinV(), RGBA, face);
                break;
            case SOUTH:
                putNormalVertex(0, vecList, 1.0f, 0.0f, 1.0f + QUAD_OFFSET, sprite.getMaxU(), sprite.getMaxV(), RGBA, face);
                putNormalVertex(vertexSize, vecList, 1.0f, 1.0f, 1.0f + QUAD_OFFSET, sprite.getMaxU(), sprite.getMinV(), RGBA, face);
                putNormalVertex(2*vertexSize, vecList, 0.0f, 1.0f, 1.0f + QUAD_OFFSET, sprite.getMinU(), sprite.getMinV(), RGBA, face);
                putNormalVertex(3*vertexSize, vecList, 0.0f, 0.0f, 1.0f + QUAD_OFFSET, sprite.getMinU(), sprite.getMaxV(), RGBA, face);
                break;
            case NORTH:
                putNormalVertex(0, vecList, 0.0f, 0.0f, 0.0f - QUAD_OFFSET, sprite.getMaxU(), sprite.getMaxV(), RGBA, face);
                putNormalVertex(vertexSize, vecList, 0.0f, 1.0f, 0.0f - QUAD_OFFSET, sprite.getMaxU(), sprite.getMinV(), RGBA, face);
                putNormalVertex(2*vertexSize, vecList, 1.0f, 1.0f, 0.0f - QUAD_OFFSET, sprite.getMinU(), sprite.getMinV(), RGBA, face);
                putNormalVertex(3*vertexSize, vecList, 1.0f, 0.0f, 0.0f - QUAD_OFFSET, sprite.getMinU(), sprite.getMaxV(), RGBA, face);
                break;
            case WEST:
                putNormalVertex(0, vecList, 0.0f - QUAD_OFFSET, 0.0f, 0.0f, sprite.getMinU(), sprite.getMaxV(), RGBA, face);
                putNormalVertex(vertexSize, vecList, 0.0f - QUAD_OFFSET, 0.0f, 1.0f, sprite.getMaxU(), sprite.getMaxV(), RGBA, face);
                putNormalVertex(2*vertexSize, vecList, 0.0f - QUAD_OFFSET, 1.0f, 1.0f, sprite.getMaxU(), sprite.getMinV(), RGBA, face);
                putNormalVertex(3*vertexSize, vecList, 0.0f - QUAD_OFFSET, 1.0f, 0.0f, sprite.getMinU(), sprite.getMinV(), RGBA, face);
                break;
            case EAST:
                putNormalVertex(0, vecList, 1.0f + QUAD_OFFSET, 0.0f, 0.0f, sprite.getMaxU(), sprite.getMaxV(), RGBA, face);
                putNormalVertex(vertexSize, vecList, 1.0f + QUAD_OFFSET, 1.0f, 0.0f, sprite.getMaxU(), sprite.getMinV(), RGBA, face);
                putNormalVertex(2*vertexSize, vecList, 1.0f + QUAD_OFFSET, 1.0f, 1.0f, sprite.getMinU(), sprite.getMinV(), RGBA, face);
                putNormalVertex(3*vertexSize, vecList, 1.0f + QUAD_OFFSET, 0.0f, 1.0f, sprite.getMinU(), sprite.getMaxV(), RGBA, face);
                break;
        }

        return new BakedQuad(vecList, tint, face, sprite, false, DefaultVertexFormats.BLOCK);
    }

    public static void putGlowingVertex(int offSet, int buffer[], float x, float y, float z, float u, float v, int RGBA) {
        //Bassed on DefaultVertexFormats.BLOCK
        buffer[offSet] = Float.floatToIntBits(x);
        buffer[offSet + 1] = Float.floatToIntBits(y);
        buffer[offSet + 2] = Float.floatToIntBits(z);
        buffer[offSet + 3] = RGBA;
        buffer[offSet + 4] = Float.floatToIntBits(u);
        buffer[offSet + 5] = Float.floatToIntBits(v);
        buffer[offSet + 6] = GLOWING_CONSTANT;
    }

    public static void putNormalVertex(int offSet, int buffer[], float x, float y, float z, float u, float v, int RGBA, EnumFacing face) {
        //Bassed on DefaultVertexFormats.BLOCK
        buffer[offSet] = Float.floatToIntBits(x);
        buffer[offSet + 1] = Float.floatToIntBits(y);
        buffer[offSet + 2] = Float.floatToIntBits(z);
        buffer[offSet + 3] = RGBA;
        buffer[offSet + 4] = Float.floatToIntBits(u);
        buffer[offSet + 5] = Float.floatToIntBits(v);
        int norm = 0;
        switch (face) {
            case DOWN:
                norm = DOWN_N_CON;
                break;
            case UP:
                norm = UP_N_CON;
                break;
            case NORTH:
                norm = NORTH_N_CON;
                break;
            case SOUTH:
                norm = SOUTH_N_CON;
                break;
            case WEST:
                norm = WEST_N_CON;
                break;
            case EAST:
                norm = EAST_N_CON;
                break;
        }
        buffer[offSet + 6] = norm;
    }
}
