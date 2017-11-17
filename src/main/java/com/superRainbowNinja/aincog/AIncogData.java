package com.superRainbowNinja.aincog;

import com.superRainbowNinja.aincog.client.models.AbsoluteModelRegistry;
import com.superRainbowNinja.aincog.common.blocks.AIBlockBase;
import com.superRainbowNinja.aincog.common.blocks.CrystalOre;
import com.superRainbowNinja.aincog.common.blocks.MachineFrame;
import com.superRainbowNinja.aincog.common.fluids.AIFluid;
import com.superRainbowNinja.aincog.common.fluids.AIFluidBlock;
import com.superRainbowNinja.aincog.common.fluids.MoltenFluid;
import com.superRainbowNinja.aincog.common.items.*;
import com.superRainbowNinja.aincog.common.machineLogic.ArcFurnaceLogic;
import com.superRainbowNinja.aincog.common.machineLogic.OreFormerLogic;
import com.superRainbowNinja.aincog.proxys.CommonProxy;
import com.superRainbowNinja.aincog.refrence.OreDictNames;
import com.superRainbowNinja.aincog.refrence.Reference;
import com.superRainbowNinja.aincog.util.AssortedUtil;
import com.superRainbowNinja.aincog.util.EnumPosition;
import com.superRainbowNinja.aincog.util.MathUtil;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

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

    public static final CrystalBase WHITE_CRYSTAL = new CrystalBase("white_crystal", 0xFFFFFF);
    public static final CrystalBase RED_CRYSTAL = new CrystalBase("red_crystal", 0xFFFF0000);
    public static final CrystalBase GREEN_CRYSTAL = new CrystalBase("green_crystal", 0xFF00FF00);
    public static final CrystalBase BLUE_CRYSTAL = new CrystalBase("blue_crystal", 0xFF0000FF);

    public static final BasicCore WHITE_BASIC_CORE = new BasicCore("white_basic_core")
            .setColors(0xFFFFFF, 0xFFFFFF, 0xFFFFFF)
            .setAttributes(0.75f, 1.0f, 0.5f);
    public static final BasicCore RED_BASIC_CORE = new BasicCore("red_basic_core")
            .setColors(0xFFFF0000, 0xFFFF0000, 0xFFFF0000)
            .setAttributes(0.50f, 0.75f, 1.0f);
    public static final BasicCore GREEN_BASIC_CORE = new BasicCore("green_basic_core")
            .setColors(0xFF00FF00, 0xFF00FF00, 0xFF00FF00)
            .setAttributes(0.75f, 0.75f, 0.75f);
    public static final BasicCore BLUE_BASIC_CORE = new BasicCore("blue_basic_core")
            .setColors(0xFF0000FF, 0xFF0000FF, 0xFF0000FF)
            .setAttributes(1f, 5.0f, 0.75f);

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
    public static final Piece PIECE = new Piece();

    public static final AIFluid WHITE_CRYSTAL_FLUID = new MoltenFluid("white_crystal", 0xFFFFFFFF);
    public static final AIFluidBlock WHITE_CRYSTAL_FLUID_BLOCK = new AIFluidBlock(WHITE_CRYSTAL_FLUID, "white_crystal_fluid");

    public static final AIFluid GREEN_CRYSTAL_FLUID = new MoltenFluid("green_crystal", 0xFF00FF00);
    public static final AIFluidBlock GREEN_CRYSTAL_FLUID_BLOCK = new AIFluidBlock(GREEN_CRYSTAL_FLUID, "green_crystal_fluid");

    public static final AIFluid RED_CRYSTAL_FLUID = new MoltenFluid("red_crystal", 0xFFFF0000);
    public static final AIFluidBlock RED_CRYSTAL_FLUID_BLOCK = new AIFluidBlock(RED_CRYSTAL_FLUID, "red_crystal_fluid");

    public static final AIFluid BLUE_CRYSTAL_FLUID = new MoltenFluid("blue_crystal", 0xFF0000FF);
    public static final AIFluidBlock BLUE_CRYSTAL_FLUID_BLOCK = new AIFluidBlock(BLUE_CRYSTAL_FLUID, "blue_crystal_fluid");

    public static final CrystalOre WHITE_CRYSTAL_ORE = new CrystalOre("white_crystal_ore", 0xFFFFFFFF, WHITE_CRYSTAL);
    public static final CrystalOre RED_CRYSTAL_ORE = new CrystalOre("red_crystal_ore", 0xFFFF0000, RED_CRYSTAL);
    public static final CrystalOre GREEN_CRYSTAL_ORE = new CrystalOre("green_crystal_ore", 0xFF00FF00, GREEN_CRYSTAL);
    public static final CrystalOre BLUE_CRYSTAL_ORE = new CrystalOre("blue_crystal_ore", 0xFF0000FF, BLUE_CRYSTAL);

    public static void intiStatics() {
        CommonProxy.add(MACHINE_FRAME);
        CommonProxy.add(GLOW_TEST);

        CommonProxy.add(LASER_SWORD);
        CommonProxy.add(MAKESHIFT_CORE);
        CommonProxy.add(COIL);
        CommonProxy.add(MACHINE_LOCK);
        CommonProxy.add(PIECE);
        CommonProxy.add(TANK_COMPONENT);

        CommonProxy.add(WHITE_BASIC_CORE);
        CommonProxy.add(RED_BASIC_CORE);
        CommonProxy.add(GREEN_BASIC_CORE);
        CommonProxy.add(BLUE_BASIC_CORE);

        CommonProxy.add(WHITE_CRYSTAL);
        CommonProxy.add(RED_CRYSTAL);
        CommonProxy.add(GREEN_CRYSTAL);
        CommonProxy.add(BLUE_CRYSTAL);

        CommonProxy.add(WHITE_CRYSTAL_FLUID_BLOCK);
        CommonProxy.add(GREEN_CRYSTAL_FLUID_BLOCK);
        CommonProxy.add(RED_CRYSTAL_FLUID_BLOCK);
        CommonProxy.add(BLUE_CRYSTAL_FLUID_BLOCK);

        CommonProxy.add(WHITE_CRYSTAL_ORE);
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

        GameRegistry.addRecipe(new ShapelessOreRecipe(AIncogData.PIECE, Blocks.IRON_BARS, Blocks.IRON_BARS, Blocks.IRON_BARS));
        GameRegistry.addRecipe(new ShapedOreRecipe(AIncogData.TANK_COMPONENT, "III", "IBI", "III", 'I', "blockGlass", 'B', Blocks.IRON_BARS));

        GameRegistry.addRecipe(new ShapedOreRecipe(AIncogData.WHITE_BASIC_CORE, "IBI", "BRB", "IBI", 'B', "ingotIron", 'I', AIncogData.WHITE_CRYSTAL, 'R', "ingotGold"));
        GameRegistry.addRecipe(new ShapedOreRecipe(AIncogData.WHITE_BASIC_CORE, "BIB", "IRI", "BIB", 'B', "ingotIron", 'I', AIncogData.WHITE_CRYSTAL, 'R', "ingotGold"));
        GameRegistry.addRecipe(new ShapedOreRecipe(AIncogData.RED_BASIC_CORE, "IBI", "BRB", "IBI", 'B', "ingotIron", 'I', AIncogData.RED_CRYSTAL, 'R', "ingotGold"));
        GameRegistry.addRecipe(new ShapedOreRecipe(AIncogData.RED_BASIC_CORE, "BIB", "IRI", "BIB", 'B', "ingotIron", 'I', AIncogData.RED_CRYSTAL, 'R', "ingotGold"));
        GameRegistry.addRecipe(new ShapedOreRecipe(AIncogData.GREEN_BASIC_CORE, "IBI", "BRB", "IBI", 'B', "ingotIron", 'I', AIncogData.GREEN_CRYSTAL, 'R', "ingotGold"));
        GameRegistry.addRecipe(new ShapedOreRecipe(AIncogData.GREEN_BASIC_CORE, "BIB", "IRI", "BIB", 'B', "ingotIron", 'I', AIncogData.GREEN_CRYSTAL, 'R', "ingotGold"));
        GameRegistry.addRecipe(new ShapedOreRecipe(AIncogData.BLUE_BASIC_CORE, "IBI", "BRB", "IBI", 'B', "ingotIron", 'I', AIncogData.BLUE_CRYSTAL, 'R', "ingotGold"));
        GameRegistry.addRecipe(new ShapedOreRecipe(AIncogData.BLUE_BASIC_CORE, "BIB", "IRI", "BIB", 'B', "ingotIron", 'I', AIncogData.BLUE_CRYSTAL, 'R', "ingotGold"));

        OreFormerLogic.addRecipe(AssortedUtil.fluidStack(WHITE_CRYSTAL_FLUID.name, OreFormerLogic.FLUID_PER_ORE), WHITE_CRYSTAL_ORE.getDefaultState());
        OreFormerLogic.addRecipe(AssortedUtil.fluidStack(GREEN_CRYSTAL_FLUID.name, OreFormerLogic.FLUID_PER_ORE), GREEN_CRYSTAL_ORE.getDefaultState());
        OreFormerLogic.addRecipe(AssortedUtil.fluidStack(BLUE_CRYSTAL_FLUID.name, OreFormerLogic.FLUID_PER_ORE), BLUE_CRYSTAL_ORE.getDefaultState());
        OreFormerLogic.addRecipe(AssortedUtil.fluidStack(RED_CRYSTAL_FLUID.name, OreFormerLogic.FLUID_PER_ORE), RED_CRYSTAL_ORE.getDefaultState());

        ArcFurnaceLogic.RecipeRegistry.add(new FluidStack(FluidRegistry.getFluid(AIncogData.WHITE_CRYSTAL_FLUID.name), 500),
                new ItemStack(Items.REDSTONE, 16),
                new ItemStack(Items.COAL, 32),
                new ItemStack(Blocks.GLASS, 16)
        );
        ArcFurnaceLogic.RecipeRegistry.add(new FluidStack(FluidRegistry.getFluid(AIncogData.RED_CRYSTAL_FLUID.name), 500),
                new ItemStack(Items.REDSTONE, 16),
                new ItemStack(Items.GOLD_INGOT, 8),
                new ItemStack(Blocks.COBBLESTONE, 32)
        );
        ArcFurnaceLogic.RecipeRegistry.add(new FluidStack(FluidRegistry.getFluid(AIncogData.GREEN_CRYSTAL_FLUID.name), 500),
                new ItemStack(Items.REDSTONE, 16),
                new ItemStack(Items.IRON_INGOT, 16),
                new ItemStack(Blocks.DIRT, 32)
        );
        ArcFurnaceLogic.RecipeRegistry.add(new FluidStack(FluidRegistry.getFluid(AIncogData.BLUE_CRYSTAL_FLUID.name), 500),
                new ItemStack(Items.REDSTONE, 16),
                new ItemStack(Items.DYE, 8, 4),
                new ItemStack(Items.CLAY_BALL, 16)
        );
        ArcFurnaceLogic.RecipeRegistry.add(new FluidStack(FluidRegistry.getFluid(AIncogData.BLUE_CRYSTAL_FLUID.name), 500),
                new ItemStack(Items.REDSTONE, 16),
                new ItemStack(Items.DYE, 8, 4),
                new ItemStack(Blocks.CLAY, 4)
        );
    }
}
