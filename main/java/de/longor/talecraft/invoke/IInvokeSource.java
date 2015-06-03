package de.longor.talecraft.invoke;

import java.util.List;

import net.minecraft.command.ICommandSender;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public interface IInvokeSource {
	
	public ICommandSender getCommandSender();
	
	public BlockPos getPosition();
	
	public World getWorld();
	
	public void getInvokes(List<IInvoke> invokes);
	
}
