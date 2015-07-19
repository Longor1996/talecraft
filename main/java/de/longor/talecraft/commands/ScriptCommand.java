package de.longor.talecraft.commands;

import java.util.List;

import de.longor.talecraft.TaleCraft;
import de.longor.talecraft.invoke.CommandSenderInvokeSource;
import de.longor.talecraft.invoke.FileScriptInvoke;
import de.longor.talecraft.invoke.Invoke;
import de.longor.talecraft.network.StringNBTCommand;
import de.longor.talecraft.util.PlayerHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class ScriptCommand extends CommandBase {
	
	@Override
	public String getName() {
		return "tc_script";
	}
	
	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "<[?]> (use tab completion)";
	}
	
	@Override
	public void execute(ICommandSender sender, String[] args) throws CommandException {
		if(args.length == 0) {
			throw new WrongUsageException("Not enough parameters for meaningful action. ("+args.length+")");
		}
		
		if(sender.getCommandSenderEntity() == null) {
			throw new WrongUsageException("ICommandSender does not have a entity assigned! Bug?");
		}
		
		if(!(sender.getCommandSenderEntity() instanceof EntityPlayerMP)) {
			throw new WrongUsageException("This command can only be executed by a opped player.");
		}
		
		EntityPlayerMP player = (EntityPlayerMP) sender.getCommandSenderEntity();
		
		if(!PlayerHelper.isOp(player)) {
			throw new WrongUsageException("This command can only be executed by a opped player.");
		}
		
		if(args[0].equals("run")) {
			if(args.length == 2) {
				// Runs a script
				String script = args[1];
				
				if(script != null && !script.isEmpty()) {
					Invoke.invoke(new FileScriptInvoke(script), new CommandSenderInvokeSource(sender));
				}
			} else {
				throw new WrongUsageException("Wrong parameter count: /tc_script run <scriptname>");
			}
		}
		
		if(args[0].equals("edit")) {
			if(args.length == 2) {
				// Get Script name
				String fileName = args[1];
				
				// Load Script
				String fileContent = TaleCraft.globalScriptManager.loadScript(sender.getEntityWorld(), fileName);
				
				// Send Command-Packet with script to sender!
				NBTTagCompound nbt = new NBTTagCompound();
				nbt.setString("scriptname", fileName);
				nbt.setString("script", fileContent);
				TaleCraft.network.sendTo(new StringNBTCommand("script_edit", nbt), player);
			} else {
				throw new WrongUsageException("Wrong parameter count: /tc_script edit <scriptname>");
			}
		}
		
	}
	
    public List addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
    	if(args.length == 0) {
    		return getListOfStringsMatchingLastWord(args, new String[]{"run","edit"});
    	}
    	
    	return null;
    }
    
    public int getRequiredPermissionLevel() {
        return 2;
    }

}
