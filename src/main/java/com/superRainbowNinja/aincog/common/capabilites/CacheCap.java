package com.superRainbowNinja.aincog.common.capabilites;

import com.superRainbowNinja.aincog.util.LogHelper;
import net.minecraft.item.ItemStack;

/**
 * Created by SuperRainbowNinja on 27/11/2017.
 */
public abstract class CacheCap {
    private boolean isCached;
    protected ItemStack thisStack;

    public CacheCap() {
        isCached = false;
        thisStack = null;
        LogHelper.infoLog("CacheCap created without item: " + this.toString());
    }

    public CacheCap(ItemStack stackIn) {
        isCached = false;
        thisStack = stackIn;
    }

    public void cacheCheck() {
        if (!isCached) {
            cache();
            isCached = true;
        }
    }

    protected abstract void cache();
}
