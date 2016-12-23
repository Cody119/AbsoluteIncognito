package com.superRainbowNinja.aincog.common.network;

import com.superRainbowNinja.aincog.util.LogHelper;
import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Created by SuperRainbowNinja on 5/12/2016.
 */
public abstract class PacketBase implements IMessage {
    protected boolean valid;

    public PacketBase() {
        valid = false;
    }

    public boolean isValid() {
        return valid;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        valid = true;
    }
}
