package de.longor.talecraft.invoke;

import de.longor.talecraft.util.WorldHelper;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public interface ITriggerableBlock {
	public void trigger(World world, BlockPos position, int data);
}
