package com.superRainbowNinja.aincog.common.items;

import com.superRainbowNinja.aincog.common.capabilites.IPoweredWeaponCap;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by SuperRainbowNinja on 10/10/2016.
 */
public abstract class CoreItem extends AIItemBase implements ICore {
    public CoreItem(String name) {
        super(name);
        setMaxStackSize(1);
    }

    @Override
    public int getMaxDamage(ItemStack stack) {
        return getMaxCoreDamage(stack);
    }

    @Override
    public boolean coreIsFunctional(ItemStack stack) {
        return true;
    }

    @SideOnly(Side.CLIENT)
    public static class CoreColor implements IItemColor {
        @Override
        public int getColorFromItemstack(ItemStack stack, int tintIndex) {
            ICore core = (ICore) stack.getItem();
            return tintIndex == 0 ? core.getPrimaryColor(stack) : tintIndex == 1 ? core.getSecondaryColor(stack) : core.getOutlineColor(stack);
        }
    }
}
