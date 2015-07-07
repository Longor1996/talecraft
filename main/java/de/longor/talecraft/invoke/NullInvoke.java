package de.longor.talecraft.invoke;

import net.minecraft.nbt.NBTTagCompound;

public class NullInvoke implements IInvoke {
	public static final String TYPE = "NullInvoke";
	public static final NullInvoke instance = new NullInvoke();
	
	private NullInvoke() {
		// private stub constructor
	}
	
	@Override
	public String getType() {
		return TYPE;
	}
	
	@Override
	public void writeToNBT(NBTTagCompound compound) {
		
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		
	}
	
}
