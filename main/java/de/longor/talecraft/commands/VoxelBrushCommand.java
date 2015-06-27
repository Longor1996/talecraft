package de.longor.talecraft.commands;

import java.util.List;

import de.longor.talecraft.invoke.Invoke;
import de.longor.talecraft.items.VoxelBrushItem;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.SyntaxErrorException;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class VoxelBrushCommand extends CommandBase {
	
	@Override
	public String getName() {
		return "tc_voxelbrush";
	}
	
	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "?";
	}
	
	@Override
	public void execute(ICommandSender sender, String[] args) throws CommandException {
		Entity entity = sender.getCommandSenderEntity();
		
		if(entity == null)
			throw new CommandException("This command can only be run by a player.", entity);
		
		if(!(entity instanceof EntityPlayerMP))
			throw new CommandException("This command can only be run by a player.", entity);
		
		EntityPlayerMP player = (EntityPlayerMP) entity;
		
		ItemStack itemStack = player.getCurrentEquippedItem();
		
		if(itemStack == null)
			throw new CommandException("No voxelbrush equipped.", player);
		
		if(!(itemStack.getItem() instanceof VoxelBrushItem))
			throw new CommandException("No voxelbrush equipped.", player, itemStack);
		
		if(args.length == 0)
			throw new CommandException("Syntax error: No arguments given.", args, args.length);
		
		if(!itemStack.hasTagCompound()) {
			itemStack.setTagCompound(new NBTTagCompound());
		}
    	
		NBTTagCompound itemStackNbtTagCompound = itemStack.getTagCompound();
		
		if(!itemStackNbtTagCompound.hasKey("vbData")) {
			NBTTagCompound vbData = new NBTTagCompound();
			
			itemStackNbtTagCompound.setTag("vbData", vbData);
		}
		
        NBTTagCompound vbData = itemStackNbtTagCompound.getCompoundTag("vbData");
		
		String arg0 = args[0];
		
		if(arg0.equalsIgnoreCase("init")) {
			NBTTagCompound shapeTag = new NBTTagCompound();
			shapeTag.setString("type", "sphere");
			shapeTag.setDouble("radius", 3.5);
			vbData.setTag("shape", shapeTag);
			
			NBTTagCompound action = new NBTTagCompound();
			action.setString("type", "replace");
			action.setString("blockID", "minecraft:stone");
			action.setString("blockMeta", "0");
			vbData.setTag("action", action);
		}
		
		if(arg0.equalsIgnoreCase("action")) {
			NBTTagCompound actionTag = vbData.getCompoundTag("action");
			
			if(args[1].equals("replace")) {
				String blockID = args[2];
				String blockMeta = args.length > 3 ? args[3] : "0";
				
				if((Block) Block.blockRegistry.getObject(new ResourceLocation(blockID)) == null) {
					throw new CommandException("Invalid block ID: " + blockID, blockID, blockMeta);
				}
				
				actionTag.setString("type", "replace");
				actionTag.setString("blockID", blockID);
				actionTag.setString("blockMeta", blockMeta);
			}
			
			if(args[1].equals("maskreplace")) {
				if(args.length != 6) {
					throw new SyntaxErrorException("Wrong amaount of parameters: //maskreplace <block> <blockMeta> <mask> <maskMeta>");
				}
				
				String blockID = args[2];
				String blockMeta = args[3];
				
				if((Block) Block.blockRegistry.getObject(new ResourceLocation(blockID)) == null) {
					throw new CommandException("Invalid block ID: " + blockID, blockID, blockMeta);
				}
				
				String maskBlockID = args[4];
				String maskBlockMeta = args[5];
				
				if((Block) Block.blockRegistry.getObject(new ResourceLocation(maskBlockID)) == null) {
					throw new CommandException("Invalid block ID: " + maskBlockID, maskBlockID, maskBlockMeta);
				}
				
				actionTag.setString("type", "maskreplace");
				actionTag.setString("blockID", blockID);
				actionTag.setString("blockMeta", blockMeta);
				actionTag.setString("mask_blockID", maskBlockID);
				actionTag.setString("mask_blockMeta", maskBlockMeta);
			}
			
			if(args[1].equals("grassify")) {
				actionTag.setString("type", "grassify");
			}
			
			if(args[1].equals("erase")) {
				actionTag.setString("type", "erase");
			}
		}
		
		if(arg0.equalsIgnoreCase("shape")) {
			NBTTagCompound shapeTag = vbData.getCompoundTag("shape");
			
			if(args[1].equals("offset")) {
				int ox = this.parseInt(args[2], -64, 64);
				int oy = this.parseInt(args[3], -64, 64);
				int oz = this.parseInt(args[4], -64, 64);
				shapeTag.setInteger("offsetX", ox);
				shapeTag.setInteger("offsetY", oy);
				shapeTag.setInteger("offsetZ", oz);
			}
			
			if(args[1].equals("box")) {
				shapeTag.setString("type", "box");
				
				if(args.length == 3) {
					int e = this.parseInt(args[2], 0, 64);
					shapeTag.setInteger("width", e);
					shapeTag.setInteger("height", e);
					shapeTag.setInteger("length", e);
				}
				
				if(args.length == 4) {
					int e = this.parseInt(args[2], 0, 64);
					int ey = this.parseInt(args[3], 0, 64);
					shapeTag.setInteger("width", e);
					shapeTag.setInteger("height", ey);
					shapeTag.setInteger("length", e);
				}
				
				if(args.length == 5) {
					int ex = this.parseInt(args[2], 0, 64);
					int ey = this.parseInt(args[3], 0, 64);
					int ez = this.parseInt(args[4], 0, 64);
					shapeTag.setInteger("width", ex);
					shapeTag.setInteger("height", ey);
					shapeTag.setInteger("length", ez);
				}
			}//"box"
			
			if(args[1].equals("cylinder")) {
				shapeTag.setString("type", "cylinder");
				
				if(args.length == 4) {
					int ey = this.parseInt(args[2], 0, 64);
					shapeTag.setInteger("height", ey);
					
					String str = args[3];
					
					if(str.startsWith("d")) {
						str = str.substring(1);
						shapeTag.setDouble("radius", this.parseDouble(str, 1, 128) / 2d);
					} else {
						shapeTag.setDouble("radius", this.parseDouble(str, .5, 64));
					}
				}
			}//"cylinder"
			
			if(args[1].equals("sphere")) {
				shapeTag.setString("type", "sphere");
				
				if(args.length > 2) {
					String str = args[2];
					
					if(str.startsWith("d")) {
						str = str.substring(1);
						shapeTag.setDouble("radius", this.parseDouble(str, 1, 128) / 2d);
					} else {
						shapeTag.setDouble("radius", this.parseDouble(str, .5, 64));
					}
				}
			}//"sphere"
			
			
			
		}//"shape"
		
		System.out.println("-> " + itemStackNbtTagCompound);
	}
	
    public List addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
    	if(args.length == 1) {
			return getListOfStringsMatchingLastWord(args, new String[] {"init", "shape", "action"});
		}
		
    	if(args.length >= 1) {
    		if(args[0].equals("shape") && args.length <= 2 ) {
    			return getListOfStringsMatchingLastWord(args, new String[] {"box", "cylinder", "sphere"});
    		}
    		
    		// func_175762_a(args, Block.blockRegistry.getKeys())
    		
    		if(args.length == 2 && args[0].equals("action")) {
    			return getListOfStringsMatchingLastWord(args, new String[] {"replace", "maskreplace", "grassify", "erase"});
    		}
    		
    		if(args.length >= 2 && args[0].equals("action") && args[1].equals("replace")) {
    			return func_175762_a(args, Block.blockRegistry.getKeys());
    		}
    		
    		if(args.length >= 2 && args[0].equals("action") && args[1].equals("maskreplace")) {
    			return func_175762_a(args, Block.blockRegistry.getKeys());
    		}
    		
    		if(args.length >= 5 && args[0].equals("action") && args[1].equals("maskreplace")) {
    			return func_175762_a(args, Block.blockRegistry.getKeys());
    		}
    	}
    	
    	return null;
    }
    
    public int getRequiredPermissionLevel()
    {
        return 2;
    }
    
}
