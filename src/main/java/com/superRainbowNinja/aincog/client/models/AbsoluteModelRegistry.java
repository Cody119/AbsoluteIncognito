package com.superRainbowNinja.aincog.client.models;

import com.superRainbowNinja.aincog.util.LogHelper;
import com.superRainbowNinja.aincog.util.QuadUtil;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Created by SuperRainbowNinja on 7/10/2016.
 *
 */
@SideOnly(Side.CLIENT)
public enum AbsoluteModelRegistry {
    INSTANCE;

    ArrayList<IModelEntry> modelEntries;

    AbsoluteModelRegistry() {
        modelEntries = new ArrayList<>();
    }

    public static IBakedModel getBakedModel(ResourceLocation loc) {
        IBakedModel model = null;
        try {
            IModel modelTest = ModelLoaderRegistry.getModel(loc);
            model = modelTest.bake(modelTest.getDefaultState(), DefaultVertexFormats.ITEM, ModelLoader.defaultTextureGetter());
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return model;
    }

    public void registerModelEntry(IModelEntry modelEntry) {
        modelEntries.add(modelEntry);
    }

    public void buildAndAddGlowingTexture(ModelResourceLocation model, ResourceLocation texture) {
        registerModelEntry(new BasicGlowModel(model, texture));
    }

    public void buildAndAddGlowingTexture(ModelResourceLocation model, ResourceLocation texture, int tint) {
        registerModelEntry(new BasicGlowModel(model, texture).setTint(tint));
    }

    public void registerModelModelBakeEventSub(ModelResourceLocation model, Consumer<ModelBakeEvent> bakeEvent) {
        registerModelEntry(new ModelBakeEventSub(model, bakeEvent));
    }

    public void registerModelGetter(final ModelResourceLocation model, Consumer<IBakedModel> getter) {
        registerModelEntry(new ModelBakeEventSub(model, (e) -> getter.accept(e.getModelRegistry().getObject(model))));
    }

    public void registerModelReplacer(ModelResourceLocation model, Supplier<IBakedModel> getter) {
        registerModelEntry(new ModelReplacer(model, getter));
    }

    @SubscribeEvent
    public void preTixtureStitch(TextureStitchEvent.Pre event) {
        for (IModelEntry e : modelEntries) {
            e.handleTextureStitch(event);
        }
    }

    @SubscribeEvent
    public void onModelBakeEvent(ModelBakeEvent event) {
        for (IModelEntry e : modelEntries) {
            e.handleModelBake(event);
        }
    }

    /*---------------------------
    |  Model Entry Classes       |
    ----------------------------*/

    public interface IModelEntry {
        void handleTextureStitch(TextureStitchEvent.Pre e);
        void handleModelBake(ModelBakeEvent e);
    }

    public static class ModelEntry implements IModelEntry{
        protected ModelResourceLocation modelLoc;

        public ModelEntry(ModelResourceLocation modelIn) {
            modelLoc = modelIn;
        }

        ModelResourceLocation getModelResourceLocation() {
            return modelLoc;
        }

        @Override
        public void handleTextureStitch(TextureStitchEvent.Pre e){}
        @Override
        public void handleModelBake(ModelBakeEvent e){}
    }

    public static class ModelReplacer extends ModelEntry {
        private Supplier<IBakedModel> modelGetter;

        public ModelReplacer(ModelResourceLocation modelIn, Supplier<IBakedModel> getter) {
            super(modelIn);
            modelGetter = getter;
        }
        @Override
        public void handleModelBake(ModelBakeEvent e) {
            e.getModelRegistry().putObject(getModelResourceLocation(), modelGetter.get());
        }
    }

    public static class ModelBakeEventSub extends ModelEntry{
        private Consumer<ModelBakeEvent> callBack;

        public ModelBakeEventSub(ModelResourceLocation modelIn, Consumer<ModelBakeEvent> callBackIn) {
            super(modelIn);
            callBack = callBackIn;
        }

        @Override
        public void handleModelBake(ModelBakeEvent e){
            callBack.accept(e);
        }
    }
/*
    private static class PoweredWeaponModelEntry extends ModelEntry{
        ModelResourceLocation subModels[];

        PoweredWeaponModelEntry(ModelResourceLocation modelIn, ModelResourceLocation ... subModelsIn) {
            super(modelIn);
            subModels = subModelsIn;
        }
    }
*/
    public static class SingleTextureModel extends ModelEntry {
        private ResourceLocation textLoc;
        private TextureAtlasSprite text;

        public SingleTextureModel(ModelResourceLocation modelIn, ResourceLocation textIn) {
            super(modelIn);
            textLoc = textIn;
        }

        ResourceLocation getTextureResourceLocation() {
            return textLoc;
        }

        TextureAtlasSprite getTexture() {
            return text;
        }

        @Override
        public void handleTextureStitch(TextureStitchEvent.Pre e) {
            text = e.getMap().registerSprite(textLoc);
        }
    }

    public static class BasicGlowModel extends SingleTextureModel {
        private int RGBA = -1;
        private int tint = -1;

        public BasicGlowModel(ModelResourceLocation r, ResourceLocation textIn) {
            super(r, textIn);
        }

        @Override
        public void handleModelBake(ModelBakeEvent e) {
            IBakedModel model = e.getModelRegistry().getObject(getModelResourceLocation());
            TextureAtlasSprite text = getTexture();
            if (model != null && text != null) {
                e.getModelRegistry().putObject(getModelResourceLocation(), QuadUtil.addGlowingQuads(model, text, RGBA, tint));
            } else {
                LogHelper.errorLog("attempt to modify model " + getModelResourceLocation() + "but " +
                    (model == null ? "model" : "texture") + " was null");
            }
        }

        public BasicGlowModel setRGBA(int in) {
            RGBA = in;
            return this;
        }

        public BasicGlowModel setTint(int in) {
            tint = in;
            return this;
        }
    }
}
