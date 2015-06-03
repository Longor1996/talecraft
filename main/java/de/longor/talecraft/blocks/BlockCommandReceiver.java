package de.longor.talecraft.blocks;

import net.minecraft.nbt.NBTTagCompound;

public interface BlockCommandReceiver {
	void commandReceived(String string, NBTTagCompound data);
}
