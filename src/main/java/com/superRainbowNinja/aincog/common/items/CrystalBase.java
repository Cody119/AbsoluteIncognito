package com.superRainbowNinja.aincog.common.items;

import com.superRainbowNinja.aincog.AIncogData;
import com.superRainbowNinja.aincog.client.models.AbsoluteModelRegistry;
import com.superRainbowNinja.aincog.proxys.ClientProxy;
import net.minecraft.client.Minecraft;
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

import java.awt.*;

/**
 * Created by SuperRainbowNinja on 14/11/2017.
 */
public class CrystalBase extends AIItemBase{
    public final int color;

    public CrystalBase(String name, int colorIn) {
        super(name);
        color = colorIn;
        setMaxStackSize(63);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerModels() {
        ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation("aincog:crystal", "inventory"));
        //AbsoluteModelRegistry.INSTANCE.registerModelReplacer(new ModelResourceLocation(getRegistryName().toString(), "inventory"), () -> ClientProxy.CRYSTAL_MODELS.crystalModel);
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
            return color;
        }
    }

//    public static class ModelBuilder implements AbsoluteModelRegistry.IModelEntry {
//
//        public IBakedModel crystalModel;
//
//        @Override
//        public void handleTextureStitch(TextureStitchEvent.Pre e) {}
//
//        @Override
//        public void handleModelBake(ModelBakeEvent e) {
//            crystalModel =  AbsoluteModelRegistry.getBakedModel(new ResourceLocation("aincog:item/crystal"));
//        }
//    }
}
