package de.longor.talecraft.blocks;

import java.util.List;

import de.longor.talecraft.TaleCraft;
import de.longor.talecraft.TaleCraftBlocks;
import de.longor.talecraft.invoke.BlockInvokeSource;
import de.longor.talecraft.invoke.IInvokeSource;
import de.longor.talecraft.invoke.Invoke;
import net.minecraft.block.state.IBlockState;
import net.minecraft.command.CommandResultStats.Type;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ClockBlockTileEntity extends TileEntity implements
		IUpdatePlayerListBox,
		ICommandSender,
		IInvokeSource,
		BlockCommandReceiver {
	
	NBTTagCompound clockInvoke;
	IInvokeSource selfSource;
	
	public int set_repeat;
	public int set_speed;
	public int set_time;
	
	/** Countdown Active? **/
	public boolean active;
	
	/** Countdown Repeats! **/
	public int repeat;
	
	/** Countdown Speed! **/
	public int speed;
	
	/** Countdown Value! **/
	public int time;
	
	public ClockBlockTileEntity() {
		clockInvoke = new NBTTagCompound();
		
		set_repeat = 10;
		set_speed = 1;
		set_time = 20;
		
		active = true;
		repeat = set_repeat;
		speed = set_speed;
		time = set_time;
	}
	
    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        readNBT_do(compound);
    }

	@Override
    public void writeToNBT(NBTTagCompound compound)
    {
    	super.writeToNBT(compound);
    	
    	compound.setBoolean("active", active);
    	compound.setInteger("repeat", repeat);
    	compound.setInteger("speed", speed);
    	compound.setInteger("time", time);
    	
    	compound.setInteger("init_repeat", set_repeat);
    	compound.setInteger("init_speed", set_speed);
    	compound.setInteger("init_time", set_time);
    	
    	compound.setTag("clockInvoke", clockInvoke);
    }
    
    private void readNBT_do(NBTTagCompound compound) {
        set_repeat = compound.getInteger("init_repeat");
        set_speed = compound.getInteger("init_speed");
        set_time = compound.getInteger("init_time");
        
        active = compound.getBoolean("active");
        repeat = compound.getInteger("repeat");
        speed = compound.getInteger("speed");
        time = compound.getInteger("time");
        
    	NBTTagCompound cTagCompound = compound.getCompoundTag("clockInvoke");
    	clockInvoke.merge(cTagCompound);
	}
    
    @Override
    public void onDataPacket(net.minecraft.network.NetworkManager net, net.minecraft.network.play.server.S35PacketUpdateTileEntity pkt)
    {
    	NBTTagCompound comp = pkt.getNbtCompound();
    	readNBT_do(comp);;
    	// TaleCraft.logger.info("CBLOCK : onDataPacket : " + comp);
    }
    
    @Override
    public Packet getDescriptionPacket()
    {
    	NBTTagCompound nbttagcompound = new NBTTagCompound();
        this.writeToNBT(nbttagcompound);
        return new S35PacketUpdateTileEntity(this.pos, 3, nbttagcompound);
    }
	
    public void clockStart() {
		repeat = set_repeat;
		speed = set_speed;
		time = set_time;
		active = true;
    }
	
    public void clockPause() {
		active ^= true;
    }
    
    public void clockStop() {
		repeat = 0;
		speed = 0;
		time = 0;
		active = false;
    }
    
    public void clockTick() {
		if(selfSource == null)
			selfSource = new BlockInvokeSource(this);
    	
		Invoke.invoke(clockInvoke, selfSource);
    }
    
    @Override
    public void update() {
    	if(worldObj.isRemote)
    		return;
    	
    	// UPDATE!
    	if(active) {
    		time -= speed;
    		
    		if(time <= 0) {
    			// The clock reached its end, what happens now?
    			
    			// execute
    			clockTick();
                worldObj.markBlockForUpdate(this.pos);
    			
    			// repeat?
    			if(repeat > 0 || repeat == -1) {
    				time = set_time;
    				
    				if(repeat != -1)
    					repeat--;
    			} else {
    				clockStop();
    			}
    		}
    	}
    	
    }
    
	@Override
	public void commandReceived(String command, NBTTagCompound data) {
		TaleCraft.logger.info("command @ " + this + ": " + command + " -> " + data);
		
		if("start".equals(command)) {
			clockStart();
		}
		
		if("pause".equals(command)) {
			clockPause();
		}
		
		if("stop".equals(command)) {
			clockStop();
		}
		
        worldObj.markBlockForUpdate(this.pos);
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
	
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate)
    {
        return false;
    }
    
	public String getStateAsString() {
		return "[" + active + " | " + time + ", " + repeat + ", " + speed + "]";
	}
	
	public boolean isClockRunning() {
		return active;
	}

	@Override
	public String getName() {
		return "ClockBlock@"+pos;
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
		return null;
	}

	@Override
	public boolean sendCommandFeedback() {
		return false;
	}

	@Override
	public void setCommandStat(Type type, int amount) {}

	@Override
	public ICommandSender getCommandSender() {
		return this;
	}

	@Override
	public void getInvokeDataCompounds(List<NBTTagCompound> invokes) {
		invokes.add(clockInvoke);
	}
	
}
