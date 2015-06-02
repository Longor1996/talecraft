package de.longor.talecraft.commands;

import java.util.List;

import de.longor.talecraft.TaleCraft;
import de.longor.talecraft.invoke.Invoke;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class TickCommand extends CommandBase {
	
	@Override
	public String getName() {
		return "tc_tickblock";
	}
	
	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "<x> <y> <z>";
	}
	
	@Override
	public void execute(ICommandSender sender, String[] args) throws CommandException {
		if(args.length != 3) {
			throw new CommandException("Wrong number of parameters: " + args.length + " given, 3 needed.");
		}
		
		BlockPos originPos = sender.getPosition();
		
		CoordinateArg posX = this.func_175770_a(originPos.getX(), args[0], false);
		CoordinateArg posY = this.func_175770_a(originPos.getY(), args[1], false);
		CoordinateArg posZ = this.func_175770_a(originPos.getZ(), args[2], false);
		BlockPos triggerPos = new BlockPos(posX.func_179628_a(), posY.func_179628_a(), posZ.func_179628_a());
		
		World world = sender.getEntityWorld();
		IBlockState state = world.getBlockState(triggerPos);
		Block block = state.getBlock();
		
		block.updateTick(world, triggerPos, state, TaleCraft.random);
	}
	
    public List addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos)
    {
    	if(args.length == 0) {
    		return getListOfStringsMatchingLastWord(args, new String[]{"~","0"});
    	}
    	if(args.length == 1) {
    		return getListOfStringsMatchingLastWord(args, new String[]{"~","0"});
    	}
    	if(args.length == 2) {
    		return getListOfStringsMatchingLastWord(args, new String[]{"~","0"});
    	}
    	
    	return null;
    }

    public int getRequiredPermissionLevel()
    {
        return 2;
    }
	
}
