package com.superRainbowNinja.aincog.common.blocks;

import com.superRainbowNinja.aincog.client.models.AbsoluteModelRegistry;
import com.superRainbowNinja.aincog.proxys.ClientProxy;
import com.superRainbowNinja.aincog.util.LogHelper;
import com.superRainbowNinja.aincog.util.QuadUtil;
import net.minecraft.block.IGrowable;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.Random;

/**
 * Created by SuperRainbowNinja on 19/12/2016.
 */
public class CrystalOre extends AIBlockBase implements IGrowable {

    public static PropertyInteger AGE = PropertyInteger.create("age", 0, 3);
    public final int color;
    public final Item drop;

    public CrystalOre(String name, int colorIn, Item dropIn) {
        super(name, Material.ROCK);
        setTickRandomly(true);
        setDefaultState(blockState.getBaseState().withProperty(AGE, 0));
        setHardness(3.0F);
        setResistance(5.0F);
        setSoundType(SoundType.STONE);
        color = colorIn;
        drop = dropIn;
    }

    private int getAge(IBlockState state) {
        return state.<Integer>getValue(AGE);
    }

    @Nullable
    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return getAge(state) == 3 ? this.drop : Item.getItemFromBlock(Blocks.COBBLESTONE);
    }

    @Override
    public int quantityDropped(Random random) {
        return 1;
    }

    @Override
    public int quantityDroppedWithBonus(int fortune, Random random) {
        if (fortune >= 3) {
            //chance for 3
            return 1 + (random.nextFloat() <= (0.2*fortune) ? 1 : 0) + (random.nextFloat() <= (0.05*fortune) ? 1 : 0);
        } else {
            return 1 + (random.nextFloat() <= (0.2*fortune) ? 1 : 0);
        }
    }

    @Override
    public BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, AGE);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return getAge(state);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(AGE, meta);
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        grow(worldIn, rand, pos, state);
    }

    @Override
    public boolean canGrow(World worldIn, BlockPos pos, IBlockState state, boolean isClient) {
        return getAge(state) < 3;
    }

    @Override
    public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state) {
        return false;
    }

    @Override
    public void grow(World worldIn, Random rand, BlockPos pos, IBlockState state) {
        if (canGrow(worldIn, pos, state, worldIn.isRemote)) {
            worldIn.setBlockState(pos, state.withProperty(AGE, getAge(state) + 1));
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerModels() {
        ModelLoader.setCustomModelResourceLocation(this.item, 0,
                new ModelResourceLocation(getRegistryName().toString(), "age=3"));
        //LogHelper.infoLog("Test block isssss :" + this.name);

        /*
        AbsoluteModelRegistery.INSTANCE.buildAndAddGlowingTexture(new ModelResourceLocation(getRegistryName().toString(), "age=1"), new ResourceLocation("aincog:blocks/gen_ore_stage_1"), 1);
        AbsoluteModelRegistery.INSTANCE.buildAndAddGlowingTexture(new ModelResourceLocation(getRegistryName().toString(), "age=2"), new ResourceLocation("aincog:blocks/gen_ore_stage_2"), 1);
        AbsoluteModelRegistery.INSTANCE.buildAndAddGlowingTexture(new ModelResourceLocation(getRegistryName().toString(), "age=3"), new ResourceLocation("aincog:blocks/gen_ore_stage_3"), 1);
        */
        AbsoluteModelRegistry.INSTANCE.registerModelReplacer(new ModelResourceLocation(getRegistryName().toString(), "age=1"), () -> ClientProxy.CRYSTAL_ORE_MODELS.model1);
        AbsoluteModelRegistry.INSTANCE.registerModelReplacer(new ModelResourceLocation(getRegistryName().toString(), "age=2"), () -> ClientProxy.CRYSTAL_ORE_MODELS.model2);
        AbsoluteModelRegistry.INSTANCE.registerModelReplacer(new ModelResourceLocation(getRegistryName().toString(), "age=3"), () -> ClientProxy.CRYSTAL_ORE_MODELS.model3);

    }

    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT_MIPPED;
    }

    @SideOnly(Side.CLIENT)
    public OreColor getOreColor() {
        return new OreColor();
    }

    @SideOnly(Side.CLIENT)
    public class OreColor implements IItemColor, IBlockColor {
        @Override
        public int colorMultiplier(ItemStack stack, int tintIndex) {
            return color;
        }

        @Override
        public int colorMultiplier(IBlockState state, @Nullable IBlockAccess worldIn, @Nullable BlockPos pos, int tintIndex) {
            return color;
        }
    }
    //this is created in the client proxy
    public static class ModelBuilder implements AbsoluteModelRegistry.IModelEntry {
        public static final ResourceLocation texture1 = new ResourceLocation("aincog:blocks/gen_ore_stage_1");
        public static final ResourceLocation texture2 = new ResourceLocation("aincog:blocks/gen_ore_stage_2");
        public static final ResourceLocation texture3 = new ResourceLocation("aincog:blocks/gen_ore_stage_3");

        public TextureAtlasSprite sprite1;
        public TextureAtlasSprite sprite2;
        public TextureAtlasSprite sprite3;

        public IBakedModel model1;
        public IBakedModel model2;
        public IBakedModel model3;

        @Override
        public void handleTextureStitch(TextureStitchEvent.Pre e) {
            sprite1 = e.getMap().registerSprite(texture1);
            sprite2 = e.getMap().registerSprite(texture2);
            sprite3 = e.getMap().registerSprite(texture3);
        }

        @Override
        public void handleModelBake(ModelBakeEvent e) {
            //IBakedModel modelBase = e.getModelRegistry().getObject(new ModelResourceLocation("aincog:block/ore_base"));
            IBakedModel modelBase =  AbsoluteModelRegistry.getBakedModel(new ResourceLocation("aincog:block/ore_base"));
            if (modelBase == null) {
                LogHelper.errorLog("Couldn't get ore_base model for CrystalOre models");
                return;
            }
            if (sprite1 != null) {
                model1 = QuadUtil.addGlowingQuads(modelBase, sprite1, -1, 1);
            } else {
                LogHelper.errorLog("CrystalOre stage 1 texture was not created properly");
                return;
            }
            if (sprite2 != null) {
                model2 = QuadUtil.addGlowingQuads(modelBase, sprite2, -1, 1);
            } else {
                LogHelper.errorLog("CrystalOre stage 2 texture was not created properly");
                return;
            }
            if (sprite3 != null) {
                model3 = QuadUtil.addGlowingQuads(modelBase, sprite3, -1, 1);
            } else {
                LogHelper.errorLog("CrystalOre stage 3 texture was not created properly");
            }
        }
    }
}
