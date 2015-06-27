package de.longor.talecraft.voxelbrush.actions;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.World;
import de.longor.talecraft.util.MutableBlockPos;
import de.longor.talecraft.voxelbrush.IAction;

public class MaskReplaceAction implements IAction{
	Block block;
	IBlockState blockState;
	Block maskblock;
	IBlockState maskblockState;
	
	public MaskReplaceAction(Block block, IBlockState blockState, Block maskblock, IBlockState maskblockState) {
		this.block = block;
		this.blockState = blockState;
		this.maskblock = maskblock;
		this.maskblockState = maskblockState;
	}
	
	public String toString() {
		return "Masked Replace ["+maskblockState+" with "+blockState+"]";
	}
	
	@Override
	public void act(World world, MutableBlockPos pos, int x, int y, int z) {
		IBlockState state = world.getBlockState(pos);
		
		if(state == maskblockState)
			world.setBlockState(pos, blockState, 3);
	}
	
}
