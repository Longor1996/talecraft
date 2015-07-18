package de.longor.talecraft.server;

import de.longor.talecraft.util.NBTHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;


public class PlayerMirror {
	public EntityPlayerMP player;
	public NBTTagCompound settings;
	
	public PlayerMirror(EntityPlayerMP player) {
		this.player = player;
		this.settings = new NBTTagCompound();
	}
	
	public void construct(NBTTagCompound data) {
		
	}
	
	public void updateSettings(NBTTagCompound data) {
		settings.merge(data);
	}
	
}
