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
import de.longor.talecraft.blocks.BlankBlock;
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
	public static HashMap<String, Block> allBlocks;
	
	public static KillBlock killBlock;
	public static ClockBlock clockBlock;
	public static RedstoneTriggerBlock redstoneTrigger;
	public static RedstoneActivatorBlock redstoneActivator;
	public static RelayBlock relayBlock;
	public static ScriptBlock scriptBlock;
	public static BlockUpdateDetector updateDetectorBlock;
	public static BlankBlock blankBlock;
	public static StorageBlock storageBlock;
	public static EmitterBlock emitterBlock;
	public static ImageHologramBlock imageHologramBlock;
	
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
		
		redstoneTrigger = register("redstone_trigger", new RedstoneTriggerBlock());
		GameRegistry.registerTileEntity(RedstoneTriggerBlockTileEntity.class, "tc_redstonetrigger");
		
		redstoneActivator = register("redstone_activator", new RedstoneActivatorBlock());
		
		relayBlock = register("relayblock", new RelayBlock());
		GameRegistry.registerTileEntity(RelayBlockTileEntity.class, "tc_relayblock");
		
		scriptBlock = register("scriptblock", new ScriptBlock());
		GameRegistry.registerTileEntity(ScriptBlockTileEntity.class, "tc_scriptblock");
		
		updateDetectorBlock = register("updatedetectorblock", new BlockUpdateDetector());
		GameRegistry.registerTileEntity(BlockUpdateDetectorTileEntity.class, "tc_updatedetectorblock");
		
		blankBlock = register("blankblock", new BlankBlock(), new BlockRegisterFunc() {
			@Override public void call(Block block, String name) {
				GameRegistry.registerBlock(block, ItemBlockBlankBlock.class, name);
			}
		});
		
		storageBlock = register("storageblock", new StorageBlock());
		GameRegistry.registerTileEntity(StorageBlockTileEntity.class, "tc_storageblock");
		
		emitterBlock = register("emitterblock", new EmitterBlock());
		GameRegistry.registerTileEntity(EmitterBlockTileEntity.class, "tc_emitterblock");
		
		imageHologramBlock = register("imagehologramblock", new ImageHologramBlock());
		GameRegistry.registerTileEntity(ImageHologramBlockTileEntity.class, "tc_imagehologramblock");
		
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
	}
	
}
