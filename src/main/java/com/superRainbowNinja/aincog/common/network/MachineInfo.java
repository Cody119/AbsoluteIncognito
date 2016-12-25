package com.superRainbowNinja.aincog.common.network;

import com.superRainbowNinja.aincog.common.machineLogic.IMachineLogic;
import com.superRainbowNinja.aincog.common.machineLogic.MachineLogicRegistry;
import com.superRainbowNinja.aincog.common.tileEntity.MachineFrameTile;
import com.superRainbowNinja.aincog.util.ExactPosition;
import com.superRainbowNinja.aincog.util.NBTUtils;
import com.superRainbowNinja.aincog.util.Operation;
import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;

import javax.annotation.Nullable;
import java.util.ArrayList;

/**
 * Created by SuperRainbowNinja on 12/11/2016.
 *
 * Used to save machine data and serialize it
 */
public class MachineInfo {
    //NBT keys
    public static final String KEY_INVENTORY = "INVENTORY";
    public static final String KEY_COMP_ITEMS = "COMP_ITEMS";
    public static final String KEY_COMP_POS = "COMP_POS";
    public static final String KEY_ENERGY = "ENERGY";
    public static final String KEY_LOCKED = "LOCKED";
    public static final String KEY_LOGIC = "LOGIC";
    public static final String KEY_LOGIC_NAME = "LOGIC_NAME";
    public static final String KEY_CORE = "CORE";
    public static final String KEY_BATTERY_BEHAVE = "BATTERY_BEHAVE";
    public static final String KEY_CUR_OP = "CUR_OP";
    public static final String KEY_CORE_ANGLE = "CORE_ANGLE";
    public static final String KEY_CORE_SPEED = "CORE_SPEED";

    static final ArrayList EMPTY_LIST = new ArrayList(0);
    public static final ItemStack[] EMPTY_ITEM_STACKS = new ItemStack[0];

    //give everything defualt values as it is a capability
    public ItemStack[] inv;
    public ArrayList<ItemStack> compItems;
    public ArrayList<ExactPosition> compPos;
    public int energy;
    public boolean locked;
    public IMachineLogic logic;
    public ItemStack core;
    public MachineFrameTile.BatteryBehaviour batteryBehaviour = MachineFrameTile.BatteryBehaviour.PROVIDE_ACCEPT;
    //core details
    public Operation curOp = Operation.NOP;
    public int coreAngle;
    public int coreSpeed;
    public long curTime;

    public MachineInfo(ByteBuf buf) {
        fromBytes(buf);
        curTime = 0;
    };

    public MachineInfo() {}

    public MachineInfo(NBTTagCompound compound) {
        readNBT(compound);
    }

    public void addInv(ItemStack[] invIn) {
        inv = invIn;
    }

    public void addComponents(ArrayList<ItemStack> items, ArrayList<ExactPosition> positions) {
        compItems = items;
        compPos = positions;
    }

    public void addEnergy(int energyIn) {
        energy = energyIn;
    }

    public void addLogic(@Nullable IMachineLogic logicIn) {
        logic = logicIn;
        locked = logicIn != null;
    }

    public void addCore(ItemStack stackIn) {
        core = stackIn;
    }

    public void addBatteryBehaviour(MachineFrameTile.BatteryBehaviour b) {
        batteryBehaviour = b;
    }

    public void addCurrentOperation(Operation op) {
        curOp = op;
    }

    public void addCurrentTime(long time) {
        curTime = time;
    }

    public void addCoreAngle(int angle) {
        coreAngle = angle;
    }

    public void addCoreSpeed(int speed) {
        coreSpeed = speed;
    }

    public void fromBytes(ByteBuf buf) {
        int len = buf.readInt();
        compItems = new ArrayList<>(8);
        compPos = new ArrayList<>(8);

        for (int i = 0; i < len; i++) {
            compItems.add(ByteBufUtils.readItemStack(buf));
        }

        for (int i = 0; i < len; i++) {
            compPos.add(new ExactPosition(buf));
        }

        inv = new ItemStack[buf.readInt()];
        int j;
        while ((j = buf.readInt()) >= 0) {
            if (j < inv.length) {
                inv[j] = ByteBufUtils.readItemStack(buf);
            }
        }

        energy = buf.readInt();

        if (locked = buf.readBoolean()) {
            logic = MachineLogicRegistry.INSTANCE.deserializeLogic(buf);
        }
        core = ByteBufUtils.readItemStack(buf);
        batteryBehaviour = MachineFrameTile.BatteryBehaviour.get(buf.readInt());
        curOp = Operation.values()[buf.readInt()];
        coreAngle = buf.readInt();
        coreSpeed = buf.readInt();
        curTime = buf.readLong();
    }

    public void toBytes(ByteBuf buf) {
        buf.writeInt(compItems.size());
        for (ItemStack item : compItems) {
            ByteBufUtils.writeItemStack(buf, item);
        }
        for (ExactPosition pos : compPos) {
            pos.toBytes(buf);
        }
        buf.writeInt(inv.length);
        for (int i = 0; i < inv.length; i++) {
            if (inv[i] != null) {
                buf.writeInt(i);
                ByteBufUtils.writeItemStack(buf, inv[i]);
            }
        }
        buf.writeInt(-1); //end the inv list

        buf.writeInt(energy);

        buf.writeBoolean(locked);
        if (locked) {
            MachineLogicRegistry.serializeLogic(logic, buf);
        }
        ByteBufUtils.writeItemStack(buf, core);
        buf.writeInt(batteryBehaviour.ordinal());
        buf.writeInt(curOp.ordinal());
        buf.writeInt(-1); //core angle, see MachineFrameTile.updateMachine for more info on y
        buf.writeInt(coreSpeed);
        buf.writeLong(curTime);

        if (core == null && curOp != Operation.NOP) {
            System.out.println("sent invalid state");
        }
    }

    public NBTTagCompound writeNBT(NBTTagCompound compound) {
        NBTUtils.writeInventoryArray(inv, compound, KEY_INVENTORY);
        NBTUtils.writeInventory(compItems, compound, KEY_COMP_ITEMS);
        NBTUtils.writePositions(compPos, compound, KEY_COMP_POS);

        compound.setInteger(KEY_ENERGY, energy);
        compound.setBoolean(KEY_LOCKED, locked);

        if (locked) {
            compound.setString(KEY_LOGIC_NAME, logic.getName());
            NBTUtils.writeObject(compound, KEY_LOGIC, logic::writeToNBT);
        }

        if (core != null) {
            compound.setTag(KEY_CORE, core.writeToNBT(new NBTTagCompound()));
        }
        compound.setInteger(KEY_BATTERY_BEHAVE, batteryBehaviour.ordinal());
        compound.setInteger(KEY_CUR_OP, curOp.ordinal());
        compound.setInteger(KEY_CORE_ANGLE, coreAngle);
        compound.setInteger(KEY_CORE_SPEED, coreSpeed);
        return compound;
    }

    public void readNBT(NBTTagCompound compound) {
        inv = NBTUtils.readInventoryArray(compound, KEY_INVENTORY);
        compItems = NBTUtils.readInventory(compound, KEY_COMP_ITEMS);
        compPos = NBTUtils.readPositions(compound, KEY_COMP_POS);

        energy = compound.getInteger(KEY_ENERGY);
        logic = null;
        if (locked = compound.getBoolean(KEY_LOCKED)) {
            logic = MachineLogicRegistry.INSTANCE.getLogic(compound.getString(KEY_LOGIC_NAME));
            if (logic != null) {
                NBTUtils.readObject(compound, KEY_LOGIC, (nbt) -> logic.readFromNBT(nbt));
            }
        }
        batteryBehaviour = MachineFrameTile.BatteryBehaviour.get(compound.getInteger(KEY_BATTERY_BEHAVE));
        curOp = Operation.values()[compound.getInteger(KEY_CUR_OP)];
        core = ItemStack.loadItemStackFromNBT(compound.getCompoundTag(KEY_CORE));
        coreAngle = compound.getInteger(KEY_CORE_ANGLE);
        coreSpeed = compound.getInteger(KEY_CORE_SPEED);
    }
}
