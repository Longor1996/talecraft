package de.longor.talecraft.util;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;

public class NBTHelper {
	
	public static final NBTTagCompound getOrCreate(NBTTagCompound parent, String name) {
		if(parent.hasKey(name, parent.getId())) {
			return parent.getCompoundTag(name);
		} else {
			NBTTagCompound compound = new NBTTagCompound();
			parent.setTag(name, compound);
			return compound;
		}
	}

	public static NBTTagCompound getOrNull(NBTTagCompound parent, String name) {
		if(parent.hasKey(name, parent.getId())) {
			return parent.getCompoundTag(name);
		} else {
			return null;
		}
	}
	
}
