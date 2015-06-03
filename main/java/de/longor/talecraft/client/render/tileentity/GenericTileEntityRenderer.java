package de.longor.talecraft.client.render.tileentity;

import java.util.Random;

import org.lwjgl.opengl.GL11;

import de.longor.talecraft.TaleCraft;
import de.longor.talecraft.blocks.ClockBlockTileEntity;
import de.longor.talecraft.client.render.RenderHelper;
import de.longor.talecraft.invoke.IInvokeSource;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GenericTileEntityRenderer<T extends TileEntity> extends TileEntitySpecialRenderer {
    private final ResourceLocation texture;
    private final IEXTTileEntityRenderer<T> extRenderer;
	
    public GenericTileEntityRenderer(String texturePath, IEXTTileEntityRenderer<T> exr) {
    	texture = new ResourceLocation(texturePath);
    	extRenderer = exr;
    }
	
    public GenericTileEntityRenderer(String texturePath) {
    	texture = new ResourceLocation(texturePath);
    	extRenderer = null;
    }

	@Override
	public void renderTileEntityAt(TileEntity p_180535_1_, double posX, double posY, double posZ, float partialTicks, int p_180535_9_) {
		T tile = (T) p_180535_1_;
		
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)posX, (float)posY, (float)posZ);
        
        // bounds
        final float I = 1f / 64f;
        final float A = 63f / 64f;
        
        // render states
        GlStateManager.disableLighting();
        GlStateManager.disableBlend();
        GlStateManager.enableCull();
        // bind texture
        this.bindTexture(texture);
        
        // get tessellator
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        
        // time to render
        
        // top
        worldrenderer.startDrawingQuads();
        worldrenderer.setBrightness(0xEE);
        
        worldrenderer.addVertexWithUV(I, A, A, 0, 0);
        worldrenderer.addVertexWithUV(A, A, A, 1, 0);
        worldrenderer.addVertexWithUV(A, A, I, 1, 1);
        worldrenderer.addVertexWithUV(I, A, I, 0, 1);
        // bottom
        worldrenderer.addVertexWithUV(I, I, I, 0, 0);
        worldrenderer.addVertexWithUV(A, I, I, 1, 0);
        worldrenderer.addVertexWithUV(A, I, A, 1, 1);
        worldrenderer.addVertexWithUV(I, I, A, 0, 1);
        // negative z | north
        worldrenderer.addVertexWithUV(I, A, I, 0, 0);
        worldrenderer.addVertexWithUV(A, A, I, 1, 0);
        worldrenderer.addVertexWithUV(A, I, I, 1, 1);
        worldrenderer.addVertexWithUV(I, I, I, 0, 1);
        // positive z | south
        worldrenderer.addVertexWithUV(A, A, A, 0, 0);
        worldrenderer.addVertexWithUV(I, A, A, 1, 0);
        worldrenderer.addVertexWithUV(I, I, A, 1, 1);
        worldrenderer.addVertexWithUV(A, I, A, 0, 1);
        // positive x | east
        worldrenderer.addVertexWithUV(A, A, I, 0, 0);
        worldrenderer.addVertexWithUV(A, A, A, 1, 0);
        worldrenderer.addVertexWithUV(A, I, A, 1, 1);
        worldrenderer.addVertexWithUV(A, I, I, 0, 1);
        // negative x | west
        worldrenderer.addVertexWithUV(I, A, A, 0, 0);
        worldrenderer.addVertexWithUV(I, A, I, 1, 0);
        worldrenderer.addVertexWithUV(I, I, I, 1, 1);
        worldrenderer.addVertexWithUV(I, I, A, 0, 1);
        tessellator.draw();
        
        if(extRenderer != null) {
        	extRenderer.render(tile, posX, posY, posZ, partialTicks);
        }
        
        /*
        final String TEXT = null; // tile.getStateAsString();
        if(TEXT != null || Boolean.FALSE) {
        	final int TEXT_W = this.getFontRenderer().getStringWidth(TEXT);
        	final float HEX = 1f / 32f;
        	GlStateManager.translate(0.5f, 1.75f, 0.5f);
        	GlStateManager.rotate(180, 1, 0, 0);
        	GlStateManager.scale(HEX, HEX, HEX);
        	GlStateManager.rotate((float)(Minecraft.getMinecraft().thePlayer.rotationYawHead + 180), 0, 1, 0);
        	this.getFontRenderer().drawString(TEXT, -TEXT_W/2, 0, 0xFFFFFFFF);
        }
        //*/
        
        GlStateManager.enableLighting();
        GlStateManager.enableCull();
        
        GlStateManager.popMatrix();
	}
	
	
	
}
