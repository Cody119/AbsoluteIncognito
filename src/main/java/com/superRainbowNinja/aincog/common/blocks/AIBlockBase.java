package com.superRainbowNinja.aincog.common.blocks;

import com.superRainbowNinja.aincog.AIncogData;
import com.superRainbowNinja.aincog.common.IRegistryEntry;
import com.superRainbowNinja.aincog.util.LogHelper;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by SuperRainbowNinja on 3/10/2016.
 *
 * Base for most blocks in this mod, pretty strait forward
 */
public class AIBlockBase extends Block implements IRegistryEntry{
    public final String name;
    public final ItemBlock item;

    public AIBlockBase(String name) {
        this(name, Material.IRON);
    }

    public AIBlockBase(String inName, Material material) {
        super(material);
        name = inName;
        setCreativeTab(AIncogData.A_INCOG_C_Tab);
        setRegistryName(name);
        setUnlocalizedName(getRegistryName().toString());
        item = buildItemBlock();
    }

    protected ItemBlock buildItemBlock() {

        ItemBlock itemBlock = (ItemBlock) new ItemBlock(this).setRegistryName(this.name);
        LogHelper.infoLog("Made item block: " + itemBlock.getUnlocalizedName());
        return itemBlock;
    }

    @Override
    public void registerBlocks(RegistryEvent.Register<Block> event) {
        event.getRegistry().register(this);
    }

    @Override
    public void registerItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().register(this.item);
    }

    @Override
    public void registerModels() {
        ModelLoader.setCustomModelResourceLocation(this.item, 0,
                new ModelResourceLocation(this.getRegistryName().toString(), "inventory"));
    }
}
