package de.longor.talecraft.util;

import java.util.function.Consumer;

import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.util.BlockPos.MutableBlockPos;
import net.minecraft.world.World;

public class WorldHelper {
	
	/**
	 * Iterates trough every block and executes a given action.
	 * WARNING: This method assumes that the bounds given as parameters are already validated!
	 **/
	public static final void foreach(World world, int ix, int iy, int iz, int ax, int ay, int az, BlockRegionIterator function) {
		if(world == null)
			return;
		if(function == null)
			return;
		
		for(int y = iy; y <= ay; y++) {
			for(int z = iz; z <= az; z++) {
				for(int x = ix; x <= ax; x++) {
					BlockPos pos = new BlockPos(x, y, z);
					IBlockState state = world.getBlockState(pos);
					function.$(state, pos);
				}
			}
		}
	}
	
	/**
	 * Iterates trough every block and replaces it with the given block.
	 * WARNING: This method assumes that the bounds given as parameters are already validated!
	 **/
	public static final void fill(World world, int ix, int iy, int iz, int ax, int ay, int az, IBlockState block) {
		if(world == null)
			return;
		
		for(int y = iy; y <= ay; y++) {
			for(int z = iz; z <= az; z++) {
				for(int x = ix; x <= ax; x++) {
					BlockPos pos = new BlockPos(x, y, z);
					world.setBlockState(pos, block);
				}
			}
		}
		
	}
	
	/**
	 * Dummy functional interface with only one method.
	 **/
	public static interface BlockRegionIterator {
		void $(IBlockState state, BlockPos position);
	}
	
}
