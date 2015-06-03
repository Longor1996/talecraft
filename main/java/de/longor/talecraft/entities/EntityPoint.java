package de.longor.talecraft.entities;

import de.longor.talecraft.TaleCraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldProviderEnd;

public class EntityPoint extends Entity {

	public EntityPoint(World worldIn) {
		super(worldIn);
        this.setSize(0.5F, 0.5F);
	}
	
	@Override
	protected void entityInit() {
		// This is a point. There is nothing to do here.
	}

    protected boolean canTriggerWalking()
    {
        return false;
    }

    public void onUpdate()
    {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
    }

    public boolean canBeCollidedWith()
    {
        return TaleCraft.proxy.isBuildMode();
    }

    public boolean isEntityInvulnerable(DamageSource p_180431_1_)
    {
        return !p_180431_1_.isCreativePlayer();
    }

    public boolean interact(EntityPlayer player)
    {
    	return false;
    }

    public boolean interactFirst(EntityPlayer playerIn)
    {
    	if(playerIn.worldObj.isRemote)
    		return false;
    	
    	ItemStack itemStack = playerIn.getCurrentEquippedItem();
    	
    	if(itemStack == null)
    		return false;
    	
    	if(itemStack.getItem() == Items.name_tag) {
    		this.setCustomNameTag(itemStack.getDisplayName());
        	return true;
    	}
    	
    	return false;
    }

    public boolean attackEntityFrom(DamageSource source, float amount)
    {
    	if(worldObj.isRemote)
    		return false;
    	
    	if(source.isCreativePlayer()) {
    		this.setDead();
        	return true;
    	}
    	
		return false;
    }
	
	@Override
	protected void readEntityFromNBT(NBTTagCompound tagCompund) {
		// This is a point. There is nothing to do here.
	}
	
	@Override
	protected void writeEntityToNBT(NBTTagCompound tagCompound) {
		// This is a point. There is nothing to do here.
	}
	
}
