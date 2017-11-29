package com.superRainbowNinja.aincog.common.capabilites;

import com.superRainbowNinja.aincog.client.models.MultiToolModel;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

/**
 * Created by SuperRainbowNinja on 28/11/2017.
 */
public interface IMultiToolCapable {
    @CapabilityInject(IMultiToolCapable.class)
    Capability<IMultiToolCapable> MULTI_TOOL_CAP = null;

    @SideOnly(Side.CLIENT)
    default List<BakedQuad> getModel(ItemStack stack, MultiToolModel model) {
        return model.handleCore;
    }

    EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ);
    boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity);
}
