package de.longor.talecraft.blocks.util.tileentity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.mozilla.javascript.NativeObject;
import org.mozilla.javascript.Scriptable;

import com.google.common.collect.Maps;

import de.longor.talecraft.TaleCraft;
import de.longor.talecraft.blocks.TCTileEntity;
import de.longor.talecraft.invoke.BlockTriggerInvoke;
import de.longor.talecraft.invoke.IInvoke;
import de.longor.talecraft.invoke.IInvokeSource;
import de.longor.talecraft.invoke.Invoke;
import de.longor.talecraft.invoke.NullInvoke;
import net.minecraft.command.CommandResultStats.Type;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class RelayBlockTileEntity extends TCTileEntity {
	private Map<String, IInvoke> invokes;
	
	public RelayBlockTileEntity() {
		invokes = Maps.newHashMap();
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
		invokes.addAll(this.invokes.values());
	}
	
	public void triggerRelayInvoke() {
		for (IInvoke invoke : invokes.values()) {
			Invoke.invoke(invoke, this);
		}
	}
    
	@Override
	public void commandReceived(String command, NBTTagCompound data) {
		super.commandReceived(command, data);
		
		if(command.equals("invoke_add")) {
			invokes.put("rix"+System.currentTimeMillis(), new BlockTriggerInvoke());
            worldObj.markBlockForUpdate(this.pos);
		}
		
		if(command.equals("invoke_remove")) {
			invokes.remove(data.getString("invokeToRemove"));
			worldObj.markBlockForUpdate(this.pos);
		}
	}
	
	@Override
	public void readFromNBT_do(NBTTagCompound comp) {
		invokes.clear();
		
		for(Object obj : comp.getKeySet()) {
			String key = (String) obj;
			
			if(key.startsWith("rix")) {
				NBTTagCompound rawinvoke = comp.getCompoundTag(key);
				IInvoke invoke = IInvoke.Serializer.read(rawinvoke);
				invokes.put(key, invoke);
			}
		}
	}
	
	@Override
	public void writeToNBT_do(NBTTagCompound comp) {
		int ID = 0;
		for(Entry<String, IInvoke> entry : invokes.entrySet()) {
			comp.setTag(entry.getKey(),IInvoke.Serializer.write(entry.getValue()));
			ID++;
		}
	}
	
	public Map<String, IInvoke> getInvokes() {
		return invokes;
	}
	
}
