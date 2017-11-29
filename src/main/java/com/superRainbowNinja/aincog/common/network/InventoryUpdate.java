package com.superRainbowNinja.aincog.common.network;

import com.superRainbowNinja.aincog.common.capabilites.IPoweredWeaponCap;
import com.superRainbowNinja.aincog.common.capabilites.PoweredWeaponCapImp;
import com.superRainbowNinja.aincog.util.LogHelper;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

import javax.swing.text.html.parser.Entity;

/**
 * Created by SuperRainbowNinja on 11/12/2016.
 */
public class InventoryUpdate extends PacketBase{
    public ItemStack stack;
    public int pos;

    public InventoryUpdate() {
        super();
    }

    public InventoryUpdate(ItemStack stackIn, int posIn) {
        pos = posIn;
        stack = stackIn;
        valid = true;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeItemStack(buf, stack);
    //    ByteBufUtils.writeTag(buf, ((PoweredWeaponCapImp) IPoweredWeaponCap.getCap(stack)).serializeNBT());
        buf.writeInt(pos);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        stack = ByteBufUtils.readItemStack(buf);
  //      ((PoweredWeaponCapImp) IPoweredWeaponCap.getCap(stack)).deserializeNBT(ByteBufUtils.readTag(buf));
        pos = buf.readInt();
        valid = true;
    }

    public static class Handle implements IMessageHandler<InventoryUpdate, IMessage> {

        @Override
        public IMessage onMessage(final InventoryUpdate message, MessageContext ctx) {
            if (ctx.side != Side.CLIENT) {
                LogHelper.errorLog("Player update msg recived on wrong side");
                return null;
            }
            if (!message.isValid()) {
                LogHelper.errorLog("Player update msg was invalid" + message.toString());
                return null;
            }

            final Minecraft mc = Minecraft.getMinecraft();
            mc.addScheduledTask(() -> {
                LogHelper.infoLog("Packet recived " + mc.thePlayer.getName());
                mc.thePlayer.inventory.setInventorySlotContents(message.pos, message.stack);
            });
            return null;
        }
    }
}
