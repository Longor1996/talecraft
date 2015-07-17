package de.longor.talecraft.items;

import de.longor.talecraft.TaleCraft;
import de.longor.talecraft.clipboard.ClipboardItem;
import de.longor.talecraft.proxy.ClientProxy;
import de.longor.talecraft.proxy.ServerHandler;
import de.longor.talecraft.server.ServerMirror;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.util.BlockSnapshot;

public class PasteItem extends TCItem {
	
    public boolean onItemUse(
    		ItemStack stack, EntityPlayer playerIn, World worldIn,
    		BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ
    ) {
    	// Player Clicked a Block.
    	
    	onItemRightClick(stack, worldIn, playerIn);
    	
    	return true;
    }
	
    public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn) {
    	if(worldIn.isRemote)
    		return itemStackIn;
    	
    	NBTTagCompound settings = TaleCraft.getSettings(playerIn);
    	
    	float lenMul = settings.getInteger("item.paste.reach");
    	Vec3 plantPos = playerIn.getLook(1);
    	plantPos = new Vec3(
    			plantPos.xCoord*lenMul,
    			plantPos.yCoord*lenMul,
    			plantPos.zCoord*lenMul
    	).add(playerIn.getPositionEyes(1));
    	
    	// PLANT!
    	String keyString = "player."+playerIn.getGameProfile().getId().toString();
    	ClipboardItem item = ServerHandler.getServerMirror(null).getClipboard().get(keyString);
    	
    	if(item != null) {
    		
    		if(item.getData().hasKey("offset", item.getData().getId())) {
    			NBTTagCompound offset = item.getData().getCompoundTag("offset");
    			plantPos = new Vec3(
    					plantPos.xCoord + offset.getFloat("x"),
    					plantPos.yCoord + offset.getFloat("y"),
    					plantPos.zCoord + offset.getFloat("z")
    			);
    		}
    		
    		if(item.getData().hasKey("blocks")) {
    			ClipboardItem.pasteRegion(item, new BlockPos(plantPos), worldIn, playerIn);
    		}
    		
    		if(item.getData().hasKey("entity")) {
    			ClipboardItem.pasteEntity(item, plantPos, worldIn, playerIn);
    		}
    	}
    	
    	return itemStackIn;
    }
    
}
