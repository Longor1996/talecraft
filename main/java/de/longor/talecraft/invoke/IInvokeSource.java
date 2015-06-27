package de.longor.talecraft.invoke;

import java.util.List;

import org.mozilla.javascript.Scriptable;

import net.minecraft.command.ICommandSender;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public interface IInvokeSource {
	
	public Scriptable getScriptScope();
	
	public ICommandSender getCommandSender();
	
	public BlockPos getPosition();
	
	public World getWorld();
	
	public void getInvokes(List<IInvoke> invokes);
	
}
