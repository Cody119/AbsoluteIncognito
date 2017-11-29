package com.superRainbowNinja.aincog.common.items;

import com.superRainbowNinja.aincog.AIncogData;
import com.superRainbowNinja.aincog.common.capabilites.ICoreContainer;
import com.superRainbowNinja.aincog.common.capabilites.IMultiToolCapable;
import com.superRainbowNinja.aincog.common.capabilites.IPoweredWeaponCap;
import com.superRainbowNinja.aincog.common.tileEntity.MachineFrameTile;
import com.superRainbowNinja.aincog.refrence.Reference;
import com.superRainbowNinja.aincog.util.DamageUtil;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by SuperRainbowNinja on 26/11/2017.
 */
public class MultiTool extends CoreContainerItem {
    public static final int MAX_DAMAGE = 100;
    public static final String NAME = "multi_tool";

    public MultiTool() {
        super(NAME);
        setMaxStackSize(1);
        //setMaxDamage(MAX_DAMAGE);
        setNoRepair();
    }

    @Override
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        IBlockState state = worldIn.getBlockState(pos);
        TileEntity tile;
        if (!worldIn.isRemote) {
            boolean countDown = false;
            ICoreContainer cap1 = stack.getCapability(ICoreContainer.CORE_CONTAINER_CAP, EnumFacing.DOWN);
            if (cap1.hasCore()) {
                IMultiToolCapable cap2 = cap1.getCoreItemStack().getCapability(IMultiToolCapable.MULTI_TOOL_CAP, null);
                if (cap2 != null) {
                    countDown =
                            cap2.onItemUse(stack, playerIn, worldIn, pos, hand, facing, hitX, hitY, hitZ)
                                    == EnumActionResult.SUCCESS;
                } else if (state.getBlock().hasTileEntity(state) && (tile = worldIn.getTileEntity(pos)) instanceof MachineFrameTile) {
                    ((MachineFrameTile) tile).wrench(playerIn, facing, hitX, hitY, hitZ);
                    countDown = true;
                }

            } else if (state.getBlock().hasTileEntity(state) && (tile = worldIn.getTileEntity(pos)) instanceof MachineFrameTile) {
                ((MachineFrameTile) tile).removeFrame(playerIn.getPositionVector());
                countDown = true;
            }
            if (countDown) {
                playerIn.getCooldownTracker().setCooldown(stack.getItem(), 1);
            }
            return EnumActionResult.SUCCESS;
        } else {
            return EnumActionResult.SUCCESS;
        }
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity) {

        if (!player.getEntityWorld().isRemote && entity instanceof EntityLivingBase) {
            ICoreContainer cap1 = ICoreContainer.getCap(stack);
            if (cap1.hasCore()) {
                IMultiToolCapable cap2 = cap1.getCoreItemStack().getCapability(IMultiToolCapable.MULTI_TOOL_CAP, null);
                if (cap2 != null) {
                    return cap2.onLeftClickEntity(stack, player, entity);
                }
            }
        }
        return false;
    }

    @SideOnly(Side.CLIENT)
    public static class CoreColor implements IItemColor {
        @Override
        public int getColorFromItemstack(ItemStack stack, int tintIndex) {
            //should be assured that the sword has a core as the rest of the model has no tint index
            if (ICoreContainer.hasCore(stack)) {
                return tintIndex == 1 ? ICoreContainer.getCap(stack).getCoreItem().getPrimaryColor(stack) :
                        (tintIndex == 2) ? ICoreContainer.getCap(stack).getCoreItem().getSecondaryColor(stack) :
                                0;
            } else {
                return 0xFFFFFFFF;
            }
        }
    }

    public static final ModelResourceLocation HANDLE = new ModelResourceLocation(Reference.MOD_ID + ":" + "multi_tool", "inventory");
    public static final ModelResourceLocation OVERLAY = new ModelResourceLocation(Reference.MOD_ID + ":" + "multi_tool_overlay", "inventory");
    public static final ModelResourceLocation CORE = new ModelResourceLocation(Reference.MOD_ID + ":" + "tool_core", "inventory");

    @Override
    @SideOnly(Side.CLIENT)
    public void registerModels() {
        super.registerModels();
        ModelBakery.registerItemVariants(AIncogData.MULTI_TOOL, OVERLAY, CORE);
    }
}
