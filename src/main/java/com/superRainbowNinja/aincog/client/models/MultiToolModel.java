package com.superRainbowNinja.aincog.client.models;

import com.google.common.collect.ImmutableList;
import com.superRainbowNinja.aincog.common.capabilites.ICoreContainer;
import com.superRainbowNinja.aincog.common.capabilites.IMultiToolCapable;
import com.superRainbowNinja.aincog.common.capabilites.IPoweredWeaponCap;
import com.superRainbowNinja.aincog.util.LogHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by SuperRainbowNinja on 28/11/2017.
 */
public class MultiToolModel extends SmartModelOverride{

    private IBakedModel handle;
    private IBakedModel overLay;
    private IBakedModel core;

    public final ImmutableList<BakedQuad> handleBare;
    public final ImmutableList<BakedQuad> handleCore;

    private boolean hasCore;
    private IMultiToolCapable cap;
    private ItemStack stack;

    public MultiToolModel(IBakedModel handleIn, IBakedModel overLayIn, IBakedModel coreIn) {
        handle = handleIn;
        overLay = overLayIn;
        core = coreIn;

        ArrayList<BakedQuad> test = new ArrayList<>();
        test.addAll(handle.getQuads(null, null, 0));
        test.addAll(overLay.getQuads(null, null, 0));
        int one = test.size();
        test.addAll(core.getQuads(null, null, 0));

        handleCore = ImmutableList.copyOf(test);
        handleBare = handleCore.subList(0, one);
    }

    @Override
    public IBakedModel handleItemState(SmartModel originalModel, ItemStack stack, World world, EntityLivingBase entity) {
        ICoreContainer cap = ICoreContainer.getCap(stack);
        hasCore = cap.hasCore();
        if (hasCore) {
            this.cap = cap.getCoreItemStack().getCapability(IMultiToolCapable.MULTI_TOOL_CAP, EnumFacing.DOWN);
        }
        this.stack = stack;
        return originalModel;
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand) {
        List<BakedQuad> quads = handleBare;
        if (hasCore) {
            if (cap != null) {
                quads = cap.getModel(stack, this);
            } else {
                quads = handleCore;
            }
        }
        stack = null;
        return quads;
    }
}
