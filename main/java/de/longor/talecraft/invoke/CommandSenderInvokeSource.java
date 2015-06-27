package de.longor.talecraft.invoke;

import java.util.List;

import jdk.nashorn.internal.objects.NativeObject;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

import org.mozilla.javascript.Scriptable;

import de.longor.talecraft.TaleCraft;

public class CommandSenderInvokeSource implements IInvokeSource {
	Scriptable scope;
	ICommandSender sender;
	
	public CommandSenderInvokeSource(ICommandSender sender) {
		this.scope = TaleCraft.globalScriptManager.createNewScope();
		this.sender = sender;
	}
	
	@Override
	public Scriptable getScriptScope() {
		return scope;
	}

	@Override
	public ICommandSender getCommandSender() {
		return sender;
	}

	@Override
	public BlockPos getPosition() {
		return sender.getPosition();
	}

	@Override
	public World getWorld() {
		return sender.getEntityWorld();
	}

	@Override
	public void getInvokes(List<IInvoke> invokes) {
		// nope
	}

}
