package com.superRainbowNinja.aincog.common.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;

/**
 * Created by SuperRainbowNinja on 5/12/2016.
 */
public abstract class TileEntityPacket extends PacketBase {
    protected BlockPos pos;

    public TileEntityPacket(BlockPos posIn) {
        super();
        pos = posIn;
    }

    public TileEntityPacket() {
        super();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(pos.getX());
        buf.writeInt(pos.getY());
        buf.writeInt(pos.getZ());
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        super.fromBytes(buf);
        pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
    }
}
