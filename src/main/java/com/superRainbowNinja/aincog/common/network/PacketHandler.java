package com.superRainbowNinja.aincog.common.network;

import com.superRainbowNinja.aincog.refrence.Reference;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Created by SuperRainbowNinja on 11/11/2016.
 */
public final class PacketHandler {
    private static int ID;

    private static final SimpleNetworkWrapper NETWORK = new SimpleNetworkWrapper(Reference.NETWORK_CHANNEL);

    private PacketHandler() {}

    public static void initMessages() {
        NETWORK.registerMessage(BlockRenderUpdater.Handle.class, BlockRenderUpdater.class, ID++, Side.CLIENT);
        NETWORK.registerMessage(ClientRenderRequest.Handle.class, ClientRenderRequest.class, ID++, Side.SERVER);
        NETWORK.registerMessage(RfUpdate.Handle.class, RfUpdate.class, ID++, Side.CLIENT);
        NETWORK.registerMessage(InventoryUpdate.Handle.class, InventoryUpdate.class, ID++, Side.CLIENT);
        //NETWORK.registerMessage(ChangeOperation.Handle.class, ChangeOperation.class, ID++, Side.CLIENT);
    }

    //Send to players who have this chunk loaded
    public static void sendToLoaded(World world, BlockPos pos, IMessage toSend) {
        if(world instanceof WorldServer) {
            WorldServer worldServer = (WorldServer) world;

            for (EntityPlayer player : worldServer.playerEntities) {
                EntityPlayerMP playerMP = (EntityPlayerMP) player;

                //the bit shift converts 2 chunk coordinates
                if (worldServer.getPlayerChunkMap().isPlayerWatchingChunk(playerMP, pos.getX() >> 4, pos.getZ() >> 4)) {
                    NETWORK.sendTo(toSend, playerMP);
                }
            }

        }
    }

    public static void sendToServer(IMessage toSend) {
        NETWORK.sendToServer(toSend);
    }

    public static void sendTo(EntityPlayerMP player, IMessage toSend) {
        NETWORK.sendTo(toSend, player);
    }
}
