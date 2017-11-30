package com.superRainbowNinja.aincog.common.capabilites;

import com.superRainbowNinja.aincog.client.models.MultiToolModel;
import com.superRainbowNinja.aincog.common.items.MultiTool;
import com.superRainbowNinja.aincog.common.tileEntity.MachineFrameTile;
import com.superRainbowNinja.aincog.proxys.ClientProxy;
import com.superRainbowNinja.aincog.util.DamageUtil;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by SuperRainbowNinja on 29/11/2017.
 */
public class MultiToolBladeCap implements IMultiToolCapable, ICapabilityProvider {
    public static final float BASE_DAMAGE = 5f;
    public static final float DURABILITY_PER_HIT = 10;

    @Override
    public List<BakedQuad> getModel(ItemStack stack, MultiToolModel model) {
        return ClientProxy.MULTI_BLADE_MODEL;
    }

    @Override
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        return EnumActionResult.PASS;
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity) {
        ICoreContainer cap = ICoreContainer.getCap(stack);
        float efficiency = cap.getCoreItem().getStrength(stack);
        if (player.getCooledAttackStrength(0.5F) > 0.95) {
            DamageUtil.swingSwordParticlesDamage(player, (EntityLivingBase) entity, BASE_DAMAGE * efficiency, 0.5f);
        } else {
            DamageUtil.playerAttack(player, (EntityLivingBase) entity, 2f * efficiency);
        }
        ((MultiTool) stack.getItem()).damage(stack, (int)Math.ceil(DURABILITY_PER_HIT*cap.getCoreItem().getEfficiency(stack)), cap);
        return true;
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == MULTI_TOOL_CAP;
    }

    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        return capability == MULTI_TOOL_CAP ? (T) this : null;
    }
}
