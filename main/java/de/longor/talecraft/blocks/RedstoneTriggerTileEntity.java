package de.longor.talecraft.blocks;

import java.util.List;
import java.util.Random;

import de.longor.talecraft.TaleCraft;
import de.longor.talecraft.invoke.BlockInvokeSource;
import de.longor.talecraft.invoke.IInvokeSource;
import de.longor.talecraft.invoke.Invoke;
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

public class RedstoneTriggerTileEntity extends TileEntity implements ICommandSender, IInvokeSource {
	NBTTagCompound triggerInvoke;
	IInvokeSource selfSource;
	
	public RedstoneTriggerTileEntity() {
		triggerInvoke = new NBTTagCompound();
	}
	
    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        readFromNBT_do(compound);
    }
    
    private void readFromNBT_do(NBTTagCompound compound) {
    	NBTTagCompound cTagCompound = compound.getCompoundTag("triggerInvoke");
    	
    	triggerInvoke.merge(cTagCompound);
    }
    
	@Override
    public void writeToNBT(NBTTagCompound compound) {
    	super.writeToNBT(compound);
    	writeToNBT_do(compound);
    }
    
    private void writeToNBT_do(NBTTagCompound compound) {
    	compound.setTag("triggerInvoke", triggerInvoke);
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public double getMaxRenderDistanceSquared()
    {
        return 4096; // 128 blocks!
    }
    
    @Override
    public boolean shouldRenderInPass(int pass)
    {
        return TaleCraft.instance.proxy.isBuildMode() ? (pass == 0) : false;
    }
    
	public void invokeFromUpdateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
		if(worldIn.isRemote)
			return;
		
		if(selfSource == null)
			selfSource = new BlockInvokeSource(this);
    	
		Invoke.invoke(triggerInvoke, selfSource);
	}
	
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate)
    {
        return false;
    }
	
	@Override
	public String getName() {
		return "RedstoneTrigger@"+pos;
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
		return pos;
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
		return "RedstoneTriggerTileEntity:{}";
	}

	@Override
	public ICommandSender getCommandSender() {
		return this;
	}

	@Override
	public void getInvokeDataCompounds(List<NBTTagCompound> invokes) {
		invokes.add(triggerInvoke);
	}
	
}
