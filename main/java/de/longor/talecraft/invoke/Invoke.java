package de.longor.talecraft.invoke;

import de.longor.talecraft.TaleCraft;
import net.minecraft.nbt.NBTTagCompound;

public class Invoke {
	
	public static final void invoke(NBTTagCompound invokeData, IInvokeSource source) {
		String type = invokeData.getString("type");
		
		if(type.length() <= 0) {
			TaleCraft.logger.error("Uh oh, a invoke without a type-tag from "+source+"! :: " + invokeData);
			return;
		}
		
		if(type.equals("command")) {
			
		}
		
	}
	
}
