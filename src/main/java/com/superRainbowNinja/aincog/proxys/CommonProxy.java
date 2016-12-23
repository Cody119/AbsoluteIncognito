package com.superRainbowNinja.aincog.proxys;

import com.superRainbowNinja.aincog.common.IRegistryEntry;
import com.superRainbowNinja.aincog.common.tileEntity.MachineFrameTile;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.ArrayList;

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

    public void initRegistryEntry() {
        registryEntries.forEach(IRegistryEntry::registerObjects);
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
}
