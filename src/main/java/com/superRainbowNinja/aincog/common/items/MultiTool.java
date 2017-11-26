package com.superRainbowNinja.aincog.common.items;

import com.superRainbowNinja.aincog.common.tileEntity.MachineFrameTile;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Created by SuperRainbowNinja on 26/11/2017.
 */
public class MultiTool extends AIItemBase {
    public static final int MAX_DAMAGE = 100;
    public static final String NAME = "multi_tool";

    public MultiTool() {
        super(NAME);
        setMaxStackSize(1);
        setMaxDamage(MAX_DAMAGE);
        setNoRepair();
    }

    @Override
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        IBlockState state = worldIn.getBlockState(pos);
        TileEntity tile;
        if (state.getBlock().hasTileEntity(state) && (tile = worldIn.getTileEntity(pos)) instanceof MachineFrameTile) {
            if (!worldIn.isRemote) {
                ((MachineFrameTile) tile).wrench(playerIn, facing, hitX, hitY, hitZ);
                return EnumActionResult.SUCCESS;
            } else {
                return EnumActionResult.SUCCESS;
            }
        }
        return EnumActionResult.PASS;

    }
}
