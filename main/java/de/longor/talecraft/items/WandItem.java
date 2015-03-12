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
    	
    	System.out.println("ITEM WAND : Block Click -> " + pos);
    	
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
    	
    	System.out.println("comp = " + tcWand);
    	
    	TaleCraft.simpleNetworkWrapper.sendTo(new PlayerNBTDataMerge(compound), (EntityPlayerMP) playerIn);
    	
        return true;
    }

    public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn)
    {
    	if(worldIn.isRemote)
    		return itemStackIn;
    	
    	System.out.println("ITEM WAND : Air Click");
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
	
}
