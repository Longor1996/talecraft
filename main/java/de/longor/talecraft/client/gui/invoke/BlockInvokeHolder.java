package de.longor.talecraft.client.gui.invoke;

import de.longor.talecraft.TaleCraft;
import de.longor.talecraft.client.gui.blocks.GuiScriptBlock;
import de.longor.talecraft.network.StringNBTCommand;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;

public class BlockInvokeHolder implements IInvokeHolder {
	BlockPos blockPosition;
	String invokeName;
	
	public BlockInvokeHolder(BlockPos position, String invokeName) {
		this.blockPosition = position;
		this.invokeName = invokeName;
	}
	
	@Override
	public void sendInvokeUpdate(NBTTagCompound newInvokeData) {
		String commandString = "blockdatamerge:"+blockPosition.getX() + " " + blockPosition.getY() + " " + blockPosition.getZ();
		NBTTagCompound commandData = new NBTTagCompound();
		commandData.setTag(invokeName, newInvokeData);
		TaleCraft.instance.network.sendToServer(new StringNBTCommand(commandString, commandData));
	}
	
	@Override
	public void sendCommand(String command, NBTTagCompound commandData) {
		String commandString = "blockcommand:"+blockPosition.getX() + " " + blockPosition.getY() + " " + blockPosition.getZ();
		
		if(commandData == null)
			commandData = new NBTTagCompound();
		commandData.setString("command", command);
		
		// Send command
		TaleCraft.instance.network.sendToServer(new StringNBTCommand(commandString, commandData));
		
		// close whatever gui is open
		Minecraft.getMinecraft().displayGuiScreen(null);
	}
	
	@Override
	public void switchInvokeType(String type) {
		String commandString = "blockdatamerge:"+blockPosition.getX() + " " + blockPosition.getY() + " " + blockPosition.getZ();
		NBTTagCompound commandData = new NBTTagCompound();
		NBTTagCompound invokeData = new NBTTagCompound();
		invokeData.setString("type", type);
		commandData.setTag(invokeName, invokeData);
		TaleCraft.instance.network.sendToServer(new StringNBTCommand(commandString, commandData));
	}
	
}
