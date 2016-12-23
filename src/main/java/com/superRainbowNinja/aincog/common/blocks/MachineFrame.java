package com.superRainbowNinja.aincog.common.blocks;

import com.superRainbowNinja.aincog.common.tileEntity.MachineFrameTile;
import com.superRainbowNinja.aincog.refrence.Reference;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;


/**
 * Created by SuperRainbowNinja on 20/04/2016.
 *
 *
 * Squares
 *  x/y/z (inclusive ranges):
 *      2 - 5
 *      6 - 9
 *      10 - 13
 */
public class MachineFrame extends AITEBase {

    public static PropertyBool LOCKED = PropertyBool.create("locked");

    public MachineFrame() {
        super("machine_frame", Material.GLASS);
        super.setHardness(0.3F);
        setDefaultState(blockState.getBaseState().withProperty(LOCKED, false));
    }

    @Override
    public BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, LOCKED);
    }

    @SideOnly(Side.CLIENT)
    public static class CoreColor implements IItemColor {
        @Override
        public int getColorFromItemstack(ItemStack stack, int tintIndex) {
            return 0xFFFFFFFF;
        }
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return 0; //don't use meta
    }

    @Deprecated
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        TileEntity tile = worldIn.getTileEntity(pos);
        if (tile instanceof MachineFrameTile) {
            return state.withProperty(LOCKED, ((MachineFrameTile) tile).isLocked());
        }
        return state;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (worldIn.isRemote)
            return true;
        TileEntity te = worldIn.getTileEntity(pos);
        if (te instanceof MachineFrameTile) {
            ((MachineFrameTile) te).playerActivate(state, playerIn, hand, heldItem, side, hitX, hitY, hitZ);
        }
        return true;
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {


        TileEntity tile = worldIn.getTileEntity(pos);
        if (tile instanceof MachineFrameTile) {
            ((MachineFrameTile) tile).dropEverything();
        }

        super.breakBlock(worldIn, pos, state);
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new MachineFrameTile();
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.INVISIBLE;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) { return false; }



    //Not 2 sure what this does but it seems to make the texture look nicer
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer()
    {
        return BlockRenderLayer.CUTOUT;
    }

    public static ModelResourceLocation MACHINE_FRAME = new ModelResourceLocation(Reference.MOD_ID + ":" + "machine_frame");

    @Override
    @SideOnly(Side.CLIENT)
    public void registerModels() {
        super.registerModels();
    }
}
