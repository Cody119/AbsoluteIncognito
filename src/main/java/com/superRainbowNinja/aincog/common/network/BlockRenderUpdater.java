package com.superRainbowNinja.aincog.common.network;

import com.superRainbowNinja.aincog.common.tileEntity.MachineFrameTile;
import com.superRainbowNinja.aincog.util.LogHelper;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Created by SuperRainbowNinja on 11/11/2016.
 *
 * Packet sent to the client to update
 * Should only have commmon code
 * TODO make more generic, dont make it store tile entity?
 */
public class BlockRenderUpdater extends TileEntityPacket {

    public Object[] getData() {
        return data;
    }

    private Object[] data;
    private MachineFrameTile tile;

    public BlockPos getPos() {
        return pos;
    }


    public BlockRenderUpdater(MachineFrameTile tileIn) {
        super(tileIn.getPos());
        data = null;
        valid = true;
        tile = tileIn;
    }
    //used by mc forge on the receiving end of the msg
    public BlockRenderUpdater() {
        super();
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        super.fromBytes(buf);
        data = (Object[])MachineFrameTile.DATA_HANDLE.read(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        super.toBytes(buf);
        MachineFrameTile.DATA_HANDLE.write(buf, tile);
    }

    public static class Handle implements IMessageHandler<BlockRenderUpdater, IMessage>{

        @Override
        public IMessage onMessage(final BlockRenderUpdater message, MessageContext ctx) {
            if (ctx.side != Side.CLIENT) {
                LogHelper.errorLog("Block render update msg recived on wrong side");
                return null;
            }
            if (!message.isValid()) {
                LogHelper.errorLog("Block render update msg was invalid" + message.toString());
                return null;
            }

            final Minecraft mc = Minecraft.getMinecraft();
            mc.addScheduledTask(() -> {
                TileEntity te = mc.theWorld.getTileEntity(message.getPos());
                if (te instanceof MachineFrameTile) {
                    //((IMachineInfoProvider) te).updateMachine(message.getData());
                    MachineFrameTile.DATA_HANDLE.readFromCache(message.getData(), ((MachineFrameTile) te));
                } else {
                    LogHelper.errorLog("Server sent AI render data at " + message.pos + " but no AI tile entity exists");
                }
            });
            return null;
        }
    }
}
