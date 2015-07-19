package de.longor.talecraft.commands;

import java.util.Arrays;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import de.longor.talecraft.TaleCraft;
import de.longor.talecraft.network.StringNBTCommand;
import de.longor.talecraft.util.CommandArgumentParser;

public class VisualizeCommand extends CommandBase {
	
	@Override
	public String getName() {
		return "tc_vz";
	}
	
	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "< ? >";
	}
	
	@Override
	public void execute(ICommandSender sender, String[] args) throws CommandException {
		CommandArgumentParser parser = new CommandArgumentParser(args);
		
		String action = parser.consume_string("Could not parse action.");
		
		// CLEAR ALL TEMPORABLES
		if(action.equals("clear")) {
			TaleCraft.network.sendToAll(new StringNBTCommand("clearRenderable"));
			return;
		}
		
		// CREATE SHAPE TEMPORABLE
		if(action.equals("sh")) {
			execute_sh(sender, args, parser);
			return;
		}
		
		// CREATE ENTITY SELECTOR TEMPORABLE
		// Currently disabled.
//		if(action.equals("es")) {
//			execute_es(sender, args, parser);
//			return;
//		}
		
	}
	
    private void execute_sh(ICommandSender sender, String[] args, CommandArgumentParser parser) throws CommandException {
    	String shape = parser.consume_string("Could not parse shape.");
    	String color = parser.consume_string("Could not parse color.");
    	
    	// Everything starting with 'c_' means 'centered on command-sender'.
    	// Everything else is normal.
    	
    	if(shape.equals("c_bx")) {
    		int sw = 1;//x-size
    		int sh = 1;//y-size
    		int sl = 1;//z-size
    		
    		System.out.println(":: " + parser.remaining());
    		
    		if(parser.remaining() == 1) {
    			int i = parser.consume_int("Could not parse extent.", 0, 1024);
    			sw = sh = sl = i;
    		}
    		
    		if(parser.remaining() == 2) {
    			int horizontal = parser.consume_int("Could not parse horizontal extent.", 0, 1024);
    			int vertical = parser.consume_int("Could not parse vertical extent.", 0, 1024);
    			sw = sl = horizontal;
    			sh = vertical;
    		}
    		
    		if(parser.remaining() == 3) {
    			sw = parser.consume_int("Could not parse x-extent.", 0, 1024);
    			sh = parser.consume_int("Could not parse y-extent.", 0, 1024);
    			sl = parser.consume_int("Could not parse z-extent.", 0, 1024);
    		}
    		
    		BlockPos center = sender.getPosition();
    		int x = center.getX();
    		int y = center.getY();
    		int z = center.getZ();
    		
    		int[] box = new int[]{
    				x - sw, y - sh, z - sl,
    				x + sw, y + sh, z + sl
    		};
    		
			NBTTagCompound pktdata = new NBTTagCompound();
			pktdata.setString("type", "box");
			pktdata.setIntArray("box", box);
			pktdata.setInteger("color", parseColor(color));
			TaleCraft.network.sendToAll(new StringNBTCommand("pushRenderable", pktdata));
    	}
    	
	}
	
    private void execute_es(ICommandSender sender, String[] args, CommandArgumentParser parser) throws CommandException {
    	String selector = parser.consume_string("Could not parse selector.");
    	String color = parser.consume_string("Could not parse color.");
    	
    	System.out.println("Selector: " + selector);
    	
		NBTTagCompound pktdata = new NBTTagCompound();
		pktdata.setString("type", "selector");
		pktdata.setString("selector", selector);
		pktdata.setInteger("positionX", sender.getPosition().getX());
		pktdata.setInteger("positionY", sender.getPosition().getY());
		pktdata.setInteger("positionZ", sender.getPosition().getZ());
		pktdata.setInteger("color", parseColor(color));
		TaleCraft.network.sendToAll(new StringNBTCommand("pushRenderable", pktdata));
	}
    
	public List addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
    	if(args.length <= 1) {
    		return getListOfStringsMatchingLastWord(args, "clear", "sh", "es");
    	}
    	
    	if(args[0].equals("sh") && args.length == 2) {
    		return getListOfStringsMatchingLastWord(args, "c_bx");
    	}
    	
    	if(args[0].equals("sh") && args.length == 3) {
    		return getListOfStringsMatchingLastWord(args, "white", "black", "red", "green", "blue", "yellow", "orange", "purple");
    	}
    	
    	return null;
    }
    
    public int getRequiredPermissionLevel()
    {
        return 2;
    }
    
    public static final int parseColor(String color) throws CommandException {
    	if(color.equalsIgnoreCase("0")) return 0xFF000000;
    	if(color.equalsIgnoreCase("1")) return 0xFFFFFFFF;
    	
    	if(color.startsWith("#")) {
    		// Is a HEX color!
    		color = color.substring(1);
    		
    		if(color.length() == 3) {
    			int r = (hexcharToInt(color.charAt(0)) * 0xF) & 0xFF;
    			int g = (hexcharToInt(color.charAt(1)) * 0xF) & 0xFF;
    			int b = (hexcharToInt(color.charAt(2)) * 0xF) & 0xFF;
    			return (r<<16) | (g<<8) | (b);
    		} else if(color.length() == 6) {
    			int r = hexcharToInt(color.charAt(0), color.charAt(1)) & 0xFF;
    			int g = hexcharToInt(color.charAt(2), color.charAt(3)) & 0xFF;
    			int b = hexcharToInt(color.charAt(4), color.charAt(5)) & 0xFF;
    			return (r<<16) | (g<<8) | (b);
    		} else if(color.length() == 8) {
    			int a = hexcharToInt(color.charAt(0), color.charAt(1)) & 0xFF;
    			int r = hexcharToInt(color.charAt(2), color.charAt(3)) & 0xFF;
    			int g = hexcharToInt(color.charAt(4), color.charAt(5)) & 0xFF;
    			int b = hexcharToInt(color.charAt(6), color.charAt(7)) & 0xFF;
    			return (a<<24) | (r<<16) | (g<<8) | (b);
    		}
    	}
    	
    	if(color.equalsIgnoreCase("white")) return 0xFFFFFFFF;
    	if(color.equalsIgnoreCase("black")) return 0xFF000000;
    	if(color.equalsIgnoreCase("red")) return 0xFFFF0000;
    	if(color.equalsIgnoreCase("green")) return 0xFF00FF00;
    	if(color.equalsIgnoreCase("blue")) return 0xFF0000FF;
    	if(color.equalsIgnoreCase("yellow")) return 0xFFFFFF00;
    	if(color.equalsIgnoreCase("purple")) return 0xFFFF00FF;
    	if(color.equalsIgnoreCase("orange")) return 0xFFFF7F00;
    	
    	throw new CommandException("Could not parse color: " + color);
    }
    
	private static final int hexcharToInt(char charAt) {
		return Integer.parseInt(String.valueOf(charAt).toLowerCase(), 16);
	}
	
	private static final int hexcharToInt(char charA, char charB) {
		return Integer.parseInt((charA+""+charB).toLowerCase(), 16);
	}
	
}