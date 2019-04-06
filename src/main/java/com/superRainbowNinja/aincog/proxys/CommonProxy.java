package com.superRainbowNinja.aincog.proxys;

import com.superRainbowNinja.aincog.common.IRegistryEntry;
import com.superRainbowNinja.aincog.common.tileEntity.MachineFrameTile;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.function.BiConsumer;

/**
 * Created by SuperRainbowNinja on 3/10/2016.
 */
public abstract class CommonProxy {
    protected static ArrayList<IRegistryEntry> registryEntries  = new ArrayList<>();

    public static <T extends IRegistryEntry> T add(T item) {
        if (registryEntries != null)
            registryEntries.add(item);
        return item;
    }

    public void preInit() {
        //registryEntries.forEach(IRegistryEntry::registerObjects);
    }

    protected  <T extends IForgeRegistryEntry<T>> void registerAll(RegistryEvent.Register<T> event, BiConsumer<IRegistryEntry, RegistryEvent.Register<T>> consumer) {
        for (IRegistryEntry entry : registryEntries) {
            consumer.accept(entry, event);
        }
    }

    public void registerBlocks(RegistryEvent.Register<Block> event) {
        registerAll(event, IRegistryEntry::registerBlocks);
    }

    public void registerItems(RegistryEvent.Register<Item> event) {
        registerAll(event, IRegistryEntry::registerItems);
    }

    public void initTEs() {
        //probalbly just register them all here, not really worth making a speacil registry or something
        GameRegistry.registerTileEntity(MachineFrameTile.class, "tile_machine_frame");
    }

    public void init() {}

    public void regEvents() {}

    /* should be the final thing called in the post init*/
    public static void clearCache() {
        //items = null;
        //blocks = null;
    }

    public void postInit() {
    }

    @Nonnull public abstract ClientProxy getClientProxy();
    @Nonnull public abstract ServerProxy getServerProxy();
}
