package com.superRainbowNinja.aincog;

import com.superRainbowNinja.aincog.client.models.AbsoluteModelRegistry;
import com.superRainbowNinja.aincog.common.blocks.AIBlockBase;
import com.superRainbowNinja.aincog.common.blocks.CrystalOre;
import com.superRainbowNinja.aincog.common.blocks.MachineFrame;
import com.superRainbowNinja.aincog.common.fluids.AIFluid;
import com.superRainbowNinja.aincog.common.fluids.AIFluidBlock;
import com.superRainbowNinja.aincog.common.fluids.MoltenFluid;
import com.superRainbowNinja.aincog.common.items.*;
import com.superRainbowNinja.aincog.proxys.CommonProxy;
import com.superRainbowNinja.aincog.refrence.OreDictNames;
import com.superRainbowNinja.aincog.refrence.Reference;
import com.superRainbowNinja.aincog.util.EnumPosition;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;

/**
 * Created by SuperRainbowNinja on 4/10/2016.
 *
 * Store all the blocks, items and other stuff in here
 * TODO damn wat a sexy looking class (ill fix it eventually)
 */
public class AIncogData {
    //creative tab
    public static final CreativeTabs A_INCOG_C_Tab = new CreativeTabs(Reference.MOD_ID) {
        @Override
        public Item getTabIconItem() {
            return AIncogData.MACHINE_FRAME.item;
        }
    };

    //blocks
    public static final MachineFrame MACHINE_FRAME = new MachineFrame();
    public static final AIBlockBase GLOW_TEST = new SimpleGlowingBlock("glow_test");

    public static class SimpleGlowingBlock extends AIBlockBase {

        public SimpleGlowingBlock(String name) {
            super(name);
        }

        @Override
        @SideOnly(Side.CLIENT)
        public BlockRenderLayer getBlockLayer() {
            return BlockRenderLayer.CUTOUT_MIPPED;
        }

        @Override
        @SideOnly(Side.CLIENT)
        public void registerModels() {
            super.registerModels();
            AbsoluteModelRegistry.INSTANCE.buildAndAddGlowingTexture(new ModelResourceLocation(getRegistryName().toString()), new ResourceLocation("aincog:blocks/gen_ore"), 1);
        }
    }

    // items
    public static final PoweredWeapon LASER_SWORD = new PoweredWeapon("laser_sword", 2000);
    public static final MachineLock MACHINE_LOCK = new MachineLock();
    public static final MakeshiftCore MAKESHIFT_CORE = new MakeshiftCore();
    public static final TankComponent TANK_COMPONENT = new TankComponent();

    public static class Coil extends AIItemBase implements IMachineComponent {
        EnumPosition[] positions = new EnumPosition[]{EnumPosition.TOP, EnumPosition.BOTTOM};

        Coil() {
            super("coil");
            //setMaxStackSize(1);
        }

        @Override
        public AIItemBase getItem() {
            return this;
        }

        @Override
        public EnumPosition[] getComponentPlaces() {
            return positions;
        }
    }
    public static final Coil COIL = new Coil();

    public static class Piece extends AIItemBase implements IMachineComponent {
        EnumPosition[] positions = new EnumPosition[]{EnumPosition.TOP_SINGLE, EnumPosition.BOTTOM_SINGLE};

        Piece() {
            super("piece");
            //setMaxStackSize(1);
        }

        @Override
        public AIItemBase getItem() {
            return this;
        }

        @Override
        public EnumPosition[] getComponentPlaces() {
            return positions;
        }
    }
    public static final Piece PEICE = new Piece();

    public static final AIFluid GREEN_CRYSTAL_FLUID = new MoltenFluid("green_crystal", 0xFF00FF00);
    public static final AIFluidBlock GREEN_CRYSTAL_FLUID_BLOCK = new AIFluidBlock(GREEN_CRYSTAL_FLUID, "green_crystal_fluid");

    public static final AIFluid RED_CRYSTAL_FLUID = new MoltenFluid("red_crystal", 0xFFFF0000);
    public static final AIFluidBlock RED_CRYSTAL_FLUID_BLOCK = new AIFluidBlock(RED_CRYSTAL_FLUID, "red_crystal_fluid");

    public static final AIFluid BLUE_CRYSTAL_FLUID = new MoltenFluid("blue_crystal", 0xFF0000FF);
    public static final AIFluidBlock BLUE_CRYSTAL_FLUID_BLOCK = new AIFluidBlock(BLUE_CRYSTAL_FLUID, "blue_crystal_fluid");

    public static final CrystalOre RED_CRYSTAL_ORE = new CrystalOre("red_crystal_ore", 0xFFFF0000);
    public static final CrystalOre GREEN_CRYSTAL_ORE = new CrystalOre("green_crystal_ore", 0xFF00FF00);
    public static final CrystalOre BLUE_CRYSTAL_ORE = new CrystalOre("blue_crystal_ore", 0xFF0000FF);

    public static void intiStatics() {
        CommonProxy.add(MACHINE_FRAME);
        CommonProxy.add(GLOW_TEST);

        CommonProxy.add(LASER_SWORD);
        CommonProxy.add(MAKESHIFT_CORE);
        CommonProxy.add(COIL);
        CommonProxy.add(MACHINE_LOCK);
        CommonProxy.add(PEICE);
        CommonProxy.add(TANK_COMPONENT);

        CommonProxy.add(GREEN_CRYSTAL_FLUID_BLOCK);
        CommonProxy.add(RED_CRYSTAL_FLUID_BLOCK);
        CommonProxy.add(BLUE_CRYSTAL_FLUID_BLOCK);

        CommonProxy.add(RED_CRYSTAL_ORE);
        CommonProxy.add(GREEN_CRYSTAL_ORE);
        CommonProxy.add(BLUE_CRYSTAL_ORE);
    }

    public static void initRecipes() {
        if (OreDictionary.doesOreNameExist(OreDictNames.COPPER_INGOT)) {
            GameRegistry.addRecipe(new ShapedOreRecipe(AIncogData.COIL, "III", "IBI", "III", 'I', OreDictNames.COPPER_INGOT, 'B', Blocks.IRON_BARS));
        } else {
            GameRegistry.addRecipe(new ShapedOreRecipe(AIncogData.COIL, "III", "IBI", "III", 'I', "ingotIron", 'B', Blocks.IRON_BARS));
        }
        GameRegistry.addRecipe(new ShapedOreRecipe(AIncogData.MACHINE_FRAME, "IBI", "BRB", "IBI", 'B', "ingotIron", 'I', Blocks.IRON_BARS, 'R', "dustRedstone"));
        GameRegistry.addRecipe(new ShapedOreRecipe(AIncogData.MACHINE_FRAME, "BIB", "IRI", "BIB", 'B', "ingotIron", 'I', Blocks.IRON_BARS, 'R', "dustRedstone"));
        GameRegistry.addRecipe(new ShapedOreRecipe(AIncogData.MACHINE_LOCK, "BBB", "BGB", "BBB", 'G', "blockGlass", 'B', Blocks.IRON_BARS));
        GameRegistry.addRecipe(new ShapedOreRecipe(AIncogData.MAKESHIFT_CORE, "IBI", "BRB", "IBI", 'B', "ingotIron", 'I', "stickWood", 'R', "dustRedstone"));
        GameRegistry.addRecipe(new ShapedOreRecipe(AIncogData.MAKESHIFT_CORE, "BIB", "IRI", "BIB", 'B', "ingotIron", 'I', "stickWood", 'R', "dustRedstone"));
    }
}
