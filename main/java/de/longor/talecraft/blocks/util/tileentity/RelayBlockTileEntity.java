package de.longor.talecraft.blocks.util.tileentity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.mozilla.javascript.NativeObject;
import org.mozilla.javascript.Scriptable;

import de.longor.talecraft.TaleCraft;
import de.longor.talecraft.blocks.TCTileEntity;
import de.longor.talecraft.invoke.BlockTriggerInvoke;
import de.longor.talecraft.invoke.IInvoke;
import de.longor.talecraft.invoke.IInvokeSource;
import de.longor.talecraft.invoke.Invoke;
import net.minecraft.command.CommandResultStats.Type;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class RelayBlockTileEntity extends TCTileEntity {
	private List<IInvoke> invokes;
	
	public RelayBlockTileEntity() {
		invokes = new ArrayList(1);
	}

	@Override
	public void init() {
		// don't do anything
	}
	
	@Override
	public String getName() {
		return "RelayBlock@"+pos;
	}
	
	@Override
	public String toString() {
		return "RelayBlockTileEntity:{}";
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

	@Override
	public void readFromNBT_do(NBTTagCompound comp) {
		NBTTagList list = comp.getTagList("relayInvokes", comp.getId());
		
		for(int i = 0; i < list.tagCount(); i++) {
			NBTTagCompound sub = list.getCompoundTagAt(i);
			IInvoke invoke = IInvoke.Serializer.read(sub);
			invokes.add(invoke);
		}
	}

	@Override
	public void writeToNBT_do(NBTTagCompound comp) {
		NBTTagList list = new NBTTagList();
		for(IInvoke invoke : invokes) {
			list.appendTag(IInvoke.Serializer.write(invoke));
		}
	}
	
}
