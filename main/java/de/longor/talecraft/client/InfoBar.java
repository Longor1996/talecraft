package de.longor.talecraft.client;

import java.util.Arrays;

import org.lwjgl.input.Keyboard;

import de.longor.talecraft.Reference;
import de.longor.talecraft.proxy.ClientProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MovingObjectPosition;

public class InfoBar {
	private StringBuilder builder = new StringBuilder(255);
	
	public void display(Minecraft mc, EntityPlayerSP player, WorldClient theWorld, ClientProxy clientProxy) {
		// begin building string
        builder.setLength(0);
        writeModVersionInfo();
        
        if(mc.thePlayer.inventory.getCurrentItem() != null) {
        	writeHeldItemInfo(mc.thePlayer.inventory.getCurrentItem());
        }
        
        if(mc.objectMouseOver != null) {
        	writeMovingObjectPositionInfo(mc, theWorld, mc.objectMouseOver);
        }
        
        if(clientProxy.wireframeMode != 0) {
        	writeWireframeModeInfo(clientProxy.wireframeMode);
        }
        
        builder.append(' ');
        builder.append(mc.getDebugFPS());
        builder.append(" FPS");
        
        // Finally, draw the whole thing!
		mc.ingameGUI.drawRect(0, 0, mc.displayWidth, mc.fontRendererObj.FONT_HEIGHT+1, 0xAA000000);
        mc.fontRendererObj.drawString(builder.toString(), 1, 1, 14737632);
        
        //*
        if(mc.thePlayer != null && mc.thePlayer.getEntityData().hasKey("tcWand")) {
        	NBTTagCompound tcWand = mc.thePlayer.getEntityData().getCompoundTag("tcWand");
        	
        	builder.setLength(0);
        	
        	if(tcWand.hasKey("cursor")) {
                builder.append(EnumChatFormatting.YELLOW);
        		builder.append(Arrays.toString(tcWand.getIntArray("cursor")));
                builder.append(EnumChatFormatting.RESET);
        	}
        	
        	if(tcWand.hasKey("boundsA") && tcWand.hasKey("boundsB")) {
                builder.append(' ');
                
        		int[] a = tcWand.getIntArray("boundsA");
        		int[] b = tcWand.getIntArray("boundsB");
        		
        		int volume = (Math.abs(b[0]-a[0])+1) * (Math.abs(b[1]-a[1])+1) * (Math.abs(b[2]-a[2])+1);
        		
                builder.append(EnumChatFormatting.DARK_GRAY);
        		builder.append(Arrays.toString(a));
                builder.append(EnumChatFormatting.GRAY);
                builder.append(" -> ");
                builder.append(EnumChatFormatting.WHITE);
        		builder.append(Arrays.toString(b));
                builder.append(EnumChatFormatting.GRAY);
                builder.append(" = ");
                builder.append(EnumChatFormatting.BLUE);
                builder.append(volume);
                builder.append(EnumChatFormatting.RESET);
        	}
        	
    		mc.ingameGUI.drawRect(0, 10, mc.displayWidth, mc.fontRendererObj.FONT_HEIGHT+11, 0xAA000000);
        	mc.fontRendererObj.drawString(builder.toString(), 1, 11, 14737632);
        }
        //*/
	}
	
	private void writeModVersionInfo() {
        builder.append(EnumChatFormatting.YELLOW);
        builder.append("TaleCraft ");
        builder.append(EnumChatFormatting.RESET);
        builder.append(Reference.MOD_VERSION);
	}
	
	private void writeHeldItemInfo(ItemStack item) {
    	builder.append(' ');
        builder.append(EnumChatFormatting.ITALIC);
    	builder.append(item.getDisplayName());
        builder.append(EnumChatFormatting.RESET);
	}
	
	private void writeWireframeModeInfo(int wireframeMode) {
        builder.append(' ');
        builder.append(EnumChatFormatting.BLUE);
        
        switch(wireframeMode) {
        	case 1: builder.append("[1:wireframe mode]"); break;
        	case 2: builder.append("[2:backface mode]"); break;
        	case 3: builder.append("[3:lighting mode]"); break;
        	default: builder.append("[?:funny display mode]"); break;
        }
        
        builder.append(EnumChatFormatting.RESET);
	}

	private void writeMovingObjectPositionInfo(Minecraft mc, WorldClient theWorld, MovingObjectPosition mop) {
        if (mop.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && mop.getBlockPos() != null)
        {
            builder.append(EnumChatFormatting.GREEN);
			BlockPos lookAt = mc.objectMouseOver.getBlockPos();
	        builder.append(' ');
	        builder.append('[');
	        builder.append(lookAt.getX());
	        builder.append(' ');
	        builder.append(lookAt.getY());
	        builder.append(' ');
	        builder.append(lookAt.getZ());
	        
            if (theWorld.isBlockLoaded(lookAt)) {
		        builder.append(' ');
		        builder.append('=');
		        builder.append(' ');
		        
		        boolean b = Keyboard.isKeyDown(Keyboard.KEY_LCONTROL);
		        if(b) {
			        builder.append(mc.theWorld.getBlockState(lookAt));
		        } else {
			        builder.append(mc.theWorld.getBlockState(lookAt).getBlock().getLocalizedName());
		        }
            }
            
	        builder.append(']');
            builder.append(EnumChatFormatting.RESET);
        }
        else if (mop.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY && mop.entityHit != null)
        {
            builder.append(EnumChatFormatting.RED);
        	Entity ent = mop.entityHit;
	        builder.append(' ');
	        builder.append('[');
	        builder.append(ent.getName());
	        builder.append(' ');
	        builder.append('(');
	        builder.append((int)ent.lastTickPosX);
	        builder.append(' ');
	        builder.append((int)ent.lastTickPosY);
	        builder.append(' ');
	        builder.append((int)ent.lastTickPosZ);
	        builder.append(')');
	        
	        if(ent instanceof EntityLivingBase) {
	        	builder.append(' ');
	        	builder.append(((EntityLivingBase)ent).getHealth());
	        }
	        
	        builder.append(']');
            builder.append(EnumChatFormatting.RESET);
        }
	}
	
	public boolean canDisplayInfoBar(Minecraft mc, ClientProxy clientProxy) {
		if(!clientProxy.isBuildMode())
			return false;
		
		if(mc.currentScreen == null)
			return true;
		
		if(mc.currentScreen instanceof GuiIngameMenu)
			return true;
		
		return false;
	}
}
