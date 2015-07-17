package de.longor.talecraft.blocks.util.tileentity;

import java.util.List;
import java.util.Random;

import org.mozilla.javascript.NativeObject;
import org.mozilla.javascript.Scriptable;

import de.longor.talecraft.TaleCraft;
import de.longor.talecraft.blocks.TCTileEntity;
import de.longor.talecraft.invoke.IInvoke;
import de.longor.talecraft.invoke.IInvokeSource;
import de.longor.talecraft.invoke.Invoke;
import de.longor.talecraft.invoke.NullInvoke;
import net.minecraft.block.state.IBlockState;
import net.minecraft.command.CommandResultStats.Type;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class RedstoneTriggerBlockTileEntity extends TCTileEntity {
	IInvoke triggerInvoke;
	
	public RedstoneTriggerBlockTileEntity() {
		triggerInvoke = NullInvoke.instance;
	}
	
	@Override
	public void init() {
		// don't do anything
	}
    
    public void readFromNBT_do(NBTTagCompound compound) {
    	triggerInvoke = IInvoke.Serializer.read(compound.getCompoundTag("triggerInvoke"));
    }
    
    public void writeToNBT_do(NBTTagCompound compound) {
    	compound.setTag("triggerInvoke", IInvoke.Serializer.write(triggerInvoke));
    }
    
	public void invokeFromUpdateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
		if(worldIn.isRemote)
			return;
		
		Invoke.invoke(triggerInvoke, this);
	}
    
	@Override
	public void commandReceived(String command, NBTTagCompound data) {
		super.commandReceived(command, data);
		
		if(command.equals("trigger")) {
			Invoke.invoke(triggerInvoke, this);
		}
	}
	
	@Override
	public String getName() {
		return "RedstoneTrigger@"+pos;
	}
	
	@Override
	public String toString() {
		return "RedstoneTriggerTileEntity:{}";
	}

	@Override
	public void getInvokes(List<IInvoke> invokes) {
		invokes.add(triggerInvoke);
	}
	
	public IInvoke getInvoke() {
		return triggerInvoke;
	}

//	@Override
//	public void getInvokesAsDataCompounds(List<NBTTagCompound> invokes) {
//		invokes.add(triggerInvoke);
//	}

	@Override
	public void getInvokeColor(float[] color) {
		color[0] = 0.75f;
		color[1] = 0.0f;
		color[2] = 0.0f;
	}
	
}
