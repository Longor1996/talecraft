package de.longor.talecraft;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.command.CommandHandler;
import net.minecraft.command.ICommand;

public class TaleCraftCommands {
	private static final List<ICommand> commands = new ArrayList<ICommand>();
	
	public static void init() {
		// just add commands here and they automagically get registered!
	}
	
	public static void register(CommandHandler registry) {
		for(ICommand cmd : commands) {
			registry.registerCommand(cmd);
		}
	}

}
