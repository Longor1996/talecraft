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

import de.longor.talecraft.items.EraserItem;
import de.longor.talecraft.items.FillerItem;
import de.longor.talecraft.items.InstaKillItem;
import de.longor.talecraft.items.TeleporterItem;
import de.longor.talecraft.items.WandItem;

public class TaleCraftItems {
	public static WandItem wand;
	public static FillerItem filler;
	public static EraserItem eraser;
	public static TeleporterItem teleporter;
	public static InstaKillItem instakill;
	
	static void init() {
		wand = new WandItem();
		wand.setUnlocalizedName("wand");
		GameRegistry.registerItem(wand, "wand", "talecraft");
		
		filler = new FillerItem();
		filler.setUnlocalizedName("filler");
		GameRegistry.registerItem(filler, "filler", "talecraft");
		
		eraser = new EraserItem();
		eraser.setUnlocalizedName("eraser");
		GameRegistry.registerItem(eraser, "eraser", "talecraft");
		
		teleporter = new TeleporterItem();
		teleporter.setUnlocalizedName("teleporter");
		GameRegistry.registerItem(teleporter, "teleporter", "talecraft");
		
		instakill = new InstaKillItem();
		instakill.setUnlocalizedName("instakill");
		GameRegistry.registerItem(instakill, "instakill", "talecraft");
	}
	
}
