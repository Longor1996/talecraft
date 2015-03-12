package de.longor.talecraft.blocks;

import de.longor.talecraft.TaleCraft;
import de.longor.talecraft.TaleCraftBlocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ClockBlockTileEntity extends TileEntity implements IUpdatePlayerListBox {
	int set_repeat;
	int set_speed;
	int set_time;
	
	boolean active;
	int repeat;
	int speed;
	int time;
	
	public ClockBlockTileEntity() {
		set_repeat = Integer.MAX_VALUE;
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
    }
    
    private void readNBT_do(NBTTagCompound compound) {
        set_repeat = compound.getInteger("init_repeat");
        set_speed = compound.getInteger("init_speed");
        set_time = compound.getInteger("init_time");
        
        active = compound.getBoolean("active");
        repeat = compound.getInteger("repeat");
        speed = compound.getInteger("speed");
        time = compound.getInteger("time");
	}
    
    @Override
    public void onDataPacket(net.minecraft.network.NetworkManager net, net.minecraft.network.play.server.S35PacketUpdateTileEntity pkt)
    {
    	NBTTagCompound comp = pkt.getNbtCompound();
    	readNBT_do(comp);;
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
		active = false;
    }
    
    public void clockStop() {
		repeat = 0;
		speed = 0;
		time = 0;
		active = false;
    }
    
    public void clockTick() {
    	// System.out.println("Clock Tick @ " + this.getPos() + " -> " + System.currentTimeMillis());
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
    
	public String getStateAsString() {
		return "[" + active + " | " + time + ", " + repeat + ", " + speed + "]";
	}
    
}
