package com.superRainbowNinja.aincog.common.network;

import com.superRainbowNinja.aincog.common.tileEntity.IOperationTile;
import com.superRainbowNinja.aincog.util.LogHelper;
import com.superRainbowNinja.aincog.util.Operation;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Created by SuperRainbowNinja on 4/12/2016.
 *
 * unused
 */
public class ChangeOperation implements IMessage {

    private BlockPos pos;

    public long tick;
    public Operation operation;

    public ChangeOperation() {}

    public ChangeOperation(BlockPos posIn, Operation op, long startTick) {
        tick = startTick;
        operation = op;
        pos = posIn;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        operation = Operation.values()[buf.readInt()];
        tick = buf.readLong();
        pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(operation.ordinal());
        buf.writeLong(tick);
        buf.writeInt(pos.getX());
        buf.writeInt(pos.getY());
        buf.writeInt(pos.getZ());
    }

    public static class Handle implements IMessageHandler<ChangeOperation, IMessage> {

        @Override
        public IMessage onMessage(ChangeOperation message, MessageContext ctx) {
            if (ctx.side != Side.CLIENT) {
                LogHelper.errorLog("Block render update msg recived on wrong side");
                return null;
            }
            /*
            if (!message.isValid()) {
                LogHelper.errorLog("Block render update msg was invalid" + message.toString());
                return null;
            }
            */
            final Minecraft mc = Minecraft.getMinecraft();
            mc.addScheduledTask(() -> {
                TileEntity te = mc.theWorld.getTileEntity(message.pos);
                if (te instanceof IOperationTile) {
                    ((IOperationTile) te).changeTask(message.operation, message.tick);
                } else {
                    LogHelper.errorLog("Server sent AI render info at " + message.pos + " but no AI tile entity exists");
                }
            });
            return null;
        }
    }
}
