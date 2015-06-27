package de.longor.talecraft.blocks.util.tileentity;

import java.util.List;

import org.mozilla.javascript.NativeObject;
import org.mozilla.javascript.Scriptable;

import de.longor.talecraft.TaleCraft;
import de.longor.talecraft.blocks.TCTileEntity;
import de.longor.talecraft.invoke.EmbeddedScriptInvoke;
import de.longor.talecraft.invoke.FileScriptInvoke;
import de.longor.talecraft.invoke.IInvoke;
import de.longor.talecraft.invoke.IInvokeSource;
import de.longor.talecraft.invoke.IScriptInvoke;
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

public class ScriptBlockTileEntity extends TCTileEntity {
	IScriptInvoke scriptInvoke;
	
	public ScriptBlockTileEntity() {
		scriptInvoke = new FileScriptInvoke();
	}
	
	@Override
	public void init() {
		
	}
	
	public void triggerInvokeScript() {
		Invoke.invoke(scriptInvoke, this);
	}
	
	@Override
	public void commandReceived(String command, NBTTagCompound data) {
		super.commandReceived(command, data);
		
		if(command.equals("reload")) {
			scriptInvoke.reloadScript();
		}
		
		if(command.equals("execute")) {
			triggerInvokeScript();
		}
		
		if(command.equals("reloadexecute")) {
			scriptInvoke.reloadScript();
			triggerInvokeScript();
		}
		
	}
	
	@Override
	public String getName() {
		return "ScriptBlock@"+pos;
	}
	
	@Override
	public String toString() {
		return "ScriptBlockTileEntity:{"+scriptInvoke+", "+getScriptScope()+"}";
	}
	
	@Override
	public void getInvokes(List<IInvoke> invokes) {
		invokes.add(scriptInvoke);
	}
    
    public void readFromNBT_do(NBTTagCompound compound) {
    	scriptInvoke = (IScriptInvoke) IInvoke.Serializer.readSI(compound.getCompoundTag("scriptInvoke"));
    	scriptInvoke.reloadScript();
    }
    
    public void writeToNBT_do(NBTTagCompound compound) {
    	compound.setTag("scriptInvoke", IInvoke.Serializer.write(scriptInvoke));
    }
    
	public String getScriptName() {
		TaleCraft.logger.info("getScriptName() " + scriptInvoke.getScriptName());
		return scriptInvoke == null ? "" : scriptInvoke.getScriptName();
	}
	
}
