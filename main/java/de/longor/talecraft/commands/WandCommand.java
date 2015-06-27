package de.longor.talecraft.commands;

import java.util.List;

import de.longor.talecraft.TaleCraft;
import de.longor.talecraft.invoke.Invoke;
import de.longor.talecraft.items.WandItem;
import de.longor.talecraft.util.WorldHelper;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class WandCommand extends CommandBase {
	
	@Override
	public String getName() {
		return "tc_wand";
	}
	
	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "? (Use tab-completion!)";
	}
	
	@Override
	public void execute(ICommandSender sender, String[] args) throws CommandException {
		EntityPlayerMP player = this.getCommandSenderAsPlayer(sender);
		
		if(args.length == 0) {
			throw new WrongUsageException("No parameters given!");
		}
		
		if(args[0].equals(".")) {
			BlockPos position = player.getPosition();
			
			int ix = position.getX();
			int iy = position.getY();
			int iz = position.getZ();
			int ax = position.getX();
			int ay = position.getY();
			int az = position.getZ();
			
			WandItem.setBounds(player, ix, iy, iz, ax, ay, az);
			return;
		}
		
		if(args[0].equals(".o")) {
			WandItem.setBounds(player, 0, 0, 0, 0, 0, 0);
			return;
		}
		
		if(args[0].equals("expand")) {
			if(args.length == 2) {
				int value = this.parseInt(args[1]);
				
				int[] bounds = WandItem.getBoundsFromPLAYERorNULL(player);
				
				if(bounds == null) {
					throw new WrongUsageException("You didn't select a region with your wand. /tc_wand expand <val>");
				}
				
				int ix = bounds[0] - value;
				int iy = bounds[1] - value;
				int iz = bounds[2] - value;
				int ax = bounds[3] + value;
				int ay = bounds[4] + value;
				int az = bounds[5] + value;
				
				WandItem.setBounds(player, ix, iy, iz, ax, ay, az);
				return;
			} else {
				throw new WrongUsageException("Missing parameters! /tc_wand expand <val>");
			}
		}
		
		if(args[0].equals("expand_h")) {
			if(args.length == 2) {
				int value = this.parseInt(args[1]);
				
				int[] bounds = WandItem.getBoundsFromPLAYERorNULL(player);
				
				if(bounds == null) {
					throw new WrongUsageException("You didn't select a region with your wand. /tc_wand expand <val>");
				}
				
				int ix = bounds[0] - value;
				int iy = bounds[1];
				int iz = bounds[2] - value;
				int ax = bounds[3] + value;
				int ay = bounds[4];
				int az = bounds[5] + value;
				
				WandItem.setBounds(player, ix, iy, iz, ax, ay, az);
				return;
			} else {
				throw new WrongUsageException("Missing parameters! /tc_wand expand <val>");
			}
		}
		
		if(args[0].equals("expand_v")) {
			if(args.length == 2) {
				int value = this.parseInt(args[1]);
				
				int[] bounds = WandItem.getBoundsFromPLAYERorNULL(player);
				
				if(bounds == null) {
					throw new WrongUsageException("You didn't select a region with your wand. /tc_wand expand <val>");
				}
				
				int ix = bounds[0];
				int iy = bounds[1] - value;
				int iz = bounds[2];
				int ax = bounds[3];
				int ay = bounds[4] + value;
				int az = bounds[5];
				
				WandItem.setBounds(player, ix, iy, iz, ax, ay, az);
				return;
			} else {
				throw new WrongUsageException("Missing parameters! /tc_wand expand <val>");
			}
		}
		
		if(args[0].equals("region")) {
			if(args.length >= 2) {
				
				if(args[1].equals("erase")) {
					int[] bounds = WandItem.getBoundsFromPLAYERorNULL(player);
					WorldHelper.fill(player.worldObj, bounds, Blocks.air.getDefaultState());
					return;
				}
				
				if(args[1].equals("fill")) {
					
					if(args.length >= 3) {
						TaleCraft.logger.info("BEEP-M");
						
						String blockID = args[2];
						String blockMeta = "0";
						Block block = (Block) Block.blockRegistry.getObject(new ResourceLocation(blockID));
						
						if(block == null) {
							throw new CommandException("Invalid block ID: " + blockID, blockID);
						}
						
						if(args.length == 4) {
							blockMeta = args[3];
						}
						
						IBlockState blockState = block.getStateFromMeta(this.parseInt(blockMeta, 0, 15));
						
						int[] bounds = WandItem.getBoundsFromPLAYERorNULL(player);
						WorldHelper.fill(player.worldObj, bounds, blockState);
						return;
					} else {
						throw new WrongUsageException("Missing parameters! /tc_wand region fill <block>");
					}
				}
				
			} else {
				throw new WrongUsageException("Missing parameters! /tc_wand region ...?");
			}
		}
		
	}
	
    public List addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
    	if(args.length <= 1) {
    		return getListOfStringsMatchingLastWord(args, new String[] {"expand", "expand_h", "region", "."});
    	}
    	
    	if(args.length == 2 && args[0].equals("region")) {
    		return getListOfStringsMatchingLastWord(args, new String[] {"erase", "fill"});
    	}
    	
    	if(args.length == 3 && args[0].equals("region") && args[1].equals("fill")) {
    		return func_175762_a(args, Block.blockRegistry.getKeys());
    	}
    	
    	if(args.length == 4 && args[0].equals("region") && args[1].equals("fill")) {
    		return getListOfStringsMatchingLastWord(args, new String[]{"0","1","2","3","4","5","6","7","8","9","10","11","12","13","14","15"});
    	}
    	
    	return null;
    }
    
    public int getRequiredPermissionLevel()
    {
        return 2;
    }
	
}
