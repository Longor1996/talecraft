package de.longor.talecraft.blocks.util.tileentity;

import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;

import com.google.gson.JsonParseException;

import de.longor.talecraft.blocks.TCTileEntity;
import de.longor.talecraft.invoke.IInvoke;
import net.minecraft.command.PlayerSelector;
import net.minecraft.command.SyntaxErrorException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentProcessor;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

public class MessageBlockTileEntity extends TCTileEntity {
    private String playerSelector;
    private String message;
    private boolean tellraw;
    
    public MessageBlockTileEntity() {
    	playerSelector = "@a";
    	message = "Hi";
    	tellraw = false;
    }
	
	@Override
	public void commandReceived(String command, NBTTagCompound data) {
		if(command.equals("trigger")) {
			trigger();
			return;
		}
		
		// fall trough
		super.commandReceived(command, data);
	}
	
	private void trigger() {
		if(this.worldObj.isRemote) {
			return;
		}
		
		if(message == null || message.isEmpty()) {
			return;
		}
		
		IChatComponent[] chatComponent = null;
		
		// compose chat message depending on the TELLRAW flag
		if(tellraw) {
			
            try {
            	chatComponent = new IChatComponent[]{IChatComponent.Serializer.jsonToComponent(message)};
            } catch (JsonParseException jsonparseexception) {
                Throwable throwable = ExceptionUtils.getRootCause(jsonparseexception);
                jsonparseexception.printStackTrace();
                throwable.printStackTrace();
                return;
            }
		} else {
			if(message.contains("\n")) {
				String[] lines = message.split("\n");
				chatComponent = new IChatComponent[lines.length];
				
				for(int i = 0; i < lines.length; i++) {
					chatComponent[i] = new ChatComponentText(lines[i]);
				}
			} else {
				chatComponent = new IChatComponent[]{new ChatComponentText(message)};
			}
		}
		
		List<EntityPlayer> players = PlayerSelector.matchEntities(this, playerSelector, EntityPlayer.class);
		
		if(players == null) {
			return;
		}
		
		if(players.isEmpty()) {
			return;
		}
		
		// SEND THE MESSAGE(S) TO ALL PLAYERS
		for(EntityPlayer player : players) {
			for(IChatComponent component : chatComponent) {
				player.addChatMessage(component);
			}
		}
	}
	
	@Override
	public void getInvokes(List<IInvoke> invokes) {
		// none
	}
	
	@Override
	public void getInvokeColor(float[] color) {
		color[0] = 0.90f;
		color[1] = 0.85f;
		color[2] = 0.50f;
	}
	
	@Override
	public String getName() {
		return "MessageBlock@"+this.getPos();
	}
	
	@Override
	public void readFromNBT_do(NBTTagCompound comp) {
		this.playerSelector = comp.getString("playerSelector");
		this.message = comp.getString("message");
		this.tellraw = comp.getBoolean("tellraw");
	}
	
	@Override
	public void writeToNBT_do(NBTTagCompound comp) {
		comp.setString("playerSelector", playerSelector);
		comp.setString("message", message);
		comp.setBoolean("tellraw", tellraw);
	}
	
	public String getPlayerSelector() {
		return playerSelector;
	}
	
	public String getMessage() {
		return message;
	}
	
	public boolean getTellRaw() {
		return tellraw;
	}
	
}
