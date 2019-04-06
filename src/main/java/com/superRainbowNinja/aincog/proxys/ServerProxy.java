package com.superRainbowNinja.aincog.proxys;


import javax.annotation.Nonnull;

/**
 * Created by SuperRainbowNinja on 3/10/2016.
 */
public class ServerProxy extends CommonProxy {
    @Nonnull
    @Override
    public ClientProxy getClientProxy() {
        throw new RuntimeException("Server proxy asked for client proxy..... which cant happen");
    }

    @Nonnull
    @Override
    public ServerProxy getServerProxy() {
        return this;
    }
}
