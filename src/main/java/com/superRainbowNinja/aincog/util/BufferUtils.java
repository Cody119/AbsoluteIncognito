package com.superRainbowNinja.aincog.util;

import io.netty.buffer.ByteBuf;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.network.ByteBufUtils;

/**
 * Created by SuperRainbowNinja on 25/12/2016.
 */
public class BufferUtils {

    public static void writeInventory(ByteBuf buf, IInventory inv) {
        writeInventory(buf, inv, true);
    }

    public static void writeInventory(ByteBuf buf, IInventory inv, boolean writeLength) {
        int size = inv.getSizeInventory();
        if (writeLength) buf.writeInt(size);
        for (int i = 0; i < size; i++) {
            ItemStack stack = inv.getStackInSlot(i);
            if (stack != null) {
                buf.writeInt(i);
                ByteBufUtils.writeItemStack(buf, stack);
            }
        }
        //end of inv entrys
        buf.writeInt(-1);
    }

    public static int readInvLength(ByteBuf buf) {
        return buf.readInt();
    }

    public static void readInvItems(ByteBuf buf, IInventory inv) {
        int i = 0;
        int size = inv.getSizeInventory();
        while ((i = buf.readInt()) != -1) {
            ItemStack stack = ByteBufUtils.readItemStack(buf);
            if (i >= 0 && i < size) {
                inv.setInventorySlotContents(i, stack);
            }
        }
    }

    public static void writeFluid(ByteBuf buf, FluidStack stack) {
        if (stack == null) {
            ByteBufUtils.writeUTF8String(buf, "");
            return;
        }
        ByteBufUtils.writeUTF8String(buf, stack.getFluid().getName());
        buf.writeInt(stack.amount);
        ByteBufUtils.writeTag(buf, stack.tag);
    }

    public static FluidStack readFluid(ByteBuf buf) {
        Fluid fluid = FluidRegistry.getFluid(ByteBufUtils.readUTF8String(buf));
        if (fluid != null) {
            return new FluidStack(fluid, buf.readInt(), ByteBufUtils.readTag(buf));
        }
        return null;
    }
}
