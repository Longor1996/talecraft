package de.longor.talecraft.invoke;

import net.minecraft.nbt.NBTTagCompound;

public class CommandInvoke implements IInvoke {
	public static final String TYPE = "CommandInvoke";
	private String command;
	
	@Override
	public String getType() {
		return TYPE;
	}
	
	public String getCommand() {
		return command;
	}
	
	public void setCommand(String command) {
		this.command = command;
	}
	
	@Override
	public void writeToNBT(NBTTagCompound compound) {
		compound.setString("command", command);
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		command = compound.getString("command");
	}

}
