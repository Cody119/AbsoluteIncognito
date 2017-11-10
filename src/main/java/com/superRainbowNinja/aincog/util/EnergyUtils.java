package com.superRainbowNinja.aincog.util;

import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyConnection;
import cofh.api.energy.IEnergyReceiver;
import cofh.api.energy.IEnergyStorage;
import com.superRainbowNinja.aincog.common.blocks.MachineFrame;
import com.superRainbowNinja.aincog.common.tileEntity.MachineFrameTile;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * Created by SuperRainbowNinja on 4/12/2016.
 */
public class EnergyUtils {

    private EnergyUtils() {}
/*
    public static boolean sendEnergyToSides(World world, BlockPos pos, IEnergyStorage storage, int maxSend, boolean[] ignoreSides) {
        boolean sentE = false;
        int rfToSend = Math.min(maxSend, storage.getEnergyStored());
        for (EnumFacing face : EnumFacing.values()) {
            if (ignoreSides != null && ignoreSides[face.getIndex()]) {
                continue;
            }
            EnumFacing opposite = face.getOpposite();
            TileEntity te = world.getTileEntity(pos.offset(face));
            if (te != null && te instanceof IEnergyConnection && ((IEnergyConnection) te).canConnectEnergy(opposite) && te instanceof IEnergyReceiver) {
                IEnergyReceiver rec = ((IEnergyReceiver) te);
                int sent = rec.receiveEnergy(opposite, rfToSend, false);
                if (sent != 0) {
                    storage.extractEnergy(sent, false);
                    sentE = true;
                    rfToSend -= sent;
                    if (rfToSend == 0) {
                        break;
                    }
                }
            }
        }
        return sentE;
    }
*/
    public static boolean sendEnergyToSides(World world, BlockPos pos, IEnergyStorage storage, int maxSend) {
        if (storage.getEnergyStored() == 0) {
            return false;
        }

        boolean sentE = false;
        int rfToSend = Math.min(maxSend, storage.getEnergyStored());
        PriorityQueue<MachineFrameTile> p = new PriorityQueue<>(6,
                Comparator.comparingInt(
                        (one) -> one.getEnergyStored(null)
                )
        );

        for (EnumFacing face : EnumFacing.values()) {
            EnumFacing opposite = face.getOpposite();
            TileEntity te = world.getTileEntity(pos.offset(face));
            if (te != null && te instanceof IEnergyConnection && ((IEnergyConnection) te).canConnectEnergy(opposite) && te instanceof IEnergyReceiver) {
                IEnergyReceiver rec = ((IEnergyReceiver) te);

                if (te instanceof MachineFrameTile) {
                    p.add((MachineFrameTile) te);
                    continue;
                }

                int sent = rec.receiveEnergy(opposite, rfToSend, false);
                if (sent != 0) {
                    storage.extractEnergy(sent, false);
                    sentE = true;
                    rfToSend -= sent;
                    if (rfToSend == 0) {
                        break;
                    }
                }
            }
        }

        for (MachineFrameTile te : p) {
            int dif = storage.getEnergyStored() - te.getEnergyStored(null);
            if (dif > 0) {
                //TODO calculate energy to send by percentage instead?
                int send = Math.min(rfToSend, dif/2);
                int sent = te.receiveEnergy(null, send, false);
                if (sent != 0) {
                    storage.extractEnergy(sent, false);
                    sentE = true;
                    rfToSend -= sent;
                    if (rfToSend == 0) {
                        break;
                    }
                }
            }
        }

        return sentE;
    }

}
