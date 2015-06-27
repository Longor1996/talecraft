package de.longor.talecraft.voxelbrush.actions;

import net.minecraft.block.Block;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.World;
import de.longor.talecraft.util.MutableBlockPos;
import de.longor.talecraft.voxelbrush.IAction;

public class ReplaceAction implements IAction{
	Block block;
	IBlockState blockState;
	
	public ReplaceAction(Block block, IBlockState blockState) {
		this.block = block;
		this.blockState = blockState;
	}
	
	public String toString() {
		return "Replace ["+blockState+"]";
	}
	
	@Override
	public void act(World world, MutableBlockPos pos, int x, int y, int z) {
		world.setBlockState(pos, blockState, 3);
	}
	
}
