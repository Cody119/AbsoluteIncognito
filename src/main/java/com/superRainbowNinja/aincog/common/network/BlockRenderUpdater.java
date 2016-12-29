package com.superRainbowNinja.aincog.common.network;

import com.superRainbowNinja.aincog.common.tileEntity.IMachineInfoProvider;
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
 */
public class BlockRenderUpdater extends TileEntityPacket {

    public MachineInfo getInfo() {
        return info;
    }

    private MachineInfo info;

    public BlockPos getPos() {
        return pos;
    }


    public BlockRenderUpdater(MachineInfo infoIn, BlockPos posIn) {
        super(posIn);
        info = infoIn;
        valid = true;
    }
    //used by mc forge on the receiving end of the msg
    public BlockRenderUpdater() {
        super();
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        super.fromBytes(buf);
        info = new MachineInfo(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        super.toBytes(buf);
        info.toBytes(buf);
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
                if (te instanceof IMachineInfoProvider) {
                    //((IMachineInfoProvider) te).updateMachine(message.getInfo());
                    MachineFrameTile.dataHandle.readFromCache(message.getInfo().data, ((MachineFrameTile) te));
                } else {
                    LogHelper.errorLog("Server sent AI render info at " + message.pos + " but no AI tile entity exists");
                }
            });
            return null;
        }
    }
}
