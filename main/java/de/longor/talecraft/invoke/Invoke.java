package de.longor.talecraft.invoke;

import java.util.Random;

import org.mozilla.javascript.Scriptable;

import scala.tools.nsc.settings.Final;
import de.longor.talecraft.TaleCraft;
import de.longor.talecraft.blocks.TCITriggerableBlock;
import de.longor.talecraft.network.StringNBTCommand;
import de.longor.talecraft.util.WorldHelper;
import de.longor.talecraft.util.WorldHelper.BlockRegionIterator;
import net.minecraft.block.Block;
import net.minecraft.block.BlockButton;
import net.minecraft.block.BlockCommandBlock;
import net.minecraft.block.BlockDispenser;
import net.minecraft.block.BlockDropper;
import net.minecraft.block.BlockLever;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.BlockTNT;
import net.minecraft.block.state.IBlockState;
import net.minecraft.command.ICommandManager;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntityCommandBlock;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class Invoke {
	
	public static final void invoke(IInvoke invoke, IInvokeSource source) {
		if(invoke == null) {
			TaleCraft.logger.error("NULL was passed to the invoke method! Source: "+source+"!");
			return;
		}
		
		if(invoke instanceof NullInvoke) {
			TaleCraft.logger.error("Uh oh, a NULL invoke from "+source+"!");
			return;
		}
		
		if(invoke instanceof IScriptInvoke) {
			String script = ((IScriptInvoke) invoke).getScript();
			Scriptable scope = source.getScriptScope();
			
			TaleCraft.globalScriptManager.interpret(script, source.toString(), scope);
			
			if(source.getWorld().getGameRules().getGameRuleBooleanValue("visualEventDebugging")) {
				// This could possibly create a crapton of lag if many events are fired.
				NBTTagCompound pktdata = new NBTTagCompound();
				pktdata.setString("type", "pos-marker");
				pktdata.setIntArray("pos", new int[]{source.getPosition().getX(),source.getPosition().getY(),source.getPosition().getZ()});
				pktdata.setInteger("color", 0xFF0000);
				TaleCraft.simpleNetworkWrapper.sendToAll(new StringNBTCommand("pushRenderable", pktdata));
			}
			return;
		}
		
		if(invoke instanceof CommandInvoke) {
			MinecraftServer server = MinecraftServer.getServer();
			ICommandManager commandManager = server.getCommandManager();
			ICommandSender sender = source.getCommandSender();
			String command = ((CommandInvoke) invoke).getCommand();
			commandManager.executeCommand(sender, command);
			
			if(source.getWorld().getGameRules().getGameRuleBooleanValue("visualEventDebugging")) {
				// This could possibly create a crapton of lag if many events are fired.
				NBTTagCompound pktdata = new NBTTagCompound();
				pktdata.setString("type", "pos-marker");
				pktdata.setIntArray("pos", new int[]{source.getPosition().getX(),source.getPosition().getY(),source.getPosition().getZ()});
				pktdata.setInteger("color", 0x0099FF);
				TaleCraft.simpleNetworkWrapper.sendToAll(new StringNBTCommand("pushRenderable", pktdata));
			}
		}
		
		if(invoke instanceof BlockTriggerInvoke) {
			// TaleCraft.logger.info("--> Executing BlockRegionTrigger from " + source.getPosition());
			
			int[] bounds = ((BlockTriggerInvoke) invoke).getBounds();
			int data = ((BlockTriggerInvoke) invoke).getTriggerData();
			
			if(bounds == null || bounds.length != 6) {
				TaleCraft.logger.error("Invalid bounds @ BlockRegionTrigger @ " + source.getPosition());
				return;
			}
			
			int ix = Math.min(bounds[0], bounds[3]);
			int iy = Math.min(bounds[1], bounds[4]);
			int iz = Math.min(bounds[2], bounds[5]);
			int ax = Math.max(bounds[0], bounds[3]);
			int ay = Math.max(bounds[1], bounds[4]);
			int az = Math.max(bounds[2], bounds[5]);
			
			trigger(source, ix, iy, iz, ax, ay, az);
			
			return;
		}
		
		TaleCraft.logger.error("! Unknown Invoke Type --> " + invoke.getType());
		
	}
	
	public static final void trigger(IInvokeSource source, int ix, int iy, int iz, int ax, int ay, int az) {
		// Logging this is a bad idea if a lot of these is executed very fast (ClockBlock, anyone?)
		// TaleCraft.logger.info("--> [" + ix + ","+ iy + ","+ iz + ","+ ax + ","+ ay + ","+ az + "]");
		
		if(source.getWorld().getGameRules().getGameRuleBooleanValue("visualEventDebugging")) {
			// Send a packet to all players that a BlockRegionTrigger just happened.
			// This could possibly create a crapton of lag if many events are fired.
			NBTTagCompound pktdata = new NBTTagCompound();
			pktdata.setString("type", "line-to-box");
			pktdata.setIntArray("src", new int[]{source.getPosition().getX(),source.getPosition().getY(),source.getPosition().getZ()});
			pktdata.setIntArray("box", new int[]{ix,iy,iz,ax,ay,az});
			TaleCraft.simpleNetworkWrapper.sendToAll(new StringNBTCommand("pushRenderable", pktdata));
		}
		
		final World world = source.getWorld();
		
		// Since we dont have lambda's, lets do things the old (ugly) way.
		WorldHelper.foreach(world, ix, iy, iz, ax, ay, az, new BlockRegionIterator() {
			@Override public void $(IBlockState state, BlockPos position) {
				trigger(world, position, state, 0);
			}
		});
	}
	
	public static final void trigger(World world, BlockPos position, IBlockState state, int flag) {
		Block block = state.getBlock();
		
		if(block instanceof TCITriggerableBlock){
			((TCITriggerableBlock) state.getBlock()).trigger(world, position, 0);
			return;
		}
		
		if(block instanceof BlockCommandBlock) {
			((TileEntityCommandBlock)world.getTileEntity(position)).getCommandBlockLogic().trigger(world);
			return;
		}
		
		// Just for the heck of it!
		if(block instanceof BlockTNT) {
			((BlockTNT) block).explode(world, position, state.withProperty(BlockTNT.EXPLODE, Boolean.TRUE), null);
			world.setBlockToAir(position);
			return;
		}
		
		if(block instanceof BlockDispenser) {
			block.updateTick(world, position, state, TaleCraft.random);
			return;
		}
		
		if(block instanceof BlockDropper) {
			block.updateTick(world, position, state, TaleCraft.random);
			return;
		}
		
		// XXX: Experimental: This could break with any update.
		if(block instanceof BlockLever) {
			state = state.cycleProperty(BlockLever.POWERED);
			world.setBlockState(position, state, 3);
			world.playSoundEffect((double)position.getX() + 0.5D, (double)position.getY() + 0.5D, (double)position.getZ() + 0.5D, "random.click", 0.3F, ((Boolean)state.getValue(BlockLever.POWERED)).booleanValue() ? 0.6F : 0.5F);
			world.notifyNeighborsOfStateChange(position, block);
			EnumFacing enumfacing1 = ((BlockLever.EnumOrientation)state.getValue(BlockLever.FACING)).getFacing();
			world.notifyNeighborsOfStateChange(position.offset(enumfacing1.getOpposite()), block);
			return;
		}
		
		// XXX: Experimental: This could break with any update.
		if(block instanceof BlockButton) {
			world.setBlockState(position, state.withProperty(BlockButton.POWERED, Boolean.valueOf(true)), 3);
            world.markBlockRangeForRenderUpdate(position, position);
            world.playSoundEffect((double)position.getX() + 0.5D, (double)position.getY() + 0.5D, (double)position.getZ() + 0.5D, "random.click", 0.3F, 0.6F);
            world.notifyNeighborsOfStateChange(position, block);
            world.notifyNeighborsOfStateChange(position.offset(((EnumFacing)state.getValue(BlockButton.FACING)).getOpposite()), block);
            world.scheduleUpdate(position, block, block.tickRate(world));
		}
		
		// XXX: Implement more vanilla triggers?
	}
	
}
