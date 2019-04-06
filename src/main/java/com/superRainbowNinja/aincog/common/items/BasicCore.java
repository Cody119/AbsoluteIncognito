package com.superRainbowNinja.aincog.common.items;

import com.superRainbowNinja.aincog.client.models.AbsoluteModelRegistry;
import com.superRainbowNinja.aincog.proxys.ClientProxy;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

/**
 * Created by SuperRainbowNinja on 15/11/2017.
 */
public class BasicCore extends CoreItem {
    private int primaryColor;
    private int secondaryColor;
    private int outlineColor;
    private float efficiency;
    private float speed;
    private float strength;

    public BasicCore(String name) {
        super(name);
    }

    @Override
    public int getMaxCoreDamage(ItemStack core) {
        return 40000;
    }

    @Override
    public int getCoreDamage(ItemStack core) {
        return core.getItemDamage();
    }

    @Override
    public boolean setCoreDamage(ItemStack core, int dmg) {
        core.setItemDamage(dmg);
        return dmg == getMaxCoreDamage(core);
    }

    @Override
    public CoreType getCoreType(@Nullable ItemStack core) {
        return CoreType.BASIC;
    }

    public BasicCore setColors(int primaryColorIn, int secondaryColorIn, int outlineColorIn) {
        primaryColor = primaryColorIn;
        secondaryColor = secondaryColorIn;
        outlineColor = outlineColorIn;
        return this;
    }

    @Override
    public int getPrimaryColor(ItemStack core) {
        return primaryColor;
    }

    @Override
    public int getSecondaryColor(ItemStack core) {
        return secondaryColor;
    }

    @Override
    public int getOutlineColor(ItemStack core) {
        return outlineColor;
    }

    public BasicCore setAttributes(float efficiencyIn, float speedIn, float strengthIn) {
        efficiency = efficiencyIn;
        speed = speedIn;
        strength = strengthIn;
        return this;
    }

    @Override
    public float getEfficiency(ItemStack core) {
        return efficiency;
    }

    @Override
    public float getSpeed(ItemStack core) {
        return speed;
    }

    @Override
    public float getStrength(ItemStack core) {
        return strength;
    }


    @Override
    @SideOnly(Side.CLIENT)
    public void registerModels() {
        ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation("aincog:core_basic", "inventory"));
    }

    @SideOnly(Side.CLIENT)
    public CoreColor getColor() {
        return new CoreColor();
    }

    @SideOnly(Side.CLIENT)
    public class CoreColor implements IItemColor {
        @Override
        public int colorMultiplier(ItemStack stack, int tintIndex) {
            //return Color.HSBtoRGB((Minecraft.getMinecraft().theWorld.getWorldTime() % 360)/360f, 0.9f, 0.7f);
            return
                    tintIndex == 0 ? primaryColor :
                    tintIndex == 1 ? secondaryColor :
                    outlineColor;
        }
    }
}
