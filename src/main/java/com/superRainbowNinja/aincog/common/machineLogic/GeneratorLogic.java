package com.superRainbowNinja.aincog.common.machineLogic;

import cofh.api.energy.IEnergyStorage;
import com.superRainbowNinja.aincog.AIncogData;
import com.superRainbowNinja.aincog.client.models.tileEntityRenders.MachineFrameRender;
import com.superRainbowNinja.aincog.common.items.ICore;
import com.superRainbowNinja.aincog.common.items.IMachineComponent;
import com.superRainbowNinja.aincog.common.tileEntity.MachineFrameTile;
import com.superRainbowNinja.aincog.util.InvUtil;
import com.superRainbowNinja.aincog.util.Operation;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;

import javax.annotation.Nullable;

/**
 * Created by SuperRainbowNinja on 3/12/2016.
 *
 */
public class GeneratorLogic implements IMachineLogic {
    private static final String NAME = "generator_logic";
    public static final int RF_PER_TICK = 80;
    public static final int RF_PER_REDSTONE = RF_PER_TICK*240;
    private int rfMiniBuf = 0;

    private int getRfPerTick(MachineFrameTile tile) {
        return (int) (RF_PER_TICK * tile.getCore().getSpeed(tile.getCoreStack()));
    }

    private int getRfPerRedstone(MachineFrameTile tile) {
        return (int) (RF_PER_REDSTONE * tile.getCore().getEfficiency(tile.getCoreStack()));
    }

    @Override
    public void tick(MachineFrameTile tile) {
        if (!tile.getWorld().isRemote) {
            int rfPerTick = getRfPerTick(tile);
            IEnergyStorage storage =  tile.getEnergy();
            //if the mini buf dosent have enough for one tick and the internal buf has enough for another redstone
            if (rfMiniBuf < rfPerTick && (rfPerTick + rfMiniBuf < (storage.getMaxEnergyStored() - storage.getEnergyStored()))) {
                ItemStack stack = tile.getStackInSlot(0);
                if (stack != null) {
                    stack.stackSize--;
                    rfMiniBuf += getRfPerRedstone(tile);
                    if (stack.stackSize == 0) {
                        tile.setInventorySlotContents(0, null);
                    }
                    if (tile.getCurOp() != Operation.START) {
                        tile.startOp();
                    } else {
                        tile.markVisualDirty();
                    }
                    //ran out of redstone
                } else if (tile.getCurOp() == Operation.START) {
                    tile.stopOp();
                }
            }

            if (rfMiniBuf != 0){
                rfMiniBuf -= storage.receiveEnergy(Math.min(rfMiniBuf, rfPerTick), false);
                tile.markRfUpdate();
                if (storage.getMaxEnergyStored() == storage.getEnergyStored()) {
                    tile.stopOp();
                } else if (tile.getCurOp() != Operation.START) {
                    tile.startOp();
                }
                tile.damageCore(1);
            }
        }


        /*
        if (!tile.getWorld().isRemote) {
            boolean update = false;
            if (rfMiniBuf == 0) {
                ItemStack stack = tile.getStackInSlot(0);
                if (stack != null) {
                    stack.stackSize--;
                    rfMiniBuf = RF_PER_REDSTONE;
                    if (stack.stackSize == 0) {
                        tile.setInventorySlotContents(0, null);
                        update = true;
                    }
                    if (tile.getCurOp() != Operation.START) {
                        tile.startOp();
                    }
                } else if (tile.getCurOp() == Operation.START) {
                    tile.stopOp();
                }
            }
            if (rfMiniBuf != 0){
                if (tile.getEnergy().receiveEnergy(RF_PER_TICK, true) == RF_PER_TICK) {
                    tile.getEnergy().receiveEnergy(RF_PER_TICK, false);
                    rfMiniBuf -= RF_PER_TICK;
                    if (!update) {
                        tile.markRfUpdate();
                    }
                }
            }
            if (update) {
                tile.markVisualDirty();
            }
        }
        */
    }

    @Override
    public void initMachine(MachineFrameTile tile) {
        tile.setBatteryBehaviour(MachineFrameTile.BatteryBehaviour.PROVIDE);
        tile.resizeInv(1);
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void insertItem(MachineFrameTile te, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (InvUtil.insertIntoSlotFromPlayer(playerIn, te, 0, heldItem, (i) -> Items.REDSTONE == i))
            te.markVisualDirty();
    }

    @Override
    public void removeItem(MachineFrameTile te, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
        ItemStack stack = te.getStackInSlot(0);
        if (stack != null) {
            playerIn.inventory.setInventorySlotContents(playerIn.inventory.currentItem, stack);
            te.setInventorySlotContents(0, null);
            te.markVisualDirty();
        }
    }

    @Override
    public void renderTileEntityAt(MachineFrameRender r, MachineFrameTile teIn, double x, double y, double z, float partialTicks, int destroyStage) {
        ItemStack stack = teIn.getStackInSlot(0);
        if (stack != null) {
            GlStateManager.pushMatrix();
            GlStateManager.translate(x + 0.5, y + 0.5, z + 0.5);
            GlStateManager.scale(1f / 4, 1f / 4, 1f / 4);

            final float val = 360/100f;
            float d0 = (float) (
                    (teIn.getWorld().getWorldTime() % 100) * val
                    + ((double) partialTicks) * val
            );
            GlStateManager.rotate(d0, 0, 1, 0);
            Minecraft.getMinecraft().getRenderItem().renderItem(stack, ItemCameraTransforms.TransformType.NONE);
            GlStateManager.popMatrix();
        }
    }

    @Override
    public void spawnParticles(MachineFrameTile tile, WorldServer worldServer, BlockPos pos) {
        if (tile.getCurOp() != Operation.NOP) {
            worldServer.spawnParticle(EnumParticleTypes.REDSTONE, pos.getX()+0.5, pos.getY()+0.5, pos.getZ()+0.5, 10, 0.05, 0.05, 0.05, 0.0D);//, Item.getIdFromItem(AIncogData.MAKESHIFT_CORE));
        }
    }

    @Override
    public void coreRemoved(MachineFrameTile teIn) {
        rfMiniBuf = 0; //the operation will be auto wipped, we just need 2 worry about internals of this class
    }

    @Override
    public void serialize(ByteBuf buf) {
        buf.writeInt(rfMiniBuf);
    }

    @Override
    public void deserialize(ByteBuf buf) {
        rfMiniBuf = buf.readInt();
    }

    @Override
    public IMachineLogic readFromNBT(NBTTagCompound compound) {
        rfMiniBuf = compound.getInteger("BUF");
        return this;
    }

    @Nullable
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setInteger("BUF", rfMiniBuf);
        return compound;
    }

    public static final int[] SLOTS = new int[]{0};

    @Override
    public int[] getSlotsForFace(EnumFacing side) {
        return SLOTS;
    }

    @Override
    public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
        return itemStackIn.getItem() == Items.REDSTONE;
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
        return true;
    }

    public static final class Provider implements IMachineLogicProvider {

        @Override
        public IMachineLogic validMachine(MachineFrameTile tile) {
            int coilCount = 0;
            for (IMachineComponent component : tile.getComponents()) {
                if (component instanceof AIncogData.Coil) {
                    coilCount++;
                }
            }
            if (coilCount == 2) {
                return new GeneratorLogic();
            }
            return null;
        }

        @Override
        public String[] getLogics() {
            return new String[]{GeneratorLogic.NAME};
        }

        @Override
        public IMachineLogic deserializeLogic(String name, ByteBuf buf) {
            GeneratorLogic logic = new GeneratorLogic();
            logic.deserialize(buf);
            return logic;
        }

        @Override
        public IMachineLogic getLogicByName(String name) {
            return new GeneratorLogic();
        }
    }
}
