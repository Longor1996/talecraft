package de.longor.talecraft.invoke;

import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public interface IInvokeSource {
	
	public ICommandSender getCommandSender();
	
	public BlockPos getPosition();
	
	public World getWorld();
	
}
