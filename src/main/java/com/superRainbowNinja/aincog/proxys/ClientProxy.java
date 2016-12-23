package com.superRainbowNinja.aincog.proxys;

import com.superRainbowNinja.aincog.AIncogData;
import com.superRainbowNinja.aincog.client.models.LaserSwordModel;
import com.superRainbowNinja.aincog.client.models.SmartModel;
import com.superRainbowNinja.aincog.client.models.tileEntityRenders.MachineFrameRender;
import com.superRainbowNinja.aincog.common.IRegistryEntry;
import com.superRainbowNinja.aincog.common.blocks.CrystalOre;
import com.superRainbowNinja.aincog.common.blocks.MachineFrame;
import com.superRainbowNinja.aincog.client.models.AbsoluteModelRegistry;
import com.superRainbowNinja.aincog.common.items.MakeshiftCore;
import com.superRainbowNinja.aincog.common.items.PoweredWeapon;
import com.superRainbowNinja.aincog.common.tileEntity.MachineFrameTile;
import com.superRainbowNinja.aincog.util.QuadUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraftforge.client.model.IPerspectiveAwareModel;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;

import static com.superRainbowNinja.aincog.common.items.PoweredWeapon.BLADE;
import static com.superRainbowNinja.aincog.common.items.PoweredWeapon.CORE;
import static com.superRainbowNinja.aincog.common.items.PoweredWeapon.HANDLE;

/**
 * Created by SuperRainbowNinja on 3/10/2016.
 */
public class ClientProxy extends CommonProxy {
    public static final CrystalOre.ModelBuilder CRYSTAL_ORE_MODELS = new CrystalOre.ModelBuilder();

    @Override
    public void initRegistryEntry() {
        super.initRegistryEntry();
        //its important that this is registered before the actual ores
        AbsoluteModelRegistry.INSTANCE.registerModelEntry(CRYSTAL_ORE_MODELS);
        registryEntries.forEach(IRegistryEntry::registerModels);

        //this loads on the server side for some reason, as far as ik it shouldnt so i muct be missing something?
        //will fix later, for now so long as its called dosent really matter where it is
        AbsoluteModelRegistry.INSTANCE.registerModelModelBakeEventSub(HANDLE, (event) -> {
            IBakedModel existingModel = event.getModelRegistry().getObject(HANDLE);
            //Sword model setup
            event.getModelRegistry().putObject(HANDLE, new SmartModel((IPerspectiveAwareModel)existingModel,
                    new LaserSwordModel(
                            QuadUtil.addItemTint(existingModel, -1),//gotta remove the index already added
                            QuadUtil.addItemTint(event.getModelRegistry().getObject(BLADE), 2),
                            QuadUtil.addItemTint(event.getModelRegistry().getObject(CORE), 1)
                    )
            ));
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
        Minecraft.getMinecraft().getItemColors().registerItemColorHandler(new PoweredWeapon.CoreColor() , AIncogData.LASER_SWORD);
        Minecraft.getMinecraft().getItemColors().registerItemColorHandler(new MakeshiftCore.CoreColor(), AIncogData.MAKESHIFT_CORE);

        CrystalOre.OreColor color = AIncogData.RED_CRYSTAL_ORE.getOreColor();
        Minecraft.getMinecraft().getItemColors().registerItemColorHandler(color, AIncogData.RED_CRYSTAL_ORE.item);
        Minecraft.getMinecraft().getBlockColors().registerBlockColorHandler(color, AIncogData.RED_CRYSTAL_ORE);

        color = AIncogData.GREEN_CRYSTAL_ORE.getOreColor();
        Minecraft.getMinecraft().getItemColors().registerItemColorHandler(color, AIncogData.GREEN_CRYSTAL_ORE.item);
        Minecraft.getMinecraft().getBlockColors().registerBlockColorHandler(color, AIncogData.GREEN_CRYSTAL_ORE);

        color = AIncogData.BLUE_CRYSTAL_ORE.getOreColor();
        Minecraft.getMinecraft().getItemColors().registerItemColorHandler(color, AIncogData.BLUE_CRYSTAL_ORE.item);
        Minecraft.getMinecraft().getBlockColors().registerBlockColorHandler(color, AIncogData.BLUE_CRYSTAL_ORE);
    }
}
