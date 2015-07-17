package de.longor.talecraft.client.render.tileentity;

import org.lwjgl.Sys;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.util.ResourceLocation;
import de.longor.talecraft.blocks.util.tileentity.EmitterBlockTileEntity;
import de.longor.talecraft.blocks.util.tileentity.ImageHologramBlockTileEntity;
import de.longor.talecraft.client.render.renderers.BoxRenderer;
import de.longor.talecraft.proxy.ClientProxy;

public class ImageHologramBlockTileEntityEXTRenderer implements
		IEXTTileEntityRenderer<ImageHologramBlockTileEntity> {
	
	@Override
	public void render(ImageHologramBlockTileEntity tileentity, double posX, double posY, double posZ, float partialTicks) {
		if(!tileentity.isActive()) return;
		
		String locationStr = tileentity.getTextureLocation();
		
		if(locationStr.equalsIgnoreCase("#atlas")) {
			ClientProxy.mc.renderEngine.bindTexture(TextureMap.locationBlocksTexture);
		} else {
			ResourceLocation location = new ResourceLocation(locationStr);
			
			ITextureObject texture = ClientProxy.mc.renderEngine.getTexture(location);
			
			if(texture != null)
		        GlStateManager.bindTexture(texture.getGlTextureId());
			else {
				ClientProxy.mc.renderEngine.bindTexture(location);
				ClientProxy.mc.renderEngine.bindTexture(ClientProxy.textureReslocSelectionBox2);
			}
		}
		
		float x = tileentity.getPos().getX() + 0.5f + tileentity.getHologramOffsetX();
		float y = tileentity.getPos().getY() + 0.5f + tileentity.getHologramOffsetY();
		float z = tileentity.getPos().getZ() + 0.5f + tileentity.getHologramOffsetZ();
		
		float pitch = tileentity.getHologramPitch();
		float yaw = tileentity.getHologramYaw();
		
		float w = tileentity.getHologramWidth() / 2f;
		float h = tileentity.getHologramHeight() / 2f;
		
		float t = 0;
		
		GL11.glPushMatrix();
		
		GL11.glTranslated(x, y, z);
		
		GL11.glRotatef(yaw, 0, 1, 0);
		GL11.glRotatef(pitch, 1, 0, 0);
		
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldrenderer = tessellator.getWorldRenderer();
		BoxRenderer.renderBox(tessellator, worldrenderer, -w, -h, -t, +w, +h, +t, 1, 1, 1, 1);
		
		GL11.glPopMatrix();
		
//		// Debug
//		{
//			GL11.glPushMatrix();
//			GL11.glTranslated(x, y, z);
//			GL11.glScaled(1f/32f, -1f/32f, 1f/32f);
//			ClientProxy.mc.fontRendererObj.drawString("PATH " + locationStr, 0, 32, 0xFFFFFFFF);
//			GL11.glPopMatrix();
//		}
	}

}
