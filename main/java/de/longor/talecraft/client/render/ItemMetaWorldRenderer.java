package de.longor.talecraft.client.render;

import de.longor.talecraft.items.TeleporterItem;
import de.longor.talecraft.proxy.ClientProxy;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;

public class ItemMetaWorldRenderer {
	// CLIENT
	public static ClientProxy clientProxy;
	public static Tessellator tessellator;
	public static WorldRenderer worldrenderer;
	public static double partialTicks;
	// CLIENT.PLAYER
	public static Vec3 playerPosition;
	public static EntityPlayerSP player;
	public static WorldClient world;
	
	// RENDER
	public static void render(Item itemType, ItemStack itemStack) {
		
		if(itemType instanceof TeleporterItem) {
			// renderTeleporterItem(); return;
		}
		
	}
	
	// Currently Useless
	private static void renderTeleporterItem() {
    	float lerp = 1F;
    	float dist = 256;
    	
        Vec3 start = player.getPositionEyes(lerp);
        Vec3 direction = player.getLook(lerp);
        Vec3 end = start.addVector(direction.xCoord * dist, direction.yCoord * dist, direction.zCoord * dist);
    	
        MovingObjectPosition MOP = world.rayTraceBlocks(start, end, false, false, false);
		
		if(MOP == null)
			return;
		
        if(MOP.typeOfHit != MovingObjectType.BLOCK)
        	return;
        
    	// Extract Block Hit
    	BlockPos newPos = MOP.getBlockPos();
    	
    	// Get new Position
    	double nX = newPos.getX();
    	double nZ = newPos.getZ();
    	double nY = newPos.getY() + 1;
		
    	double minX = nX;
    	double minY = nY;
    	double minZ = nZ;
    	double maxX = nX+1;
    	double maxY = nY+1;
    	double maxZ = nZ+1;
    	
    	float r = 1, g = 1, b = 1, a = 1;
    	
    	GlStateManager.disableTexture2D();
    	GlStateManager.disableLighting();
    	GlStateManager.disableColorMaterial();
    	GlStateManager.disableRescaleNormal();
        BoxRenderer.renderBox(tessellator, worldrenderer, minX, minY, minZ, maxX, maxY, maxZ, r, g, b, a);
	}
	
}
