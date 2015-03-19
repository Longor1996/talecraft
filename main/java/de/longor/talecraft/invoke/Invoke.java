package de.longor.talecraft.invoke;

import de.longor.talecraft.TaleCraft;
import de.longor.talecraft.util.WorldHelper;
import de.longor.talecraft.util.WorldHelper.$682953;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCommandBlock;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityCommandBlock;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class Invoke {
	
	public static final void invoke(NBTTagCompound invokeData, IInvokeSource source) {
		String type = invokeData.getString("type");
		
		if(type.length() <= 0) {
			TaleCraft.logger.error("Uh oh, a invoke without a type-tag from "+source+"! :: " + invokeData);
			return;
		}
		
		if("blockRegionTrigger".equals(type)) {
			// TaleCraft.logger.info("--> Executing BlockRegionTrigger from " + source.getPosition());
			
			int[] bounds = invokeData.getIntArray("bounds");
			int data = invokeData.getInteger("triggerData");
			
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
			
			// Logging this is a bad idea if a lot of these is executed very fast (ClockBlock, anyone?)
			// TaleCraft.logger.info("--> [" + ix + ","+ iy + ","+ iz + ","+ ax + ","+ ay + ","+ az + "]");
			
			// If enabled ("gamerule visualEventDebugging"),
			// TODO: send a packet to all players (in creative mode) that a BlockRegionTrigger just happened.
			
			final World world = source.getWorld();
			
			// Since we dont have lambda's, lets do things the old (ugly) way.
			WorldHelper.foreach(world, ix, iy, iz, ax, ay, az, new $682953() {
				@Override public void $(IBlockState state, BlockPos position) {
					Block block = state.getBlock();
					if(block instanceof ITriggerableBlock){
						((ITriggerableBlock) state.getBlock()).trigger(world, position, 0);
					}
					if(block instanceof BlockCommandBlock) {
						((TileEntityCommandBlock)world.getTileEntity(position)).getCommandBlockLogic().trigger(world);
					}
				}
			});
			
			return;
		}
		
		TaleCraft.logger.error("! Unknown Invoke Type --> " + type);
		
	}
	
}
