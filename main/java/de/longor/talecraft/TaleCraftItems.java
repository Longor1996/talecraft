package de.longor.talecraft;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.BlockStone;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemMultiTexture;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameData;
import net.minecraftforge.fml.common.registry.GameRegistry;

import com.google.common.base.Function;

import de.longor.talecraft.items.WandItem;

public class TaleCraftItems {
	public static WandItem wand;
	
	static void init() {
		wand = new WandItem();
		wand.setUnlocalizedName("wand");
		GameRegistry.registerItem(wand, "wand", "talecraft");
	}
	
}
