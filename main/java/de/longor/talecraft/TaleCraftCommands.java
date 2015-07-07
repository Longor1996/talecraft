package de.longor.talecraft;

import java.util.ArrayList;
import java.util.List;

import de.longor.talecraft.commands.ButcherCommand;
import de.longor.talecraft.commands.MountCommand;
import de.longor.talecraft.commands.RegionTriggerCommand;
import de.longor.talecraft.commands.ScriptCommand;
import de.longor.talecraft.commands.SwitchShaderCommand;
import de.longor.talecraft.commands.TriggerCommand;
import de.longor.talecraft.commands.VelocityCommand;
import de.longor.talecraft.commands.VoxelBrushCommand;
import de.longor.talecraft.commands.WandCommand;
import net.minecraft.command.CommandHandler;
import net.minecraft.command.ICommand;

public class TaleCraftCommands {
	private static final List<ICommand> commands = new ArrayList<ICommand>();
	
	public static void init() {
		// just add commands here and they automagically get registered!
		
		commands.add(new WandCommand());
		commands.add(new MountCommand());
		commands.add(new TriggerCommand());
		commands.add(new RegionTriggerCommand());
		commands.add(new VelocityCommand());
		// commands.add(new SwitchShaderCommand());
		commands.add(new VoxelBrushCommand());
		commands.add(new ButcherCommand());
		commands.add(new ScriptCommand());
		
	}
	
	public static void register(CommandHandler registry) {
		for(ICommand cmd : commands) {
			registry.registerCommand(cmd);
		}
	}

}
