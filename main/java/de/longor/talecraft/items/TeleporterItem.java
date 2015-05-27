package de.longor.talecraft.items;

import de.longor.talecraft.TaleCraft;
import de.longor.talecraft.TaleCraftTabs;
import net.minecraft.block.Block;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TeleporterItem extends TCItem {
	
	public TeleporterItem() {
        this.setCreativeTab(TaleCraftTabs.tab_TaleCraftTab);
	}
	
    public boolean onItemUse(
    		ItemStack stack, EntityPlayer playerIn, World worldIn,
    		BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ
    ) {
    	if(worldIn.isRemote)
    		return true;
    	
        // Get new Position
        double nX = pos.getX() + 0.5;
        double nZ = pos.getZ() + 0.5;
        double nY = pos.getY() + 1;
        
        // Get Old Rotation
        float rY = playerIn.rotationYaw;
        float rP = playerIn.rotationPitch;
        
        // Force-Unmount the Player
        playerIn.mountEntity((Entity)null);
           
        // Teleport
        if(playerIn instanceof EntityPlayerMP) {
        	// Its a MP player
        	((EntityPlayerMP) playerIn).playerNetServerHandler.setPlayerLocation(nX,nY,nZ, rY, rP);
			playerIn.velocityChanged = true;
        } else if (playerIn instanceof EntityPlayerSP) {
        	// Its a SP player
        	/// This will probably never be called.
        	playerIn.setPositionAndRotation(nX, nY, nZ, rY, rP);
			playerIn.velocityChanged = true;
		}
        
    	return true;
    }
	
    public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn) {
    	if(worldIn.isRemote)
    		return itemStackIn;
    	
    	float lerp = 1F;
    	float dist = 256;
    	
        Vec3 start = playerIn.getPositionEyes(lerp);
        Vec3 direction = playerIn.getLook(lerp);
        Vec3 end = start.addVector(direction.xCoord * dist, direction.yCoord * dist, direction.zCoord * dist);
    	
        MovingObjectPosition MOP = worldIn.rayTraceBlocks(start, end, false, false, false);
    	
        if(MOP != null && MOP.typeOfHit == MovingObjectType.BLOCK) {
        	// Extract Block Hit
        	BlockPos newPos = MOP.getBlockPos();
        	
        	// Get new Position
        	double nX = newPos.getX() + 0.5;
        	double nZ = newPos.getZ() + 0.5;
        	double nY = newPos.getY() + 1;
        	
        	// Get Old Rotation
        	float rY = playerIn.rotationYaw;
        	float rP = playerIn.rotationPitch;
        	
        	// Force-Unmount the Player
        	playerIn.mountEntity((Entity)null);
            
        	// Teleport
        	if(playerIn instanceof EntityPlayerMP) {
        		// Its a MP player
        		((EntityPlayerMP) playerIn).playerNetServerHandler.setPlayerLocation(nX,nY,nZ, rY, rP);
				playerIn.velocityChanged = true;
        	} else if (playerIn instanceof EntityPlayerSP) {
        		// Its a SP player
        		/// This will probably never be called.
        		playerIn.setPositionAndRotation(nX, nY, nZ, rY, rP);
				playerIn.velocityChanged = true;
			}
        }
        
        return itemStackIn;
    }
    
}
