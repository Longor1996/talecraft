package de.longor.talecraft.invoke;

import java.util.Arrays;

import net.minecraft.nbt.NBTTagCompound;

public class BlockTriggerInvoke implements IInvoke {
	public static final String TYPE = "BlockTriggerInvoke";
	int[] bounds;
	int data;
	
	public int[] getBounds() {
		return bounds;
	}
	
	public int getTriggerData() {
		return data;
	}
	
	@Override
	public String getType() {
		return TYPE;
	}
	
	@Override
	public void writeToNBT(NBTTagCompound compound) {
		compound.setIntArray("bounds", bounds);
		compound.setInteger("data", data);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		data = compound.getInteger("data");
		bounds = compound.getIntArray("bounds");
		
		if(bounds == null)
			bounds = new int[6];
		else if(bounds.length != 6)
			bounds = Arrays.copyOf(bounds, 6);
	}
	
}
