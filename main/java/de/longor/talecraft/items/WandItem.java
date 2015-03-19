package de.longor.talecraft.items;

import io.netty.buffer.ByteBuf;

import java.util.Arrays;

import de.longor.talecraft.TaleCraft;
import de.longor.talecraft.TaleCraftTabs;
import de.longor.talecraft.network.PlayerNBTDataMerge;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class WandItem extends Item {
	
	public WandItem() {
        this.setCreativeTab(TaleCraftTabs.tab_TaleCraftTab);
	}
	
    public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ)
    {
    	if(worldIn.isRemote)
    		return true;
    	
    	// System.out.println("ITEM WAND : Block Click -> " + pos);
    	
    	NBTTagCompound compound = playerIn.getEntityData();
    	
    	NBTTagCompound tcWand = null;
    	
    	if(!compound.hasKey("tcWand")) {
    		tcWand = new NBTTagCompound();
    		compound.setTag("tcWand", tcWand);
    	} else {
    		tcWand = compound.getCompoundTag("tcWand");
    	}
    	
    	int[] pos$$ = new int[]{pos.getX(),pos.getY(),pos.getZ()};
    	
    	tcWand.setIntArray("cursor", Arrays.copyOf(pos$$, 3));
    	
    	if(!tcWand.hasKey("boundsA")) {
    		tcWand.setIntArray("boundsA", Arrays.copyOf(pos$$, 3));
    		tcWand.setIntArray("boundsB", Arrays.copyOf(pos$$, 3));
    	} else {
    		boolean flip = tcWand.getBoolean("flip");
    		if(flip) {
        		tcWand.setIntArray("boundsB", Arrays.copyOf(pos$$, 3));
    		} else {
        		tcWand.setIntArray("boundsA", Arrays.copyOf(pos$$, 3));
    		}
    		tcWand.setBoolean("flip", !flip);
    	}
    	
    	// System.out.println("comp = " + tcWand);
    	
    	TaleCraft.simpleNetworkWrapper.sendTo(new PlayerNBTDataMerge(compound), (EntityPlayerMP) playerIn);
    	
        return true;
    }

    public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn)
    {
    	if(worldIn.isRemote)
    		return itemStackIn;
        return itemStackIn;
    }

    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityPlayer playerIn)
    {
        return stack;
    }

    public boolean isDamageable()
    {
        return false;
    }

    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
        return false;
    }

    @SideOnly(Side.CLIENT)
    public boolean isFull3D()
    {
        return true;
    }

    public EnumAction getItemUseAction(ItemStack stack)
    {
        return EnumAction.NONE;
    }

    public boolean canHarvestBlock(Block blockIn)
    {
        return false;
    }
    
    @Override
    public boolean onBlockStartBreak(ItemStack itemstack, BlockPos pos, EntityPlayer player) {
        return true;
    }
    
    public static final int[] getBoundsFromPLAYERorNULL(EntityPlayer player) {
    	return getBoundsFromPLAYERDATAorNULL(player.getEntityData());
    }
    
    public static final int[] getBoundsFromPLAYERDATAorNULL(NBTTagCompound playerData) {
    	if(playerData.hasKey("tcWand"))
    		return getBoundsFromTCWANDorNULL(playerData.getCompoundTag("tcWand"));
    	return null;
    }
    
    public static final int[] getBoundsFromTCWANDorNULL(NBTTagCompound tcWand) {
		if(!tcWand.hasKey("boundsA") || !tcWand.hasKey("boundsB")) {
			return null;
		}
    	
    	int[] a = tcWand.getIntArray("boundsA");
		int[] b = tcWand.getIntArray("boundsB");
		
		int ix = Math.min(a[0], b[0]);
		int iy = Math.min(a[1], b[1]);
		int iz = Math.min(a[2], b[2]);
		int ax = Math.max(a[0], b[0]);
		int ay = Math.max(a[1], b[1]);
		int az = Math.max(a[2], b[2]);
		
		return new int[]{ix,iy,iz,ax,ay,az};
    }
	
}
