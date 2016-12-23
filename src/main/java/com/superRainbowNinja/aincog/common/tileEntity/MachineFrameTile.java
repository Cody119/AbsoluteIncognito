package com.superRainbowNinja.aincog.common.tileEntity;

import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyHandler;
import cofh.api.energy.IEnergyProvider;
import cofh.api.energy.IEnergyReceiver;
import com.superRainbowNinja.aincog.AIncogData;
import com.superRainbowNinja.aincog.common.items.ICore;
import com.superRainbowNinja.aincog.common.items.IMachineComponent;
import com.superRainbowNinja.aincog.common.items.MachineLock;
import com.superRainbowNinja.aincog.common.machineLogic.IMachineLogic;
import com.superRainbowNinja.aincog.common.machineLogic.MachineLogicRegistry;
import com.superRainbowNinja.aincog.common.network.*;
import com.superRainbowNinja.aincog.util.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * Created by SuperRainbowNinja on 10/10/2016.
 *
 * Fluid handler has been changed 2 a capability
 * TODO core chat channel
 * TODO maybe make componets render slightly closer (on y-axis) when theres only 2 layers?
 */
public class MachineFrameTile extends TileEntity implements ITickable, ISidedInventory, IMachineInfoProvider, IEnergyHandler, IEnergyProvider, IEnergyReceiver, IRfUpdater {
    private static final int MAX_RF_SEND = 100;
    //core spinning
    private static final int ACCELERATION = 1;
    private static final int MAX_SPEED = 20;
    private ItemStack inv[];
    //these 3 are parallel
    private ArrayList<ItemStack> componentItems = new ArrayList<>(8);
    private ArrayList<IMachineComponent> components = new ArrayList<>(8);
    private ArrayList<ExactPosition> componentPositions = new ArrayList<>(8);

    private ItemStack coreStack;
    private ICore core;
    private EnergyStorage energy;
    private boolean locked;

    private Operation curOp;
    private int coreAngle;
    private int coreSpeed;

    private BatteryBehaviour batteryBehaviour;

    //Unsaved fields
    //this is undefined if locked is false (generally should be null in that scenario)
    private IMachineLogic logic;
    //should only be used on the server side, indicates if the machine received or sent rf this tick, if it did it cannot send rf this tick
    private boolean receivedOrSent; //if energy was recived or sent this tick
    //dosent need 2 be saved, used on server side to denote the need for a full render update
    private boolean vDirty = false;
    private boolean rfUpdate = false;

    public enum BatteryBehaviour {
        PROVIDE,
        ACCEPT,
        PROVIDE_ACCEPT;
        public NBTTagInt getTag() {return new NBTTagInt(this.ordinal());}
        public static BatteryBehaviour get(int id) {return values()[id];}
    }

    public MachineFrameTile() {
        this(10000);
    }

    public MachineFrameTile(int energyCapacityIn) {
        inv = new ItemStack[0];
        energy = new EnergyStorage(energyCapacityIn);
        locked = false;
        batteryBehaviour = BatteryBehaviour.PROVIDE_ACCEPT;
        curOp = Operation.NOP;
        coreAngle = 0;
        coreSpeed = 0;
    }

    /*----------------------------
    |  Accessors/Mutators        |
    ----------------------------*/

    public ArrayList<IMachineComponent> getComponents() {
        return components;
    }

    public ArrayList<ExactPosition> getComponentPositions() {
        return componentPositions;
    }

    public ArrayList<ItemStack> getComponentItems() {
        return componentItems;
    }

    public ItemStack getCoreStack() {
        return coreStack;
    }

    public EnergyStorage getEnergy() {
        return energy;
    }

    public boolean isLocked() {
        return locked;
    }

    public IMachineLogic getLogic() {
        return logic;
    }

    public BatteryBehaviour getBatteryBehaviour() {
        return batteryBehaviour;
    }
    public void setBatteryBehaviour(BatteryBehaviour batteryBehaviour) {
        this.batteryBehaviour = batteryBehaviour;
    }

    public float getEnergyRatio() {
        return ((float) energy.getEnergyStored())/energy.getMaxEnergyStored();
    }

    public boolean hasCore() {
        return coreStack != null;
    }
    //a machine may have a core that is "not functional" (like a rf powered core that has no rf left)
    public boolean coreIsFunctional() {
        return coreStack != null && core.coreIsFunctional(coreStack);
    }

    public Operation getCurOp() {
        return curOp;
    }

    public int getCoreAngle() {
        return coreAngle;
    }

    public int getCoreSpeed() {
        return coreSpeed;
    }

    public ICore getCore() {
        return core;
    }

    //Setters that can fail

    public boolean trySetCore(ItemStack core) {
        if (!hasCore() && core != null && core.getItem() instanceof ICore) {
            setCore(core);
            markVisualDirty();
            return true;
        }
        return false;
    }

    public boolean tryAddComponent(IMachineComponent component, ItemStack item, float x, float y, float z) {
        ExactPosition pos = ExactPosition.getPosFromCo2(x, y, z);
        //set 2 true if at least one component exists on this level
        boolean level = false;
        for (ExactPosition cPos : componentPositions) {
            if (cPos.equals(pos)) {
                return false;
            }
            if (cPos.layerCheck(pos)) {
                if (cPos.isWholeLayer()) {
                    return false;
                } else {
                    level = true;
                }
            }
        }
        for (EnumPosition allowedPos : component.getComponentPlaces()) {
            //check if position matches exactly
            if (pos.compatible(allowedPos)) {
                addComponent(pos, component, item.splitStack(1));
                return true;
            }
            //check if potential position is a full layer and make sure it can fit if it is
            if (!(level || allowedPos.isSingle || allowedPos.isTop != pos.isTop())) {
                //we pass the allowed pos as pos represents a single position
                addComponent(pos.asWholeLayer(), component, item.splitStack(1));
                return true;
            }
        }

        return false;
    }

    public ItemStack tryRemoveComponent(float x, float y, float z) {
        ExactPosition pos = ExactPosition.getPosFromCo2(x, y, z);
        for (int i = 0; i < components.size(); i++) {
            ExactPosition cPos = componentPositions.get(i);
            if (cPos.equals(pos) || (cPos.isWholeLayer() && cPos.isTop() == pos.isTop())) {
                return removeComponent(i);
            }
        }
        return null;
    }

    public void resizeInv(int size) {
        if (inv.length != size) {
            ItemStack newInv[] = new ItemStack[size];
            System.arraycopy(inv, 0, newInv, 0, Math.min(inv.length, size));
            inv = newInv;
            this.markVisualDirty();
        }
    }

    //Internal get/set methods
    private ItemStack removeComponent(int i) {
        components.remove(i);
        componentPositions.remove(i);
        ItemStack ret = componentItems.remove(i);
        markVisualDirty();
        return ret;
    }
    private void addComponent(ExactPosition pos, IMachineComponent component, ItemStack item) {
        componentItems.add(item.copy()); //its generally good practice to take a copy
        components.add(component);
        componentPositions.add(pos);
        markVisualDirty();
    }

    private void setCore(@Nullable ItemStack coreIn) {
        if (coreIn != null) {
            coreStack = coreIn.copy();
            core = (ICore) coreStack.getItem();
        } else {
            coreStack = null;
            core = null;
        }
    }

    private void removeCore() {
        setCore(null);
        if (locked) {
            logic.coreRemoved(this);
        }
        curOp = Operation.NOP;
        coreSpeed = 0;
        coreAngle = 0;
        markVisualDirty();
    }

    /*---------------------------
    |  Public Methods           |
    ----------------------------*/

    public boolean playerActivate(IBlockState state, EntityPlayer playerIn, EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {


        if (heldItem != null) {
            if (trySetCore(heldItem)) {
                playerIn.inventory.setInventorySlotContents(playerIn.inventory.currentItem, null);
                return true;
            }
        } else {
            if ((side == EnumFacing.UP || hitY > 0.9) && hasCore()) {
                playerIn.inventory.setInventorySlotContents(playerIn.inventory.currentItem, getCoreStack());
                removeCore();
                return true;
            }
        }

        if (locked) {
            if (heldItem != null) {
                logic.insertItem(this, playerIn, hand, heldItem, side, hitX, hitY, hitZ);
            } else {
                logic.removeItem(this, playerIn, side, hitX, hitY, hitZ);
            }
        } else {
            //machine is not valid
            if (heldItem != null) {
                Item item = heldItem.getItem();
                if (item instanceof IMachineComponent) {
                    tryAddComponent((IMachineComponent) item, heldItem, hitX, hitY, hitZ);
                } else if (item instanceof MachineLock) {
                    if (attemptLock()) {
                        //remove lock
                        playerIn.inventory.decrStackSize(playerIn.inventory.currentItem, 1);
                    }
                }
            } else {
                playerIn.inventory.setInventorySlotContents(playerIn.inventory.currentItem, tryRemoveComponent(hitX, hitY, hitZ));
            }
        }
        return true;
    }

    //note that this may cause the core to be removed or become inactive
    public void damageCore(int dmg) {
        if (coreStack != null && core.setCoreDamage(coreStack, core.getCoreDamage(coreStack) + dmg)) {
            coreBreakParticles(worldObj, pos);
            removeCore();
        }
    }

    //marks dirty and sends render updates
    public void markVisualDirty() {
        vDirty = true;
    }

    public void markRfUpdate() {
        rfUpdate = true;
    }

    public void startOp() {
        if (coreIsFunctional() && curOp != Operation.START) {
            curOp = Operation.START;
            //PacketHandler.sendToLoaded(worldObj, getPos(), new ChangeOperation(getPos(), curOp, worldObj.getTotalWorldTime()));
            markVisualDirty();
        }
    }

    public void stopOp() {
        if (coreIsFunctional() && curOp == Operation.START) {
            curOp = Operation.FINISH;
            //PacketHandler.sendToLoaded(worldObj, getPos(), new ChangeOperation(getPos(), curOp, worldObj.getTotalWorldTime()));
            markVisualDirty();
        }
    }

    /*---------------------------
    |  Internal Methods          |
    ----------------------------*/

    private void updateOp() {
        /*
        if (coreStack == null && curOp != Operation.NOP) {
            curOp = Operation.NOP;
            System.out.println("This shouldnt happen");
        }
        */
        if (curOp != Operation.NOP) {
            coreAngle = (coreAngle + coreSpeed) % 360;
            coreSpeed = Math.min((coreSpeed + (ACCELERATION * (curOp == Operation.START ? 1 : -1))), MAX_SPEED);
            if (coreSpeed <= 0) {
                curOp = Operation.NOP;
                coreSpeed = 0;
            }
        }

    }

    private boolean attemptLock() {
        IMachineLogic logicTemp = MachineLogicRegistry.INSTANCE.tryGetLogic(this);
        if (logicTemp != null) {
            logic = logicTemp;
            locked = true;
            logic.initMachine(this);
            markVisualDirty();
        }
        return locked;
    }
    //latter this will calculate the relative face
    private EnumFacing calcRelativeFace(EnumFacing face) {
        return face;
    }

    //update all the internal component list that is cached for speed
    private void updateInternals() {
        components = new ArrayList<>(8);
        for (ItemStack item : componentItems) {
            if (item.getItem() instanceof IMachineComponent) {
                components.add((IMachineComponent) item.getItem());
            } else {
                components.add(null);
                LogHelper.errorLog("TE at " + getPos().toString() + " had incorrect components, r u running the correct version? or was save data corrupt");
            }
        }
    }

    //this is only called once just before a core is removed from breaking, so core != null
    private void coreBreakParticles(World worldIn, BlockPos pos) {
        //World server cast as the method we use is not in World (this is important and confusing)
        WorldServer server = ((WorldServer) worldIn);
        //server.spawnParticle(EnumParticleTypes.REDSTONE, pos.getX()+0.5, pos.getY()+0.5, pos.getZ()+0.5, 10, 0.1, 0.1, 0.1, 0.0D);//, Item.getIdFromItem(AIncogData.MAKESHIFT_CORE));
        server.spawnParticle(EnumParticleTypes.ITEM_CRACK, pos.getX()+0.5, pos.getY()+0.95, pos.getZ()+0.5, 15, 0.1, 0.0, 0.1, 0.1, Item.getIdFromItem(coreStack.getItem()));
    }

    //does not drop the inventory
    private void resetFrame() {
        locked = false;
        resizeInv(0);
        setBatteryBehaviour(BatteryBehaviour.PROVIDE_ACCEPT);
        markVisualDirty();
    }

    //NOTE THIS METHODS DROP, THEY DO NOT REMOVE STUFF FROM THE TE
    public void dropInventory() {
        InventoryHelper.dropInventoryItems(worldObj, pos, this);
    }

    public void dropLock() {
        if (locked) {
            InventoryHelper.spawnItemStack(worldObj, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(AIncogData.MACHINE_LOCK, 1));
        }
    }

    public void dropComponents() {
        for (ItemStack stack : componentItems) {
            InventoryHelper.spawnItemStack(worldObj, pos.getX(), pos.getY(), pos.getZ(), stack);
        }
    }

    public void dropCore() {
        if (hasCore()) {
            InventoryHelper.spawnItemStack(worldObj, pos.getX(), pos.getY(), pos.getZ(), getCoreStack());
        }
    }

    public void dropEverything() {
        dropInventory();
        dropLock();
        dropComponents();
        dropCore();
    }

    /*---------------------------
    |  Override Methods          |
    ----------------------------*/
    @Override
    public MachineInfo getMachineInfo() {
        MachineInfo info = new MachineInfo();
        info.addInv(inv);
        info.addComponents(componentItems, componentPositions);
        info.addCore(coreStack);
        info.addEnergy(energy.getEnergyStored());
        info.addLogic(locked ? logic : null);
        info.addBatteryBehaviour(batteryBehaviour);
        info.addCurrentOperation(curOp);
        info.addCoreAngle(coreAngle);
        info.addCoreSpeed(coreSpeed);
        info.addCurrentTime(worldObj.getTotalWorldTime());
        return info;
    }

    @Override
    public void updateMachine(MachineInfo info) {
        inv = info.inv;
        componentItems = new ArrayList<>(8);
        componentItems.addAll(info.compItems);

        componentPositions = new ArrayList<>(8);
        componentPositions.addAll(info.compPos);

        if (componentPositions.size() == componentItems.size()) {
            int i = 0;
            while (i < componentItems.size()) {
                if (componentItems.get(i) == null || componentPositions.get(i) == null) {

                    LogHelper.errorLog("Error, found null entries in components at index: " + i);
                    LogHelper.errorLog(componentItems.get(i) + " and " + componentPositions.get(i));
                    LogHelper.errorLog("TE at " + LogHelper.getPosString(getPos()));
                    componentPositions.remove(i);
                    componentItems.remove(i);
                    continue;
                }
                i++;
            }
        } else {
            LogHelper.errorLog("Componenet sizes are wrong " + componentPositions.size() + " " + componentItems.size());
            LogHelper.errorLog("TE at " + LogHelper.getPosString(getPos()));
            componentPositions = new ArrayList<>(8);
            componentItems = new ArrayList<>(8);
        }

        setCore(info.core);
        updateInternals();

        energy.setEnergyStored(info.energy);

        locked = info.locked;
        if (locked) {
            logic = info.logic;
        } else {
            logic = null;
        }
        batteryBehaviour = info.batteryBehaviour;
        curOp = info.curOp;
        //this is set 2 negative one in a network update, theres a good change the core angle will dysync but it looks really dodgy if we try and synce them
        //TODO maybe try fix this? sounds hard and pointless
        if (info.coreAngle != -1) {
            coreAngle = info.coreAngle;
        }
        coreSpeed = info.coreSpeed;
        if (info.curTime != 0) {
            coreSpeed += (worldObj.getTotalWorldTime() - info.curTime) * ACCELERATION * curOp.scale;
            if (coreSpeed <= 0) {
                coreSpeed = 0;
            }
        }
        if (locked) {
            logic.postDeserialize(this);
        }
    }

    @Override
    public void update() {
        if (locked && coreIsFunctional())
            logic.tick(this);

        if (!worldObj.isRemote) {
            if (batteryBehaviour != BatteryBehaviour.ACCEPT && !receivedOrSent && EnergyUtils.sendEnergyToSides(getWorld(), getPos(), energy, MAX_RF_SEND)) {
                markRfUpdate();
            }
            receivedOrSent = false;

            if (vDirty) {
                PacketHandler.sendToLoaded(getWorld(), getPos(), new BlockRenderUpdater(getMachineInfo(), getPos()));
                vDirty = false;
                rfUpdate = false;
                markDirty();
            } else if (rfUpdate) {
                PacketHandler.sendToLoaded(getWorld(), getPos(), new RfUpdate(getRfForUpdate(), getPos()));
                rfUpdate = false;
                markDirty();
            }

            if (locked && worldObj.getWorldTime() % 10 == 0) {
                logic.spawnParticles(this, ((WorldServer) worldObj), pos);
            }
        }

        updateOp();
    }


    @Override
    public void onLoad() {
        //ask the server for the render info for this block
        if (getWorld().isRemote) {
            PacketHandler.sendToServer(new ClientRenderRequest(this));
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        updateMachine(new MachineInfo(compound));
        super.readFromNBT(compound);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        getMachineInfo().writeNBT(compound);
        return super.writeToNBT(compound);
    }
//TODO allow for insertion of components?
    @Override
    public int[] getSlotsForFace(EnumFacing side) {
        return locked ? logic.getSlotsForFace(side) : new int[0];
    }

    @Override
    public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
        return locked ? logic.canInsertItem(index, itemStackIn, direction) : false;
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
        return locked ? logic.canExtractItem(index, stack, direction) : false;
    }

    @Override
    public int getSizeInventory() {
        return inv.length;
    }

    @Nullable
    @Override
    public ItemStack getStackInSlot(int index) {
        return inv[index];
    }
//TODO seprate update for stack lose? does that need 2 be updated?
    @Nullable
    @Override
    public ItemStack decrStackSize(int index, int count) {
        ItemStack ret = inv[index].splitStack(count);
        if (inv[index].stackSize == 0) {
            removeStack(index);
        }
        return ret;
    }

    @Nullable
    @Override
    public ItemStack removeStackFromSlot(int index) {
        ItemStack ret = inv[index];
        removeStack(index);
        return ret;
    }

    private void removeStack(int slot) {
        inv[slot] = null;
        markVisualDirty();
    }
//TODO separate update if just the stack size changes?
    @Override
    public void setInventorySlotContents(int index, @Nullable ItemStack stack) {
        if (inv[index] == null) {
            if (stack != null) {
                markVisualDirty();
            }
        } else if (stack != null) {
            if (stack.getItem() != inv[index].getItem()) {
                markVisualDirty();
            }
        }
        inv[index] = stack;
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        /* stole from the chest, dont think i need it though (well intelij simplified it apparently, which is awesome) */
        return this.worldObj.getTileEntity(this.pos) == this && player.getDistanceSq((double) this.pos.getX() + 0.5D, (double) this.pos.getY() + 0.5D, (double) this.pos.getZ() + 0.5D) <= 64.0D;
    }

    @Override
    public void openInventory(EntityPlayer player) {}

    @Override
    public void closeInventory(EntityPlayer player) {}

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return true;
    }

    @Override
    public int getField(int id) {
        return 0;
    }

    @Override
    public void setField(int id, int value) {}

    @Override
    public int getFieldCount() {
        return 0;
    }

    @Override
    public void clear() {}

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        return !(locked && logic.hasCapability(capability, facing)) && super.hasCapability(capability, facing);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        T tmp = locked ? logic.getCapability(capability, facing) : null;
        return tmp == null ? super.getCapability(capability, facing) : tmp;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }

    @Override
    public boolean canConnectEnergy(EnumFacing from) {
        return true;
    }

    @Override
    public int getEnergyStored(EnumFacing from) {
        return energy.getEnergyStored();
    }

    @Override
    public int getMaxEnergyStored(EnumFacing from) {
        return energy.getMaxEnergyStored();
    }

    @Override
    public int receiveEnergy(EnumFacing from, int maxReceive, boolean simulate) {
        int i = batteryBehaviour != BatteryBehaviour.PROVIDE ? energy.receiveEnergy(maxReceive, simulate) : 0;
        if (i != 0) {
            markRfUpdate();
            receivedOrSent = true;
        }
        return i;
    }

    @Override
    public int extractEnergy(EnumFacing from, int maxExtract, boolean simulate) {
        int i = batteryBehaviour != BatteryBehaviour.ACCEPT ? energy.extractEnergy(maxExtract, simulate) : 0;
        if (i != 0) {
            markRfUpdate();
            receivedOrSent = true;
        }
        return i;
    }

    @Override
    public void receiveUpdate(int i) {
        energy.setEnergyStored(i);
    }

    @Override
    public int getRfForUpdate() {
        return energy.getEnergyStored();
    }
}
