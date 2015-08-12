package de.longor.talecraft.blocks.util.tileentity;

import java.util.List;

import org.mozilla.javascript.NativeObject;
import org.mozilla.javascript.Scriptable;

import de.longor.talecraft.TaleCraft;
import de.longor.talecraft.TaleCraftBlocks;
import de.longor.talecraft.blocks.TCTileEntity;
import de.longor.talecraft.invoke.BlockTriggerInvoke;
import de.longor.talecraft.invoke.EnumTriggerState;
import de.longor.talecraft.invoke.IInvoke;
import de.longor.talecraft.invoke.IInvokeSource;
import de.longor.talecraft.invoke.Invoke;
import de.longor.talecraft.invoke.NullInvoke;
import net.minecraft.block.state.IBlockState;
import net.minecraft.command.CommandResultStats.Type;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
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

public class ClockBlockTileEntity extends TCTileEntity implements IUpdatePlayerListBox {
	IInvoke clockInvoke;
	IInvoke clockStartInvoke;
	IInvoke clockStopInvoke;
	
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
		clockInvoke = BlockTriggerInvoke.ZEROINSTANCE;
		clockStartInvoke = BlockTriggerInvoke.ZEROINSTANCE;
		clockStopInvoke = BlockTriggerInvoke.ZEROINSTANCE;
		
		set_repeat = 10;
		set_speed = 1;
		set_time = 20;
		
		active = false;
		repeat = set_repeat;
		speed = set_speed;
		time = set_time;
	}

	@Override
	public void init() {
		// don't do anything
	}
	
	@Override
    public void writeToNBT_do(NBTTagCompound compound)
    {
    	compound.setBoolean("active", active);
    	compound.setInteger("repeat", repeat);
    	compound.setInteger("speed", speed);
    	compound.setInteger("time", time);
    	
    	compound.setInteger("init_repeat", set_repeat);
    	compound.setInteger("init_speed", set_speed);
    	compound.setInteger("init_time", set_time);
    	
    	compound.setTag("clockInvoke", IInvoke.Serializer.write(clockInvoke));
    	compound.setTag("clockStartInvoke", IInvoke.Serializer.write(clockStartInvoke));
    	compound.setTag("clockStopInvoke", IInvoke.Serializer.write(clockStopInvoke));
    }
    
    public void readFromNBT_do(NBTTagCompound compound) {
        set_repeat = compound.getInteger("init_repeat");
        set_speed = compound.getInteger("init_speed");
        set_time = compound.getInteger("init_time");
        
        active = compound.getBoolean("active");
        repeat = compound.getInteger("repeat");
        speed = compound.getInteger("speed");
        time = compound.getInteger("time");
        
        clockInvoke = IInvoke.Serializer.read(compound.getCompoundTag("clockInvoke"));
        clockStartInvoke = IInvoke.Serializer.read(compound.getCompoundTag("clockStartInvoke"));
        clockStopInvoke = IInvoke.Serializer.read(compound.getCompoundTag("clockStopInvoke"));
	}
    
    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt)
    {
    	NBTTagCompound comp = pkt.getNbtCompound();
    	readFromNBT_do(comp);
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
		Invoke.invoke(clockStartInvoke, this, null, EnumTriggerState.ON);
    }
	
    public void clockPause() {
		active ^= true;
    }
    
    public void clockStop() {
		repeat = 0;
		speed = 0;
		time = 0;
		active = false;
		Invoke.invoke(clockStopInvoke, this, null, EnumTriggerState.OFF);
    }
    
    public void clockTick() {
		Invoke.invoke(clockInvoke, this, null, EnumTriggerState.IGNORE);
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
		if("trigger".equals(command)) {
			Invoke.invoke(clockInvoke, this, null, EnumTriggerState.ON);
		}
		
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
		
		super.commandReceived(command, data);
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
	public void getInvokes(List<IInvoke> invokes) {
		invokes.add(clockInvoke);
		invokes.add(clockStartInvoke);
		invokes.add(clockStopInvoke);
	}
	
	public IInvoke getTickInvoke() {
		return clockInvoke;
	}
	
	public IInvoke getStartInvoke() {
		return clockStartInvoke;
	}
	
	public IInvoke getStopInvoke() {
		return clockStopInvoke;
	}
	
//	@Override
//	public void getInvokesAsDataCompounds(List<NBTTagCompound> invokes) {
//		invokes.add(clockInvoke);
//	}

	@Override
	public void getInvokeColor(float[] color) {
		color[0] = 0.5f;
		color[1] = 0.1f;
		color[2] = 0.5f;
	}
	
}
