package de.longor.talecraft.blocks;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import de.longor.talecraft.invoke.BlockTriggerInvoke;
import de.longor.talecraft.invoke.IInvoke;
import de.longor.talecraft.invoke.IInvokeSource;
import de.longor.talecraft.invoke.Invoke;
import net.minecraft.command.CommandResultStats.Type;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class RelayBlockTileEntity extends TileEntity implements ICommandSender, IInvokeSource {
	private List<IInvoke> invokes;
	
	public RelayBlockTileEntity() {
		invokes = new ArrayList(1);
	}
	
	@Override
	public String getName() {
		return "RelayBlock@"+pos;
	}
	
	@Override
	public IChatComponent getDisplayName() {
		return new ChatComponentText(getName());
	}
	
	@Override
	public void addChatMessage(IChatComponent message) {
		// nope!
	}
	
	@Override
	public boolean canUseCommand(int permLevel, String commandName) {
		return true;
	}
	
	@Override
	public BlockPos getPosition() {
		return this.pos;
	}
	
	@Override
	public Vec3 getPositionVector() {
		return new Vec3(this.pos.getX(),this.pos.getY(),this.pos.getZ());
	}
	
	@Override
	public World getEntityWorld() {
		return this.worldObj;
	}
	
	@Override
	public Entity getCommandSenderEntity() {
		return null; // nope, can't implement!
	}
	
	@Override
	public boolean sendCommandFeedback() {
		return false; // nope
	}
	
	@Override
	public void setCommandStat(Type type, int amount) {
		// nope
	}
	
	@Override
	public String toString() {
		return "RelayBlockTileEntity:{}";
	}
	
	@Override
	public ICommandSender getCommandSender() {
		return this;
	}
	
	@Override
	public void getInvokes(List<IInvoke> invokes) {
		invokes.addAll(this.invokes);
	}
	
	public void triggerRelayInvoke() {
		for (IInvoke invoke : invokes) {
			Invoke.invoke(invoke, this);
		}
	}
	
}
