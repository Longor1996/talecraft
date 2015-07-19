package de.longor.talecraft.server;

import java.util.HashMap;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import de.longor.talecraft.TaleCraft;
import de.longor.talecraft.blocks.TCIBlockCommandReceiver;
import de.longor.talecraft.network.PlayerNBTDataMerge;
import de.longor.talecraft.network.StringNBTCommand;
import de.longor.talecraft.util.PlayerHelper;

public class ServerHandler {
	
	public static void handleEntityJoin(World world, Entity entity) {
		// If this is a player, send the player the persistent EntityData.
		if(entity instanceof EntityPlayerMP) {
			TaleCraft.network.sendTo(new PlayerNBTDataMerge(entity.getEntityData()), (EntityPlayerMP) entity);
			// PlayerList.playerJoin((EntityPlayerMP)entity);
		}
	}
	
	public static void handleSNBTCommand(NetHandlerPlayServer serverHandler, StringNBTCommand cmd) {
		EntityPlayerMP playerEntity = serverHandler.playerEntity;
		WorldServer worldServer = playerEntity.getServerForPlayer();
		handleSNBTCommand(playerEntity, worldServer, cmd);
	}
	
	/** This method actually handles the SNBT-command. **/
	private static void handleSNBTCommand(EntityPlayerMP player, World world, StringNBTCommand commandPacket) {
		if(world.isRemote){
			TaleCraft.logger.error("FATAL ERROR: ServerHandler method was called on client-side!");
			return;
		}
		
		if(commandPacket.command.equals("join acknowledged")) {
			TaleCraft.logger.info("join acknowledged : " + commandPacket.data);
			getServerMirror(null).playerList().getPlayer(player).construct(commandPacket.data);
			return;
		}
		
		if(commandPacket.command.equals("update settings")) {
			TaleCraft.logger.info("updating settings " + commandPacket.data);
			getServerMirror(null).playerList().getPlayer(player).updateSettings(commandPacket.data);
			return;
		}
		
		if(commandPacket.command.startsWith("blockdatamerge:")) {
			if(!PlayerHelper.isOp(player)) {
				player.addChatMessage(new ChatComponentText("Error: 'blockdatamerge' is a operator only command."));
				return;
			}
			
			String positionString = commandPacket.command.substring(15);
			String[] posStrings = positionString.split(" ");
			BlockPos position = new BlockPos(Integer.valueOf(posStrings[0]), Integer.valueOf(posStrings[1]), Integer.valueOf(posStrings[2]));
			
			TileEntity entity = world.getTileEntity(position);
			
			if(entity != null) {
				TaleCraft.logger.info("(datamerge) " + position + " -> " + commandPacket.data);
				mergeTileEntityData(entity, commandPacket.data);
			} else {
				player.addChatMessage(new ChatComponentText("Error: Failed to merge block data: TileEntity does not exist."));
				return;
			}
		}
		
		if(commandPacket.command.startsWith("blockcommand:")) {
			if(!PlayerHelper.isOp(player)) {
				player.addChatMessage(new ChatComponentText("Error: 'blockcommand' is a operator only command."));
				return;
			}
			
			String positionString = commandPacket.command.substring(13);
			String[] posStrings = positionString.split(" ");
			BlockPos position = new BlockPos(Integer.valueOf(posStrings[0]), Integer.valueOf(posStrings[1]), Integer.valueOf(posStrings[2]));
			
			TileEntity entity = world.getTileEntity(position);
			
			if(entity != null) {
				TaleCraft.logger.info("(block command) " + position + " -> " + commandPacket.data);
				
				if(entity instanceof TCIBlockCommandReceiver) {
					((TCIBlockCommandReceiver) entity).commandReceived(commandPacket.data.getString("command"), commandPacket.data);
				}
			} else {
				player.addChatMessage(new ChatComponentText("Error: Failed to merge block data: TileEntity does not exist."));
				return;
			}
		}
		
		TaleCraft.logger.error("Received unknown StringNBTCommand from client: "+commandPacket.command+" : "+commandPacket.data);
	}
	
	/** Merges the given {@link NBTTagCompound} into the given {@link TileEntity} data. **/
	private static void mergeTileEntityData(TileEntity entity, NBTTagCompound data) {
		BlockPos blockpos = entity.getPos();
		NBTTagCompound compound = new NBTTagCompound();
		entity.writeToNBT(compound);
		
		compound.merge(data);
		compound.setInteger("x", blockpos.getX());
		compound.setInteger("y", blockpos.getY());
		compound.setInteger("z", blockpos.getZ());
		
		entity.readFromNBT(compound);
		entity.markDirty();
		entity.getWorld().markBlockForUpdate(blockpos);
	}
	
	private static HashMap<MinecraftServer, ServerMirror> serverMirrorsMap = new HashMap<MinecraftServer, ServerMirror>();
	public static ServerMirror getServerMirror(MinecraftServer server) {
		if(server == null) {
			server = MinecraftServer.getServer();
		}
		
		ServerMirror mirror = serverMirrorsMap.get(server);
		
		if(mirror == null) {
			mirror = new ServerMirror();
			mirror.create(server);
			serverMirrorsMap.put(server, mirror);
		}
		
		return mirror;
	}
	
	public static void destroyServerMirror(MinecraftServer server) {
		if(server == null) {
			for(ServerMirror mirror : serverMirrorsMap.values()) {
				mirror.destroy();
			}
			serverMirrorsMap.clear();
			return;
		}
		
		ServerMirror mirror = serverMirrorsMap.remove(server);
		
		if(mirror != null) {
			mirror.destroy();
		}
	}
	
}
