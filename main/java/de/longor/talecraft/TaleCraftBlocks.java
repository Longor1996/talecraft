package de.longor.talecraft;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import net.minecraft.block.Block;
import net.minecraft.block.BlockStone;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemMultiTexture;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameData;
import net.minecraftforge.fml.common.registry.GameRegistry;
import de.longor.talecraft.blocks.ClockBlock;
import de.longor.talecraft.blocks.ClockBlockTileEntity;
import de.longor.talecraft.blocks.KillBlock;
import de.longor.talecraft.blocks.RedstoneTrigger;
import de.longor.talecraft.blocks.RedstoneTriggerTileEntity;

public class TaleCraftBlocks
{
	public static KillBlock killBlock;
	public static ClockBlock clockBlock;
	public static RedstoneTrigger redstoneTrigger;
	public static HashMap<String, Block> allBlocks;
	
	static void init()
	{
		allBlocks = new HashMap<String, Block>();
		///////////////////////////////////
		
		killBlock = register("killblock", new KillBlock(), new BlockRegisterFunc() {
			@Override public void call(Block block, String name) {
				GameRegistry.registerBlock(block, ItemBlockKillBlock.class, name);
				
			}
		});
		
		clockBlock = register("clockblock", new ClockBlock());
		GameRegistry.registerTileEntity(ClockBlockTileEntity.class, "tc_clockblock");
		
		redstoneTrigger = register("redstone_trigger", new RedstoneTrigger());
		GameRegistry.registerTileEntity(RedstoneTriggerTileEntity.class, "tc_redstonetrigger");
		
	}
	
	static <T extends Block> T register(String name, T block) {
		block.setUnlocalizedName("talecraft:"+name);
		GameRegistry.registerBlock(block, name);
		allBlocks.put(name, block);
		return block;
	}
	
	static <T extends Block> T register(String name, T block, BlockRegisterFunc registerFunc) {
		block.setUnlocalizedName("talecraft:"+name);
		registerFunc.call(block, name);
		allBlocks.put(name, block);
		return block;
	}
	
	private static interface BlockRegisterFunc {
		public void call(Block block, String name);
	}
	
	public static class ItemBlockKillBlock extends ItemMultiTexture {
		public ItemBlockKillBlock(Block block) {
			super(block, block, new String[]{
					"all",
					"npc",
					"items",
					"living",
					"player",
					"monster",
					"xor_player"
			});
		}
	}
	
}
