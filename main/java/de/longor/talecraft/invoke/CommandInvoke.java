package de.longor.talecraft.invoke;

import net.minecraft.nbt.NBTTagCompound;

public class CommandInvoke implements IInvoke {
	private String command;
	
	@Override
	public String getType() {
		return "CommandInvoke";
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
