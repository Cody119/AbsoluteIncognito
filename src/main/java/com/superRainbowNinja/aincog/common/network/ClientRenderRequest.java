package com.superRainbowNinja.aincog.common.network;

import com.superRainbowNinja.aincog.common.tileEntity.MachineFrameTile;
import com.superRainbowNinja.aincog.util.LogHelper;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Created by SuperRainbowNinja on 12/11/2016.
 *
 * ask the server for render info for te at given block cords
 */
public class ClientRenderRequest extends TileEntityPacket {

    public ClientRenderRequest(BlockPos posIn) {
        super(posIn);
        valid = true;
    }

    public ClientRenderRequest(TileEntity te) {
        super(te.getPos());
        valid = true;
    }

    //used by mc forge on the receiving end of the msg
    public ClientRenderRequest() {
        super();
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        super.fromBytes(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        super.toBytes(buf);
    }

    public static class Handle implements IMessageHandler<ClientRenderRequest, IMessage> {

        @Override
        public IMessage onMessage(final ClientRenderRequest message, MessageContext ctx) {
            if (ctx.side != Side.SERVER) {
                LogHelper.errorLog("Client render msg recived on wrong side");
                return null;
            }
            if (!message.isValid()) {
                LogHelper.errorLog("Client render msg was invalid" + message.toString());
                return null;
            }

            //cant instantly reply as we are on the network thread and hence cant access the TE data
            final EntityPlayerMP sendingPlayer = ctx.getServerHandler().player;
            if (sendingPlayer == null) {
                LogHelper.errorLog("EntityPlayerMP was null when Client reguest was received");
                return null;
            }

            final WorldServer playerWorldServer = sendingPlayer.getServerWorld();
            playerWorldServer.addScheduledTask(() -> {
                TileEntity te = playerWorldServer.getTileEntity(message.pos);
                if (te instanceof MachineFrameTile) {
                    PacketHandler.sendTo(sendingPlayer, new BlockRenderUpdater(((MachineFrameTile) te)));
                } else {
                    LogHelper.errorLog("Player requested AI render info at " + message.pos + " but no AI tile entity exists");
                }
            });
            return null;
        }
    }
}
