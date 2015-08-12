package de.longor.talecraft.invoke;

import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

import akka.actor.Scope;
import de.longor.talecraft.TaleCraft;
import de.longor.talecraft.blocks.TCITriggerableBlock;
import de.longor.talecraft.network.StringNBTCommand;
import de.longor.talecraft.script.wrappers.entity.EntityObjectWrapper;
import de.longor.talecraft.script.wrappers.world.WorldObjectWrapper;
import de.longor.talecraft.server.ServerMirror;
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
	
	public static final void invoke(IInvoke invoke, IInvokeSource source, Map<String,Object> scopeParams, EnumTriggerState triggerStateOverride) {
		if(source.getInvokeWorld() != null && source.getInvokeWorld().getGameRules().getGameRuleBooleanValue("disableTCInvokeSystem")) {
			TaleCraft.logger.info("Tried to execute invoke {"+invoke+"}, but the invoke system is disabled!");
			return;
		}
		
		if(invoke == null) {
			TaleCraft.logger.error("NULL was passed to the invoke method! Source: "+source+"!");
			return;
		}
		
		if(invoke instanceof NullInvoke) {
			TaleCraft.logger.error("Uh oh, a NULL invoke from "+source+"!");
			ServerMirror.instance().trackInvoke(source, invoke);
			return;
		}
		
		if(invoke instanceof IScriptInvoke) {
			String script = ((IScriptInvoke) invoke).getScript();
			Scriptable scope = source.getInvokeScriptScope();
			
			if(scopeParams != null) {
				Context ctx = Context.enter();
				Scriptable scope2 = ctx.newObject(TaleCraft.globalScriptManager.getGlobalScope());
				scope2.setPrototype(scope);
				scope2.setParentScope(scope);
				
				for(Entry<String, Object> entry : scopeParams.entrySet()) {
					String NAME = entry.getKey();
					Object OBJECT = entry.getValue();
					ScriptableObject.putProperty(scope2, NAME, Context.javaToJS(OBJECT, scope2));
				}
				
				scope = scope2;
			}
			
			TaleCraft.globalScriptManager.interpret(script, source.toString(), scope);
			
			if(source.getInvokeWorld().getGameRules().getGameRuleBooleanValue("visualEventDebugging")) {
				// This could possibly create a crapton of lag if many events are fired.
				NBTTagCompound pktdata = new NBTTagCompound();
				pktdata.setString("type", "pos-marker");
				pktdata.setIntArray("pos", new int[]{source.getInvokePosition().getX(),source.getInvokePosition().getY(),source.getInvokePosition().getZ()});
				pktdata.setInteger("color", 0xFF0000);
				TaleCraft.network.sendToAll(new StringNBTCommand("client.render.renderable.push", pktdata));
			}
			
			ServerMirror.instance().trackInvoke(source, invoke);
			return;
		}
		
		if(invoke instanceof CommandInvoke) {
			MinecraftServer server = MinecraftServer.getServer();
			ICommandManager commandManager = server.getCommandManager();
			ICommandSender sender = source.getInvokeAsCommandSender();
			String command = ((CommandInvoke) invoke).getCommand();
			commandManager.executeCommand(sender, command);
			
			if(source.getInvokeWorld().getGameRules().getGameRuleBooleanValue("visualEventDebugging")) {
				// This could possibly create a crapton of lag if many events are fired.
				NBTTagCompound pktdata = new NBTTagCompound();
				pktdata.setString("type", "pos-marker");
				pktdata.setIntArray("pos", new int[]{source.getInvokePosition().getX(),source.getInvokePosition().getY(),source.getInvokePosition().getZ()});
				pktdata.setInteger("color", 0x0099FF);
				TaleCraft.network.sendToAll(new StringNBTCommand("client.render.renderable.push", pktdata));
			}
			
			ServerMirror.instance().trackInvoke(source, invoke);
			return;
		}
		
		if(invoke instanceof BlockTriggerInvoke) {
			// TaleCraft.logger.info("--> Executing BlockRegionTrigger from " + source.getPosition());
			
			int[] bounds = ((BlockTriggerInvoke) invoke).getBounds();
			EnumTriggerState state = ((BlockTriggerInvoke) invoke).getOnOff();
			
			state = state.override(triggerStateOverride);
			
			if(bounds == null || bounds.length != 6) {
				TaleCraft.logger.error("Invalid bounds @ BlockRegionTrigger @ " + source.getInvokePosition());
				return;
			}
			
			int ix = Math.min(bounds[0], bounds[3]);
			int iy = Math.min(bounds[1], bounds[4]);
			int iz = Math.min(bounds[2], bounds[5]);
			int ax = Math.max(bounds[0], bounds[3]);
			int ay = Math.max(bounds[1], bounds[4]);
			int az = Math.max(bounds[2], bounds[5]);
			
			trigger(source, ix, iy, iz, ax, ay, az, state);
			
			ServerMirror.instance().trackInvoke(source, invoke);
			return;
		}
		
		TaleCraft.logger.error("! Unknown Invoke Type --> " + invoke.getType());
		
	}
	
	public static final void trigger(IInvokeSource source, int ix, int iy, int iz, int ax, int ay, int az, final EnumTriggerState triggerStateOverride) {
		// Logging this is a bad idea if a lot of these is executed very fast (ClockBlock, anyone?)
		// TaleCraft.logger.info("--> [" + ix + ","+ iy + ","+ iz + ","+ ax + ","+ ay + ","+ az + "]");
		
		World world = source.getInvokeWorld();
		
		if(world.getGameRules().getGameRuleBooleanValue("visualEventDebugging")) {
			// Send a packet to all players that a BlockRegionTrigger just happened.
			// This could possibly create a crapton of lag if many events are fired.
			NBTTagCompound pktdata = new NBTTagCompound();
			pktdata.setString("type", "line-to-box");
			pktdata.setIntArray("src", new int[]{source.getInvokePosition().getX(),source.getInvokePosition().getY(),source.getInvokePosition().getZ()});
			pktdata.setIntArray("box", new int[]{ix,iy,iz,ax,ay,az});
			TaleCraft.network.sendToAll(new StringNBTCommand("client.render.renderable.push", pktdata));
		}
		
		// Since we dont have lambda's, lets do things the old (ugly) way.
		WorldHelper.foreach(world, ix, iy, iz, ax, ay, az, new BlockRegionIterator() {
			@Override public void $(World world, IBlockState state, BlockPos position) {
				trigger(world, position, state, triggerStateOverride);
			}
		});
	}
	
	public static final void trigger(World world, BlockPos position, IBlockState state, EnumTriggerState state2) {
		Block block = state.getBlock();
		
		if(block instanceof TCITriggerableBlock){
			((TCITriggerableBlock) state.getBlock()).trigger(world, position, state2);
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
