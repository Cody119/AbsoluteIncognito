package com.superRainbowNinja.aincog.common.network;

import com.superRainbowNinja.aincog.common.tileEntity.IRfUpdater;
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
 * Created by SuperRainbowNinja on 4/12/2016.
 */
public class RfUpdate extends TileEntityPacket {
    public int power;

    public RfUpdate() {
        super();
    }
    public RfUpdate(int i, BlockPos posIn) {
        super(posIn);
        power = i;
        valid = true;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        super.fromBytes(buf);
        power = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        super.toBytes(buf);
        buf.writeInt(power);
    }

    public static class Handle implements IMessageHandler<RfUpdate, IMessage> {

        @Override
        public IMessage onMessage(RfUpdate message, MessageContext ctx) {
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
                TileEntity te = mc.theWorld.getTileEntity(message.pos);
                if (te instanceof IRfUpdater) {
                    ((IRfUpdater) te).receiveUpdate(message.power);
                } else {
                    LogHelper.errorLog("Server sent AI rf info at " + message.pos + " but no AI tile entity exists");
                }
            });
            return null;
        }
    }
}
