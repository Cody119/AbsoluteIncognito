package com.superRainbowNinja.aincog.common.fluids;

import com.superRainbowNinja.aincog.AIncogData;
import com.superRainbowNinja.aincog.common.IRegistryEntry;
import com.superRainbowNinja.aincog.common.blocks.AIBlockBase;
import com.superRainbowNinja.aincog.refrence.Reference;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by SuperRainbowNinja on 9/12/2016.
 *
 * TODO faces are culling when they shouldn't, not sure y
 * TODO go through fluid settings, things like sustaining plants
 * TODO slowly repair AI items? (or maybe just the armour when ur wearing it?)
 */
public class AIFluidBlock extends BlockFluidClassic implements IRegistryEntry{
    public final String name;
    //public final ItemBlock item;
    public final ModelResourceLocation fluidLocation;

    public AIFluidBlock(Fluid fluid, String inName) {
        super(fluid, Material.LAVA);
        name = inName;
        fluidLocation = new ModelResourceLocation(Reference.MOD_ID + ":" + "fluids", name);
        setCreativeTab(AIncogData.A_INCOG_C_Tab);
        setRegistryName(name);
        setUnlocalizedName(getRegistryName().toString());
        setQuantaPerBlock(4); //how far it will flow from the source, this is the same as lava
        //item = (ItemBlock) new ItemBlock(this).setRegistryName(this.name);
    }

    @Override
    public void registerObjects() {
        GameRegistry.register(this);
        //GameRegistry.register(this.item);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerModels() {
        //ModelLoader.setCustomMeshDefinition(item, stack1 -> fluidLocation);
        ModelLoader.setCustomStateMapper(this, new StateMapperBase() {
            protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
                return fluidLocation;
            }
        });
    }

}
