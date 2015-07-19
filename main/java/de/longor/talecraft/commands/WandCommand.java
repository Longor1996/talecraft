package de.longor.talecraft.commands;

import java.util.List;

import de.longor.talecraft.TaleCraft;
import de.longor.talecraft.TaleCraftItems;
import de.longor.talecraft.clipboard.ClipboardItem;
import de.longor.talecraft.invoke.CommandSenderInvokeSource;
import de.longor.talecraft.invoke.Invoke;
import de.longor.talecraft.items.CopyItem;
import de.longor.talecraft.items.WandItem;
import de.longor.talecraft.network.StringNBTCommand;
import de.longor.talecraft.server.ServerMirror;
import de.longor.talecraft.util.GObjectTypeHelper;
import de.longor.talecraft.util.WorldHelper;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.EnumFacing;
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
		
		if(args[0].equals("..")) {
			BlockPos position = player.getPosition();
			
			int ix = position.getX();
			int iy = position.getY();
			int iz = position.getZ();
			int ax = position.getX();
			int ay = position.getY()+1;
			int az = position.getZ();
			
			WandItem.setBounds(player, ix, iy, iz, ax, ay, az);
			return;
		}
		
		if(args[0].equals(".o")) {
			WandItem.setBounds(player, 0, 0, 0, 0, 0, 0);
			return;
		}
		
		if(args[0].equals(".c")) {
			BlockPos position = player.getPosition();
			
			int ix = player.chunkCoordX * 16;
			int iy = 0;
			int iz = player.chunkCoordZ * 16;
			int ax = player.chunkCoordX * 16 + 16;
			int ay = 255;
			int az = player.chunkCoordZ * 16 + 16;
			
			WandItem.setBounds(player, ix, iy, iz, ax, ay, az);
			return;
		}
		
		if(args[0].equals("set")) {
			if(args.length == 3) {
				int[] bounds = WandItem.getBoundsFromPLAYERorNULL(player);
				
				if(bounds == null) {
					throw new WrongUsageException("You didn't select a region with your wand. /tc_wand expand <val>");
				}
				
				String varStr = args[1];
				String valueStr = args[2];
				
				int ix = bounds[0];
				int iy = bounds[1];
				int iz = bounds[2];
				int ax = bounds[3];
				int ay = bounds[4];
				int az = bounds[5];
				
				if(varStr.equalsIgnoreCase("x0")) {
					ix = (int) this.func_175761_b(sender.getPosition().getX(), varStr, false);
				} else if(varStr.equalsIgnoreCase("y0")) {
					iy = (int) this.func_175761_b(sender.getPosition().getY(), varStr, false);
				} else if(varStr.equalsIgnoreCase("z0")) {
					iz = (int) this.func_175761_b(sender.getPosition().getZ(), varStr, false);
				} else if(varStr.equalsIgnoreCase("x1")) {
					ax = (int) this.func_175761_b(sender.getPosition().getX(), varStr, false);
				} else if(varStr.equalsIgnoreCase("y1")) {
					ay = (int) this.func_175761_b(sender.getPosition().getY(), varStr, false);
				} else if(varStr.equalsIgnoreCase("z1")) {
					az = (int) this.func_175761_b(sender.getPosition().getZ(), varStr, false);
				} else {
					throw new WrongUsageException("Wrong parameter 'bound'! /tc_wand set <bound> <value>");
				}
				
				WandItem.setBounds(player, ix, iy, iz, ax, ay, az);
			} else if(args.length == 7) {
				int[] bounds = WandItem.getBoundsFromPLAYERorNULL(player);
				
				if(bounds == null) {
					throw new WrongUsageException("You didn't select a region with your wand. /tc_wand expand <val>");
				}
				
				int ix = (int) this.func_175761_b(sender.getPosition().getX(), args[1], false);//bounds[0];
				int iy = (int) this.func_175761_b(sender.getPosition().getY(), args[2], false);;//bounds[1];
				int iz = (int) this.func_175761_b(sender.getPosition().getZ(), args[3], false);;//bounds[2];
				int ax = (int) this.func_175761_b(sender.getPosition().getX(), args[4], false);;//bounds[3];
				int ay = (int) this.func_175761_b(sender.getPosition().getY(), args[5], false);;//bounds[4];
				int az = (int) this.func_175761_b(sender.getPosition().getZ(), args[6], false);;//bounds[5];
				
				WandItem.setBounds(player, ix, iy, iz, ax, ay, az);
			} else {
				throw new WrongUsageException("Missing parameters! /tc_wand set < ? >");
			}
		}
		
		if(args[0].equals("expand_to") || args[0].equals("exto")) {
			if(args.length == 4) {
				int[] bounds = WandItem.getBoundsFromPLAYERorNULL(player);
				
				if(bounds == null) {
					throw new WrongUsageException("You didn't select a region with your wand. /tc_wand expand <val>");
				}
				
				int posX = (int) this.func_175761_b(sender.getPosition().getX(), args[1], false);
				int posY = (int) this.func_175761_b(sender.getPosition().getY(), args[2], false);
				int posZ = (int) this.func_175761_b(sender.getPosition().getZ(), args[3], false);
				
				int ix = bounds[0];
				int iy = bounds[1];
				int iz = bounds[2];
				int ax = bounds[3];
				int ay = bounds[4];
				int az = bounds[5];
				
				if(ix > posX) ix = posX;
				if(iy > posY) iy = posY;
				if(iz > posZ) iz = posZ;
				if(ax < posX) ax = posX;
				if(ay < posY) ay = posY;
				if(az < posZ) az = posZ;
				
				WandItem.setBounds(player, ix, iy, iz, ax, ay, az);
			} else {
				throw new WrongUsageException("Missing parameters! /tc_wand expand_to <x> <y> <z>");
			}
			
			
		}
		
		if(args[0].equals("expand_into") || args[0].equals("exito")) {
			if(args.length == 3) {
				int[] bounds = WandItem.getBoundsFromPLAYERorNULL(player);
				
				if(bounds == null) {
					throw new WrongUsageException("You didn't select a region with your wand. /tc_wand expand <val>");
				}
				
				int ix = bounds[0];
				int iy = bounds[1];
				int iz = bounds[2];
				int ax = bounds[3];
				int ay = bounds[4];
				int az = bounds[5];
				
				int direction = -1;
				
				if(args[1].equalsIgnoreCase("east")  || args[1].equalsIgnoreCase("+x") || args[1].equalsIgnoreCase("x")) direction = 3; // +x
				if(args[1].equalsIgnoreCase("up")    || args[1].equalsIgnoreCase("+y") || args[1].equalsIgnoreCase("y")) direction = 0; // +y
				if(args[1].equalsIgnoreCase("south") || args[1].equalsIgnoreCase("+z") || args[1].equalsIgnoreCase("z")) direction = 4; // +z
				
				if(args[1].equalsIgnoreCase("down")  || args[1].equalsIgnoreCase("-y")) direction = 1; // -y
				if(args[1].equalsIgnoreCase("north") || args[1].equalsIgnoreCase("-z")) direction = 2; // -z
				if(args[1].equalsIgnoreCase("west")  || args[1].equalsIgnoreCase("-x")) direction = 5; // -x
				
				if(args[1].equalsIgnoreCase("me")) {
		        	EnumFacing directionSky = player.getHorizontalFacing();
		        	EnumFacing directionFull = null;
		        	
		    		if(player.rotationPitch > 45) {
		    			directionFull = EnumFacing.DOWN;
		    		} else if(player.rotationPitch < -45) {
		    			directionFull = EnumFacing.UP;
		    		} else {
		    			directionFull = player.getHorizontalFacing();
		    		}
					
		    		switch(directionFull) {
		    		case UP:    direction = 0; break; // +y
		    		case DOWN:  direction = 1; break; // -y
		    		case NORTH: direction = 2; break; // -z
		    		case EAST:  direction = 3; break; // +x
		    		case SOUTH: direction = 4; break; // +z
		    		case WEST:  direction = 5; break; // -x
		    		default: throw new WrongUsageException("WHAT THE FUCK?!");
		    		}
				}
				
				int value = this.parseInt(args[2], 1, 128);
				
				switch (direction) {
				case 0: ay += value; break; // +y
				case 1: iy -= value; break; // -y
				case 2: iz -= value; break; // -z
				case 3: ax += value; break; // +x
				case 4: az += value; break; // +z
				case 5: ix -= value; break; // -x
				default: throw new WrongUsageException("Direction unknown: " + args[2]);
				}
				
				WandItem.setBounds(player, ix, iy, iz, ax, ay, az);
			} else {
				throw new WrongUsageException("Missing parameters! /tc_wand expand <direction> <value>");
			}
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
				
				if(args[1].equals("copy")) {
					if(args.length > 2) {
						String name = args[2];
						
						int[] bounds = WandItem.getBoundsFromPLAYERorNULL(player);
						ClipboardItem item = ClipboardItem.copyRegion(bounds, player.worldObj, name, player);
						
						if(item != null) {
							ServerMirror.instance().getClipboard().put(name, item);
							player.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN+"Copied region to clipboard as '"+name+"'!"));
						}
					} else {
						CopyItem copy = TaleCraftItems.copy;
						ItemStack stack = new ItemStack(copy);
						copy.onItemRightClick(stack, player.worldObj, player);
						TaleCraft.network.sendTo(new StringNBTCommand("item.copy.trigger"), player);
					}
					return;
				}
				
				if(args[1].equals("paste")) {
					if(args.length == 2) {
						int[] bounds = WandItem.getBoundsFromPLAYERorNULL(player);
						String name = "player."+player.getGameProfile().getId().toString();
						ClipboardItem item = ServerMirror.instance().getClipboard().get(name);
						
						if(item != null) {
							ClipboardItem.pasteRegion(item, new BlockPos(bounds[0], bounds[1], bounds[2]), player.worldObj, player);
							player.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN+"Copied region to world: '"+name+"'."));
						} else {
							throw new CommandException("There is no record with the name '"+name+"' in the clipboard.");
						}
					}
					
					if(args.length > 2) {
						String name = args[2];
						int[] bounds = WandItem.getBoundsFromPLAYERorNULL(player);
						ClipboardItem item = ServerMirror.instance().getClipboard().get(name);
						
						if(item != null) {
							ClipboardItem.pasteRegion(item, new BlockPos(bounds[0], bounds[1], bounds[2]), player.worldObj, player);
							player.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN+"Copied region to world: '"+name+"'."));
						} else {
							throw new CommandException("There is no record with the name '"+name+"' in the clipboard.");
						}
						
					} else {
						throw new WrongUsageException("Missing parameters! /tc_wand region paste <name>");
					}
					
					return;
				}
				
				if(args[1].equals("trigger")) {
					int[] bounds = WandItem.getBoundsFromPLAYERorNULL(player);
					
					int ix = bounds[0];
					int iy = bounds[1];
					int iz = bounds[2];
					int ax = bounds[3];
					int ay = bounds[4];
					int az = bounds[5];
					
					Invoke.trigger(new CommandSenderInvokeSource(player), ix, iy, iz, ax, ay, az);
					return;
				}
				
				if(args[1].equals("fill")) {
					if(args.length == 3) {
						IBlockState replace = GObjectTypeHelper.findBlockState(args[2]);
						
						if(replace == null) {
							throw new CommandException("Could not find block type: " + args[2]);
						}
						
						int[] bounds = WandItem.getBoundsFromPLAYERorNULL(player);
						WorldHelper.fill(player.worldObj, bounds, replace);
						return;
					} else {
						throw new WrongUsageException("Missing parameters! /tc_wand region fill <block>");
					}
				}
				
				if(args[1].equals("replace")) {
					
					if(args.length == 4) {
						IBlockState replace = GObjectTypeHelper.findBlockState(args[2]);
						IBlockState mask = GObjectTypeHelper.findBlockState(args[3]);
						
						if(replace == null) {
							throw new CommandException("Could not find block type: " + args[2]);
						}
						
						if(mask == null) {
							throw new CommandException("Could not find block type: " + args[3]);
						}
						
						int[] bounds = WandItem.getBoundsFromPLAYERorNULL(player);
						WorldHelper.replace(player.worldObj, bounds, replace, mask);
						return;
					} else {
						throw new WrongUsageException("Missing parameters! /tc_wand region fill <block>");
					}
				}
				
				if(args[1].equals("room")) {
					if(args.length == 4) {
						IBlockState replace = GObjectTypeHelper.findBlockState(args[3]);
						
						if(replace == null) {
							throw new CommandException("Could not find block type: " + args[3]);
						}
						
						boolean f_up = false;
						boolean f_down = false;
						boolean f_north = false;
						boolean f_east = false;
						boolean f_south = false;
						boolean f_west = false;
						
						String flagsStr = args[2];
						
						if(containsCharIgnoreCase(flagsStr, 'U')) f_up = true;
						if(containsCharIgnoreCase(flagsStr, 'D')) f_down = true;
						if(containsCharIgnoreCase(flagsStr, 'N')) f_north = true;
						if(containsCharIgnoreCase(flagsStr, 'E')) f_east = true;
						if(containsCharIgnoreCase(flagsStr, 'S')) f_south = true;
						if(containsCharIgnoreCase(flagsStr, 'W')) f_west = true;
						
						if(containsCharIgnoreCase(flagsStr, 'B')) {
							f_north = true;
							f_east = true;
							f_south = true;
							f_west = true;
						}
						
						if(containsCharIgnoreCase(flagsStr, 'A')) {
							f_up = true;
							f_down = true;
							f_north = true;
							f_east = true;
							f_south = true;
							f_west = true;
						}
						
						int[] bounds = WandItem.getBoundsFromPLAYERorNULL(player);
						
						int ix = bounds[0];
						int iy = bounds[1];
						int iz = bounds[2];
						int ax = bounds[3];
						int ay = bounds[4];
						int az = bounds[5];
						
						if(f_down)  WorldHelper.fill(player.worldObj, ix, iy, iz, ax, iy, az, replace);
						if(f_up)    WorldHelper.fill(player.worldObj, ix, ay, iz, ax, ay, az, replace);
						if(f_north) WorldHelper.fill(player.worldObj, ix, iy, iz, ax, ay, iz, replace);
						if(f_east)  WorldHelper.fill(player.worldObj, ax, iy, iz, ax, ay, az, replace);
						if(f_south) WorldHelper.fill(player.worldObj, ix, iy, az, ax, ay, az, replace);
						if(f_west)  WorldHelper.fill(player.worldObj, ix, iy, iz, ix, ay, az, replace);
						
						// WorldHelper.fill(player.worldObj, bounds, replace);
						return;
					} else {
						throw new WrongUsageException("Missing parameters! /tc_wand region room <UDNESW BA> <block>");
					}
				}
				
			} else {
				throw new WrongUsageException("Missing parameters! /tc_wand region ...?");
			}
		}
		
	}
	
	private boolean containsCharIgnoreCase(String flagsStr, char c) {
		char cLow = Character.toLowerCase(c);
		char cUp  = Character.toUpperCase(c);
		return flagsStr.indexOf(cUp) != -1 || flagsStr.indexOf(cLow) != -1;
	}
	
	public List addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
    	if(args.length <= 1) {
    		return getListOfStringsMatchingLastWord(args, new String[] {
    				"region",
    				"expand",
    				"expand_h",
    				"expand_v",
    				"expand_to",
    				"expand_into",
    				".", "..", ".o", ".c",
    				"set"
    		});
    	}
    	
    	if(args.length >= 2 && args[0].equals("expand_to")) {
    		return getListOfStringsMatchingLastWord(args, new String[] {"~", "0"});
    	}
    	
    	if(args.length == 2 && args[0].equals("expand_into")) {
    		return getListOfStringsMatchingLastWord(args, new String[] {
    				"me",
    				"up","down",
    				"north","east","south","west",
    				"x","y","z",
    				"+x","+y","+z",
    				"-x","-y","-z"
    		});
    	}
    	
    	if(args.length == 2 && args[0].equals("set")) {
    		return getListOfStringsMatchingLastWord(args, new String[] {"x0","y0","z0","x1","y1","z1"});
    	}
    	
    	if(args.length > 2 && args[0].equals("set")) {
    		return getListOfStringsMatchingLastWord(args, new String[] {"~", "0"});
    	}
    	
    	if(args.length == 2 && args[0].equals("region")) {
    		return getListOfStringsMatchingLastWord(args, new String[] {"erase", "fill", "replace", "trigger"});
    	}
    	
    	if(args.length == 3 && args[0].equals("region") && args[1].equals("fill")) {
    		return func_175762_a(args, Block.blockRegistry.getKeys());
    	}
    	
    	if(args.length >= 3 && args[0].equals("region") && args[1].equals("replace")) {
    		return func_175762_a(args, Block.blockRegistry.getKeys());
    	}
    	
    	return null;
    }
    
    public int getRequiredPermissionLevel()
    {
        return 2;
    }
	
}
