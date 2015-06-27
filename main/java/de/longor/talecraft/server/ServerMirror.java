package de.longor.talecraft.server;

import java.util.HashMap;
import java.util.Map;

import de.longor.talecraft.TaleCraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

public class ServerMirror {
	private MinecraftServer server;
	private PlayerList players;
	
	public MinecraftServer getServer() {
		return server;
	}
	
	public void create(MinecraftServer server) {
		TaleCraft.logger.info("!!! Creating Server Mirror: " + server);
		
		this.server = server;
		this.players = new PlayerList();
	}
	
	public void destroy() {
		TaleCraft.logger.info("!!! Destroying Server Mirror: " + server);
		this.players.destroy();
	}
	
	public PlayerList playerList() {
		return players;
	}
	
}
