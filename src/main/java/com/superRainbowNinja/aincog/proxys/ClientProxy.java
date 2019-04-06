package com.superRainbowNinja.aincog.proxys;

import com.google.common.collect.ImmutableList;
import com.superRainbowNinja.aincog.AIncogData;
import com.superRainbowNinja.aincog.client.models.LaserSwordModel;
import com.superRainbowNinja.aincog.client.models.MultiToolModel;
import com.superRainbowNinja.aincog.client.models.SmartModel;
import com.superRainbowNinja.aincog.client.models.tileEntityRenders.MachineFrameRender;
import com.superRainbowNinja.aincog.common.IRegistryEntry;
import com.superRainbowNinja.aincog.common.blocks.CrystalOre;
import com.superRainbowNinja.aincog.common.blocks.MachineFrame;
import com.superRainbowNinja.aincog.client.models.AbsoluteModelRegistry;
import com.superRainbowNinja.aincog.common.items.*;
import com.superRainbowNinja.aincog.common.tileEntity.MachineFrameTile;
import com.superRainbowNinja.aincog.refrence.Reference;
import com.superRainbowNinja.aincog.util.QuadUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;

import com.superRainbowNinja.aincog.common.items.PoweredWeapon;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by SuperRainbowNinja on 3/10/2016.
 */
public class ClientProxy extends CommonProxy {
    public static final CrystalOre.ModelBuilder CRYSTAL_ORE_MODELS = new CrystalOre.ModelBuilder();

    public static final ModelResourceLocation MULTI_BLADE_RECOURCE = new ModelResourceLocation(Reference.MOD_ID + ":" + "multi_tool_blade", "inventory");
    public static List<BakedQuad> MULTI_BLADE_MODEL = null;

    public static final IRegistryEntry MULTI_TOOL_SWORD = new IRegistryEntry() {
        @Override
        public void registerModels() {
            ModelBakery.registerItemVariants(AIncogData.MULTI_TOOL, MULTI_BLADE_RECOURCE);
        }
    };

    public void registerModels() {
        registryEntries.forEach(IRegistryEntry::registerModels);
    }

    @Override
    public void preInit() {
        super.preInit();

        add(MULTI_TOOL_SWORD);
        //its important that this is registered before the actual ores
        AbsoluteModelRegistry.INSTANCE.registerModelEntry(CRYSTAL_ORE_MODELS);
        //AbsoluteModelRegistry.INSTANCE.registerModelEntry(CRYSTAL_MODELS);


        //this loads on the server side for some reason, as far as ik it shouldnt so i muct be missing something?
        //will fix later, for now so long as its called dosent really matter where it is

        //TODO put these back
        AbsoluteModelRegistry.INSTANCE.registerModelModelBakeEventSub(PoweredWeapon.HANDLE, (event) -> {
            IBakedModel existingModel = event.getModelRegistry().getObject(PoweredWeapon.HANDLE);
            //Sword model setup
            event.getModelRegistry().putObject(PoweredWeapon.HANDLE, new SmartModel(existingModel,
                    new LaserSwordModel(
                            QuadUtil.addItemTint(existingModel, -1),//gotta remove the index already added
                            QuadUtil.addItemTint(event.getModelRegistry().getObject(PoweredWeapon.BLADE), 2),
                            QuadUtil.addItemTint(event.getModelRegistry().getObject(PoweredWeapon.CORE), 1)
                    )
            ));
        });

        AbsoluteModelRegistry.INSTANCE.registerModelModelBakeEventSub(MultiTool.HANDLE, (event) -> {
            IBakedModel existingModel = event.getModelRegistry().getObject(MultiTool.HANDLE);
            //Sword model setup
            event.getModelRegistry().putObject(MultiTool.HANDLE, new SmartModel(existingModel,
                    new MultiToolModel(
                            QuadUtil.addItemTint(existingModel, -1),//gotta remove the index already added
                            QuadUtil.addItemTint(event.getModelRegistry().getObject(MultiTool.OVERLAY), 2),
                            QuadUtil.addItemTint(event.getModelRegistry().getObject(MultiTool.CORE), 1)
                    )
            ));
        });

        AbsoluteModelRegistry.INSTANCE.registerModelModelBakeEventSub(MULTI_BLADE_RECOURCE, (event) -> {
            IBakedModel existingModel = event.getModelRegistry().getObject(MultiTool.HANDLE);
            MULTI_BLADE_MODEL = ImmutableList.<BakedQuad>builder()
                    .addAll(((MultiToolModel) ((SmartModel) existingModel).getItemOverrideList()).handleCore)
                    .addAll(QuadUtil.addItemTint(event.getModelRegistry().getObject(MULTI_BLADE_RECOURCE), 2).getQuads(null, null, 0))
                    .build();
        });
    }


    @Override
    public void regEvents() {
        super.regEvents();
        MinecraftForge.EVENT_BUS.register(AbsoluteModelRegistry.INSTANCE);
    }

    @Override
    public void init() {
        super.init();
        //as with tile entitys probably wont make any speacil code for registering them
        ClientRegistry.bindTileEntitySpecialRenderer(MachineFrameTile.class, new MachineFrameRender());
    }

    @Override
    public void postInit() {
        super.postInit();

        Minecraft.getMinecraft().getItemColors().registerItemColorHandler(new MachineFrame.CoreColor() , AIncogData.MACHINE_FRAME.item);

        //TODO put these back
        Minecraft.getMinecraft().getItemColors().registerItemColorHandler(new PoweredWeapon.CoreColor() , AIncogData.LASER_SWORD);
        Minecraft.getMinecraft().getItemColors().registerItemColorHandler(new MultiTool.CoreColor() , AIncogData.MULTI_TOOL);
        Minecraft.getMinecraft().getItemColors().registerItemColorHandler(new MakeshiftCore.CoreColor(), AIncogData.MAKESHIFT_CORE);

        Minecraft.getMinecraft().getItemColors().registerItemColorHandler(AIncogData.WHITE_CRYSTAL.getColor(), AIncogData.WHITE_CRYSTAL);
        Minecraft.getMinecraft().getItemColors().registerItemColorHandler(AIncogData.RED_CRYSTAL.getColor(), AIncogData.RED_CRYSTAL);
        Minecraft.getMinecraft().getItemColors().registerItemColorHandler(AIncogData.GREEN_CRYSTAL.getColor(), AIncogData.GREEN_CRYSTAL);
        Minecraft.getMinecraft().getItemColors().registerItemColorHandler(AIncogData.BLUE_CRYSTAL.getColor(), AIncogData.BLUE_CRYSTAL);

        Minecraft.getMinecraft().getItemColors().registerItemColorHandler(AIncogData.WHITE_BASIC_CORE.getColor(), AIncogData.WHITE_BASIC_CORE);
        Minecraft.getMinecraft().getItemColors().registerItemColorHandler(AIncogData.RED_BASIC_CORE.getColor(), AIncogData.RED_BASIC_CORE);
        Minecraft.getMinecraft().getItemColors().registerItemColorHandler(AIncogData.GREEN_BASIC_CORE.getColor(), AIncogData.GREEN_BASIC_CORE);
        Minecraft.getMinecraft().getItemColors().registerItemColorHandler(AIncogData.BLUE_BASIC_CORE.getColor(), AIncogData.BLUE_BASIC_CORE);

        CrystalOre.OreColor color = AIncogData.WHITE_CRYSTAL_ORE.getOreColor();
        Minecraft.getMinecraft().getItemColors().registerItemColorHandler(color, AIncogData.WHITE_CRYSTAL_ORE.item);
        Minecraft.getMinecraft().getBlockColors().registerBlockColorHandler(color, AIncogData.WHITE_CRYSTAL_ORE);

        color = AIncogData.RED_CRYSTAL_ORE.getOreColor();
        Minecraft.getMinecraft().getItemColors().registerItemColorHandler(color, AIncogData.RED_CRYSTAL_ORE.item);
        Minecraft.getMinecraft().getBlockColors().registerBlockColorHandler(color, AIncogData.RED_CRYSTAL_ORE);

        color = AIncogData.GREEN_CRYSTAL_ORE.getOreColor();
        Minecraft.getMinecraft().getItemColors().registerItemColorHandler(color, AIncogData.GREEN_CRYSTAL_ORE.item);
        Minecraft.getMinecraft().getBlockColors().registerBlockColorHandler(color, AIncogData.GREEN_CRYSTAL_ORE);

        color = AIncogData.BLUE_CRYSTAL_ORE.getOreColor();
        Minecraft.getMinecraft().getItemColors().registerItemColorHandler(color, AIncogData.BLUE_CRYSTAL_ORE.item);
        Minecraft.getMinecraft().getBlockColors().registerBlockColorHandler(color, AIncogData.BLUE_CRYSTAL_ORE);
    }

    @Nonnull
    @Override
    public ClientProxy getClientProxy() {
        return this;
    }

    @Nonnull
    @Override
    public ServerProxy getServerProxy() {
        throw new RuntimeException("Client proxy asked for server proxy..... which cant happen");
    }
}
