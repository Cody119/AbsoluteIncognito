package com.superRainbowNinja.aincog.common.machineLogic;

import com.superRainbowNinja.aincog.client.models.tileEntityRenders.MachineFrameRender;
import com.superRainbowNinja.aincog.common.tileEntity.MachineFrameTile;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;

/**
 * Created by SuperRainbowNinja on 2/12/2016.
 */
public interface IMachineLogic extends ICapabilityProvider {
    //Logic should store tile
    MachineFrameTile getTile();
    //called every tick once this logic is installed and there is a valid core
    void tick();
    //called after the tile entity has installed this logic, giving a chance 2 set an inv size and such
    //it is not called when the machine is reloaded
    void initMachine(MachineFrameTile tile);
    //every type of machine logic must have a unique name that will be checked when reloading
    String getName();
    /*Note these next 2 methods don't have 2 do insertion/removal, its just what they generally do*/
    //called when player attempts to insert an item
    void insertItem(EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ);
    //called when a player clicks with an empty hand (general a remove attempt)
    void removeItem(EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ);
    //get a chance 2 render additional stuff
    void renderTileEntityAt(MachineFrameRender r, double x, double y, double z, float partialTicks, int destroyStage);
    //called every 10 ticks, food interval for spawning particles (server side)
    void spawnParticles(WorldServer worldServer, BlockPos pos);
    //called if the core breaks/is removed (note could be called whenever the core is damaged i.e. within another logic method)
    void coreRemoved();
    //called after Deserialization (from nbt or byte buffer)
    void postDeserialize(MachineFrameTile tile);

    //only need these if
    //used when sending render info, so if the client doesn't need info, don't need 2 serialize it
    void serialize(ByteBuf buf);
    void deserialize(ByteBuf buf);
    //used for saving/loading, note the inv is saved automatically so dont need 2 worry about that
    //it probs dosent need to get get the tile seeing as it gets passed every tick, well c
    IMachineLogic readFromNBT(NBTTagCompound compound);
    @Nullable
    NBTTagCompound writeToNBT(NBTTagCompound compound);

    default boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        return false;
    }
    default <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        return null;
    }

    default int[] getSlotsForFace(EnumFacing side) {
        return new int[0];
    }

    default boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
        return false;
    }

    default boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
        return false;
    }


    /* temp */
    IMachineLogic CLIENT_MACHINE = new IMachineLogic() {
        MachineFrameTile tile;

        @Override
        public MachineFrameTile getTile() { return tile; }
        @Override
        public void tick() {}
        @Override
        public void initMachine(MachineFrameTile tileIn) { tile = tileIn; }
        @Override
        public String getName() {return "CLIENT_MACHINE";}
        @Override
        public void insertItem(EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {}
        @Override
        public void removeItem(EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {}
        @Override
        public void renderTileEntityAt(MachineFrameRender r, double x, double y, double z, float partialTicks, int destroyStage) {}
        @Override
        public void spawnParticles(WorldServer worldServer, BlockPos pos) {}
        @Override
        public void coreRemoved() {}
        @Override
        public void postDeserialize(MachineFrameTile tileIn) { tile = tileIn; }
        @Override
        public void serialize(ByteBuf buf) {}
        @Override
        public void deserialize(ByteBuf buf) {}
        @Override
        public IMachineLogic readFromNBT(NBTTagCompound compound) {return this;}
        @Nullable
        @Override
        public NBTTagCompound writeToNBT(NBTTagCompound compound) {return compound;}
    };
}
