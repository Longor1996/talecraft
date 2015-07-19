package de.longor.talecraft;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import net.minecraft.block.Block;
import net.minecraft.block.BlockStone;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemMultiTexture;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameData;
import net.minecraftforge.fml.common.registry.GameRegistry;
import de.longor.talecraft.blocks.deco.BlankBlock;
import de.longor.talecraft.blocks.util.BarrierEXTBlock;
import de.longor.talecraft.blocks.util.BlockUpdateDetector;
import de.longor.talecraft.blocks.util.ClockBlock;
import de.longor.talecraft.blocks.util.EmitterBlock;
import de.longor.talecraft.blocks.util.ImageHologramBlock;
import de.longor.talecraft.blocks.util.KillBlock;
import de.longor.talecraft.blocks.util.RedstoneActivatorBlock;
import de.longor.talecraft.blocks.util.RedstoneTriggerBlock;
import de.longor.talecraft.blocks.util.RelayBlock;
import de.longor.talecraft.blocks.util.ScriptBlock;
import de.longor.talecraft.blocks.util.StorageBlock;
import de.longor.talecraft.blocks.util.tileentity.BlockUpdateDetectorTileEntity;
import de.longor.talecraft.blocks.util.tileentity.ClockBlockTileEntity;
import de.longor.talecraft.blocks.util.tileentity.EmitterBlockTileEntity;
import de.longor.talecraft.blocks.util.tileentity.ImageHologramBlockTileEntity;
import de.longor.talecraft.blocks.util.tileentity.RedstoneTriggerBlockTileEntity;
import de.longor.talecraft.blocks.util.tileentity.RelayBlockTileEntity;
import de.longor.talecraft.blocks.util.tileentity.ScriptBlockTileEntity;
import de.longor.talecraft.blocks.util.tileentity.StorageBlockTileEntity;

public class TaleCraftBlocks
{
	public static HashMap<String, Block> blocksMap;
	public static List<Block> blocks;
	
	// UTILITY
	public static KillBlock killBlock;
	public static ClockBlock clockBlock;
	public static RedstoneTriggerBlock redstoneTrigger;
	public static RedstoneActivatorBlock redstoneActivator;
	public static RelayBlock relayBlock;
	public static ScriptBlock scriptBlock;
	public static BlockUpdateDetector updateDetectorBlock;
	public static StorageBlock storageBlock;
	public static EmitterBlock emitterBlock;
	public static ImageHologramBlock imageHologramBlock;
//	public static BarrierEXTBlock barrierEXTBlock;
	
	// DECORATION
	public static BlankBlock blankBlock;
	public static BlankBlock deco_stone_a;
	public static BlankBlock deco_stone_b;
	public static BlankBlock deco_stone_c;
	
	static void init()
	{
		blocksMap = Maps.newHashMap();
		blocks = Lists.newArrayList();
		///////////////////////////////////
		
		init_utility();
		init_decoration();
	}
	
	private static void init_utility() {
		killBlock = register("killblock", new KillBlock(), new BlockRegisterFunc() {
			@Override public void call(Block block, String name) {
				GameRegistry.registerBlock(block, ItemBlockKillBlock.class, name);
			}
		});
		
		clockBlock = register("clockblock", new ClockBlock());
		GameRegistry.registerTileEntity(ClockBlockTileEntity.class, "tc_clockblock");
		
		redstoneTrigger = register("redstone_trigger", new RedstoneTriggerBlock());
		GameRegistry.registerTileEntity(RedstoneTriggerBlockTileEntity.class, "tc_redstonetrigger");
		
		redstoneActivator = register("redstone_activator", new RedstoneActivatorBlock());
		
		relayBlock = register("relayblock", new RelayBlock());
		GameRegistry.registerTileEntity(RelayBlockTileEntity.class, "tc_relayblock");
		
		scriptBlock = register("scriptblock", new ScriptBlock());
		GameRegistry.registerTileEntity(ScriptBlockTileEntity.class, "tc_scriptblock");
		
		updateDetectorBlock = register("updatedetectorblock", new BlockUpdateDetector());
		GameRegistry.registerTileEntity(BlockUpdateDetectorTileEntity.class, "tc_updatedetectorblock");
		
		storageBlock = register("storageblock", new StorageBlock());
		GameRegistry.registerTileEntity(StorageBlockTileEntity.class, "tc_storageblock");
		
		emitterBlock = register("emitterblock", new EmitterBlock());
		GameRegistry.registerTileEntity(EmitterBlockTileEntity.class, "tc_emitterblock");
		
		imageHologramBlock = register("imagehologramblock", new ImageHologramBlock());
		GameRegistry.registerTileEntity(ImageHologramBlockTileEntity.class, "tc_imagehologramblock");
		
		// Can't implement because custom renderers are currently only possible with tileentities
//		barrierEXTBlock = register("barrierextblock", new BarrierEXTBlock(), new BlockRegisterFunc() {
//			@Override public void call(Block block, String name) {
//				GameRegistry.registerBlock(block, ItemBarrierEXTBlock.class, name);
//			}
//		});
	}

	private static void init_decoration() {
		blankBlock = register("blankblock", new BlankBlock(), new BlockRegisterFunc() {
			@Override public void call(Block block, String name) {
				GameRegistry.registerBlock(block, ItemBlockBlankBlock.class, name);
			}
		});
		
		deco_stone_a = register("deco_stone_a", new BlankBlock(), new BlockRegisterFunc() {
			@Override public void call(Block block, String name) {
				GameRegistry.registerBlock(block, ItemBlockBlankBlock.class, name);
			}
		});
		deco_stone_b = register("deco_stone_b", new BlankBlock(), new BlockRegisterFunc() {
			@Override public void call(Block block, String name) {
				GameRegistry.registerBlock(block, ItemBlockBlankBlock.class, name);
			}
		});
		deco_stone_c = register("deco_stone_c", new BlankBlock(), new BlockRegisterFunc() {
			@Override public void call(Block block, String name) {
				GameRegistry.registerBlock(block, ItemBlockBlankBlock.class, name);
			}
		});
		
	}

	static <T extends Block> T register(String name, T block) {
		block.setUnlocalizedName("talecraft:"+name);
		GameRegistry.registerBlock(block, name);
		blocksMap.put(name, block);
		blocks.add(block);
		
		Item item = Item.getItemFromBlock(block);
		if(item!=null)TaleCraftItems.ALL_TC_ITEMS.add(item);
		
		return block;
	}
	
	static <T extends Block> T register(String name, T block, BlockRegisterFunc registerFunc) {
		block.setUnlocalizedName("talecraft:"+name);
		registerFunc.call(block, name);
		blocksMap.put(name, block);
		blocks.add(block);
		
		Item item = Item.getItemFromBlock(block);
		if(item!=null)TaleCraftItems.ALL_TC_ITEMS.add(item);
		
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
	
	public static class ItemBlockBlankBlock extends ItemMultiTexture {
		public ItemBlockBlankBlock(Block block) {
			super(block, block, new String[]{
					"0",
					"1",
					"2",
					"3",
					"4",
					"5",
					"6",
					"7",
					"8",
					"9",
					"10",
					"11",
					"12",
					"13",
					"14",
					"15"
			});
		}
		
	    public int getMetadata(int damage)
	    {
	        return damage;
	    }
	}
	
	public static class ItemBarrierEXTBlock extends ItemMultiTexture {
		public ItemBarrierEXTBlock(Block block) {
			super(block, block, new String[]{
					"all", // 0
					"players", // 1
					"living", // 2
					"living (not players)", // 3
					"monsters", // 4
					"villagers", // 5
					"items" // 6
			});
		}
	}
	
	public static List<Block> getBlockList() {
		return blocks;
	}
	
}
