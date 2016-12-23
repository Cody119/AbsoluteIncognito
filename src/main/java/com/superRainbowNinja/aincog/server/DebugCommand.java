package com.superRainbowNinja.aincog.server;

import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.FluidRegistry;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by SuperRainbowNinja on 8/10/2016.
 */
public class DebugCommand implements ICommand{
    ArrayList<String> names;

    public DebugCommand() {
        names = new ArrayList<String>();
        names.add("aiDebug");
    }

    @Override
    public String getCommandName() {
        return "aiDebug";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "aiDebug";
    }

    @Override
    public List<String> getCommandAliases() {
        return names;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        //System.out.println(Minecraft.getMinecraft().theWorld.getCombinedLight(sender.getCommandSenderEntity().getPosition(), 15));
        //System.out.println(Minecraft.getMinecraft().theWorld.getCombinedLight(new BlockPos(0, 0, 0), 15));
        System.out.println(FluidRegistry.isFluidRegistered("my_fluid"));
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return true;
    }

    @Override
    public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos) {
        return null;
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        return true;
    }

    @Override
    public int compareTo(ICommand o) {
        return 0;
    }
}
