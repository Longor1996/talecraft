package de.longor.talecraft.invoke;

import de.longor.talecraft.TaleCraft;
import net.minecraft.nbt.NBTTagCompound;

public interface IInvoke {
	
	public String getType();
	
	public void writeToNBT(NBTTagCompound compound);
	public void readFromNBT(NBTTagCompound compound);
	
	public static class Serializer {
		
		public static final NBTTagCompound write(IInvoke invoke) {
			NBTTagCompound compound = new NBTTagCompound();
			compound.setString("type", invoke.getType());
			invoke.writeToNBT(compound);
			return compound;
		}
		
		public static final IInvoke read(NBTTagCompound compoundTag) {
			String type = compoundTag.getString("type");
			
			if(type == null || type.isEmpty())
				return NullInvoke.instance;
			
			if("NullInvoke".equals(type)) {
				return NullInvoke.instance;
			}
			
			if("BlockTriggerInvoke".equals(type)) {
				BlockTriggerInvoke invoke = new BlockTriggerInvoke();
				invoke.readFromNBT(compoundTag);
				return invoke;
			}
			
			TaleCraft.logger.error("(FATAL) Corrupted or unknown Invoke Data found: " + compoundTag + ".");
			return NullInvoke.instance;
		}
		
	}
	
}
