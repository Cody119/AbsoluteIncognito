package com.superRainbowNinja.aincog.common.machineLogic;

import com.superRainbowNinja.aincog.AIncogData;
import com.superRainbowNinja.aincog.client.models.tileEntityRenders.MachineFrameRender;
import com.superRainbowNinja.aincog.common.blocks.MachineFrame;
import com.superRainbowNinja.aincog.common.capabilites.LockableTankImp;
import com.superRainbowNinja.aincog.common.items.IMachineComponent;
import com.superRainbowNinja.aincog.common.items.TankComponent;
import com.superRainbowNinja.aincog.common.tileEntity.MachineFrameTile;
import com.superRainbowNinja.aincog.util.LogHelper;
import com.superRainbowNinja.aincog.util.Operation;
import io.netty.buffer.ByteBuf;
import javafx.util.Pair;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.items.wrapper.InvWrapper;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by SuperRainbowNinja on 3/12/2016.
 *  recipes added in AIncogData
 *
 */
public class OreFormerLogic extends FluidLogic {
    private static final String NAME = "ore_former_logic";
    public static final int RF_PER_TICK = 20;
    public static final int TIME_PER_CHECK = 20;
    public static final int FLUID_PER_ORE = 500;
    public static final int RANGE = 1;
    public static final float CHANCE = 2.0f;//1f/(27*2);
    private int progress = 0;

    private int getRfPerTick(MachineFrameTile tile) {
        return (int) (RF_PER_TICK / tile.getCore().getEfficiency(tile.getCoreStack()));
    }

    private static ArrayList<Pair<Fluid, IBlockState>> recipes = new ArrayList<>(3);

    public static interface Recipe {
//TODO recipe's

        Fluid getFluid();
        boolean check(MachineFrameTile tile, FluidStack fluid);
        FluidStack consume(MachineFrame tile, FluidStack fluid);
    }

    public static Boolean addRecipe(Fluid fluid, IBlockState state) {
        if (fluid == null || state == null) {
            LogHelper.errorLog("Tried to add null fluid recipe");
            return false;
        } else {
            recipes.add(new Pair<>(fluid, state));
            return true;
        }
    }

    public static IBlockState getRecipe(Fluid fluid) {
        for (Pair<Fluid, IBlockState> p : recipes) {
            if (fluid == p.getKey()) {
                return p.getValue();
            }
        }
        return null;
    }

    @Override
    public void tick(MachineFrameTile tile) {
        if (!tile.getWorld().isRemote) {
            FluidStack fluidStack = tank.drain(FLUID_PER_ORE, false);
            int rfPerTick;
            IBlockState result;
            if (fluidStack != null &&
                    fluidStack.amount >= FLUID_PER_ORE &&
                    tile.getEnergyStored(null) > (rfPerTick = getRfPerTick(tile)) &&
                    (result = getRecipe(fluidStack.getFluid())) != null
                    ) {
                if (tile.getCurOp() != Operation.START) {
                    tile.startOp();
                }
                progress = (progress + 1) % TIME_PER_CHECK;
                tile.getEnergy().extractEnergy(rfPerTick, false);

                if (progress == (TIME_PER_CHECK - 1)) {
                    float chance = CHANCE * tile.getCore().getEfficiency(tile.getCoreStack());
                    Random r = new Random();
                    BlockPos pos = tile.getPos();
//
                    endLoop:
                    for (int x = pos.getX() - RANGE; x <= pos.getX() + RANGE; x++) {
                        for (int z = pos.getZ() - RANGE; z <= (pos.getZ() + RANGE); z++) {
                            for (int y = pos.getY() - (2 * RANGE) - 1; y <= pos.getY()-1; y++) {
                                if (r.nextFloat() <= chance && tile.getWorld().getBlockState(new BlockPos(x, y, z)).getBlock() == Blocks.STONE) {
                                    tile.getWorld().setBlockState(new BlockPos(x, y, z), result);
                                    tank.drain(FLUID_PER_ORE, true);
                                    LogHelper.infoLog("change " + LogHelper.getPosString(new BlockPos(x,y,z)));
                                    fluidStack = tank.drain(FLUID_PER_ORE, false);
                                    if (fluidStack == null ||
                                            fluidStack.amount < FLUID_PER_ORE ||
                                            (result = getRecipe(fluidStack.getFluid())) == null
                                            ) {
                                        break endLoop;
                                    }
                                }
                            }
                        }
                    }

                }
            } else {
                if (progress != 0) {
                    progress = 0;
                }
                if (tile.getCurOp() == Operation.START) {
                    tile.stopOp();
                }
            }
        }
    }

    @Override
    public void initMachine(MachineFrameTile tile) {
        super.initMachine(tile);
        tile.setBatteryBehaviour(MachineFrameTile.BatteryBehaviour.ACCEPT);
        tile.resizeInv(0);
        tank.setState(LockableTankImp.OutputState.IO);
    }

    @Override
    public void postDeserialize(MachineFrameTile tile) {
        super.postDeserialize(tile);
        tank.setState(LockableTankImp.OutputState.IO);
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void insertItem(MachineFrameTile te, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (FluidUtil.tryFillContainerAndStow(heldItem, tank, new InvWrapper(playerIn.inventory), Integer.MAX_VALUE,  playerIn))
            te.markVisualDirty();
        else if (FluidUtil.tryEmptyContainerAndStow(heldItem, tank, new InvWrapper(playerIn.inventory), Integer.MAX_VALUE, playerIn)) {
            te.markVisualDirty();
        }
    }

    @Override
    public void removeItem(MachineFrameTile te, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {}

    @Override
    public void renderTileEntityAt(MachineFrameRender r, MachineFrameTile teIn, double x, double y, double z, float partialTicks, int destroyStage) {
//        ItemStack stack = teIn.getStackInSlot(0);
//        if (stack != null) {
//            GlStateManager.pushMatrix();
//            GlStateManager.translate(x + 0.5, y + 0.5, z + 0.5);
//            GlStateManager.scale(1f / 4, 1f / 4, 1f / 4);
//
//            final float val = 360/100f;
//            float d0 = (float) (
//                    (teIn.getWorld().getWorldTime() % 100) * val
//                    + ((double) partialTicks) * val
//            );
//            GlStateManager.rotate(d0, 0, 1, 0);
//            Minecraft.getMinecraft().getRenderItem().renderItem(stack, ItemCameraTransforms.TransformType.NONE);
//            GlStateManager.popMatrix();
//        }
    }

    @Override
    public void spawnParticles(MachineFrameTile tile, WorldServer worldServer, BlockPos pos) {
//        if (tile.getCurOp() != Operation.NOP) {
//            worldServer.spawnParticle(EnumParticleTypes.REDSTONE, pos.getX()+0.5, pos.getY()+0.5, pos.getZ()+0.5, 10, 0.05, 0.05, 0.05, 0.0D);//, Item.getIdFromItem(AIncogData.MAKESHIFT_CORE));
//        }
    }

    @Override
    public void coreRemoved(MachineFrameTile teIn) {
        //rfMiniBuf = 0; //the operation will be auto wipped, we just need 2 worry about internals of this class
    }

    @Override
    public void serialize(ByteBuf buf) {
        buf.writeInt(progress);
    }

    @Override
    public void deserialize(ByteBuf buf) {
        progress = buf.readInt();
    }

    @Override
    public IMachineLogic readFromNBT(NBTTagCompound compound) {
        progress = compound.getInteger("PROG");
        return this;
    }

    @Nullable
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setInteger("PROG", progress);
        return compound;
    }

    public static final int[] SLOTS = new int[]{0};

    @Override
    public int[] getSlotsForFace(EnumFacing side) {
        return SLOTS;
    }

    public static final class Provider implements IMachineLogicProvider {

        @Override
        public IMachineLogic validMachine(MachineFrameTile tile) {
            int coilCount = 0;
            int count = 0;
            ArrayList<IMachineComponent> components = tile.getComponents();
            for (int i = 0; i < components.size(); i++) {
                if (components.get(i) instanceof TankComponent && tile.getComponentPositions().get(i).isTop()) {
                    coilCount++;
                }
                if (components.get(i) instanceof AIncogData.Piece) {
                    count++;
                }
            }
            if (coilCount == 1 && count == 4) {
                return new OreFormerLogic();
            }
            return null;
        }

        @Override
        public String[] getLogics() {
            return new String[]{OreFormerLogic.NAME};
        }

        @Override
        public IMachineLogic deserializeLogic(String name, ByteBuf buf) {
            OreFormerLogic logic = new OreFormerLogic();
            logic.deserialize(buf);
            return logic;
        }

        @Override
        public IMachineLogic getLogicByName(String name) {
            return new OreFormerLogic();
        }
    }
}