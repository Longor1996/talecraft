package de.longor.talecraft.items;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import de.longor.talecraft.TaleCraftTabs;

public class TCItem extends Item {
	
	public TCItem() {
        this.setCreativeTab(TaleCraftTabs.tab_TaleCraftTab);
	}
	
    public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ)
    {
    	if(worldIn.isRemote)
    		return true;
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
    public boolean isFull3D() {
        return true;
    }

    public boolean isItemTool(ItemStack stack)
    {
        return true;
    }
    
    public EnumAction getItemUseAction(ItemStack stack) {
        return EnumAction.NONE;
    }
    
    public boolean canHarvestBlock(Block blockIn) {
        return false;
    }

    public boolean canItemEditBlocks()
    {
        return false;
    }
    
    @Override
    public boolean onBlockStartBreak(ItemStack itemstack, BlockPos pos, EntityPlayer player) {
        return true;
    }
    
    public int getItemStackLimit(ItemStack stack)
    {
        return 1;
    }

    public boolean getShareTag()
    {
        return true;
    }
    
}
