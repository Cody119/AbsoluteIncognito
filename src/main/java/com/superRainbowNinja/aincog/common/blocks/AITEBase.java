package com.superRainbowNinja.aincog.common.blocks;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Created by SuperRainbowNinja on 12/11/2016.
 *
 * Very similar to net.minecraft.block.BlockContainer, but a few redundancy's removed and all the advantages of my own base class
 */
public abstract class AITEBase extends AIBlockBase implements ITileEntityProvider {
    public AITEBase(String name) {
        super(name);
        this.hasTileEntity = true;
    }

    public AITEBase(String name, Material material) {
        super(name, material);
        this.hasTileEntity = true;
    }

    //@Override
    //public EnumBlockRenderType getRenderType(IBlockState state) {
    //    return EnumBlockRenderType.MODEL;
    //}

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        super.breakBlock(world, pos, state);
        //have to manually remove the te
        world.removeTileEntity(pos);
    }

    @Override
    public boolean eventReceived(IBlockState state, World worldIn, BlockPos pos, int id, int param) {
        super.eventReceived(state, worldIn, pos, id, param);
        TileEntity tileentity = worldIn.getTileEntity(pos);
        return tileentity == null ? false : tileentity.receiveClientEvent(id, param);
    }
}
