package de.longor.talecraft.server;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;


public class PlayerMirror {
	public PlayerClipboard clipboard;
	public EntityPlayerMP player;
	
	public PlayerMirror(EntityPlayerMP player) {
		this.player = player;
		this.clipboard = null;
	}
	
	public void setClipboard(PlayerClipboard clipboard) {
		this.clipboard = clipboard;
	}
	
	public void setClipboard(IBlockState[] region, int w, int h, int l) {
		this.clipboard = new PlayerClipboard(region,w,h,l);
	}
	
}
