package de.longor.talecraft.invoke;

import java.util.List;

import net.minecraft.command.ICommandSender;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class BlockInvokeSource implements IInvokeSource {
	ICommandSender commandSender;
	BlockPos position;
	World world;
	
	public BlockInvokeSource(TileEntity te) {
		position = te.getPos();
		world = te.getWorld();
		commandSender = (ICommandSender) te;
	}
	
	@Override
	public BlockPos getPosition() {
		return position;
	}

	@Override
	public World getWorld() {
		return world;
	}
	
	@Override
	public ICommandSender getCommandSender() {
		return commandSender;
	}
	
	@Override
	public String toString() {
		return "BlockInvokeSource:{"+commandSender+"}@"+position;
	}
	
	@Override
	public void getInvokeDataCompounds(List<NBTTagCompound> invokes) {
		// none
	}
	
}
