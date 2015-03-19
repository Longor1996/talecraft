package de.longor.talecraft.client.render;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;

public class BoxRenderer {
	
	public static final void renderBox(Tessellator tessellator, WorldRenderer worldrenderer, float minX, float minY, float minZ, float maxX, float maxY, float maxZ) {
        // top
        worldrenderer.startDrawingQuads();
        worldrenderer.setBrightness(0xEE);
        
        worldrenderer.addVertexWithUV(minX, maxY, maxZ, 0, 0);
        worldrenderer.addVertexWithUV(maxX, maxY, maxZ, 1, 0);
        worldrenderer.addVertexWithUV(maxX, maxY, minZ, 1, 1);
        worldrenderer.addVertexWithUV(minX, maxY, minZ, 0, 1);
        // bottom
        worldrenderer.addVertexWithUV(minX, minY, minZ, 0, 0);
        worldrenderer.addVertexWithUV(maxX, minY, minZ, 1, 0);
        worldrenderer.addVertexWithUV(maxX, minY, maxZ, 1, 1);
        worldrenderer.addVertexWithUV(minX, minY, maxZ, 0, 1);
        // negative z | north
        worldrenderer.addVertexWithUV(minX, maxY, minZ, 0, 0);
        worldrenderer.addVertexWithUV(maxX, maxY, minZ, 1, 0);
        worldrenderer.addVertexWithUV(maxX, minY, minZ, 1, 1);
        worldrenderer.addVertexWithUV(minX, minY, minZ, 0, 1);
        // positive z | south
        worldrenderer.addVertexWithUV(maxX, maxY, maxZ, 0, 0);
        worldrenderer.addVertexWithUV(minX, maxY, maxZ, 1, 0);
        worldrenderer.addVertexWithUV(minX, minY, maxZ, 1, 1);
        worldrenderer.addVertexWithUV(maxX, minY, maxZ, 0, 1);
        // positive x | east
        worldrenderer.addVertexWithUV(maxX, maxY, minZ, 0, 0);
        worldrenderer.addVertexWithUV(maxX, maxY, maxZ, 1, 0);
        worldrenderer.addVertexWithUV(maxX, minY, maxZ, 1, 1);
        worldrenderer.addVertexWithUV(maxX, minY, minZ, 0, 1);
        // negative x | west
        worldrenderer.addVertexWithUV(minX, maxY, maxZ, 0, 0);
        worldrenderer.addVertexWithUV(minX, maxY, minZ, 1, 0);
        worldrenderer.addVertexWithUV(minX, minY, minZ, 1, 1);
        worldrenderer.addVertexWithUV(minX, minY, maxZ, 0, 1);
        tessellator.draw();
	}
	
	public static final void renderBox(
			Tessellator tessellator, WorldRenderer worldrenderer,
			float minX, float minY, float minZ,
			float maxX, float maxY, float maxZ,
			float r, float g, float b, float a
	) {
        // top
        worldrenderer.startDrawingQuads();
        
        worldrenderer.setColorRGBA_F(r, g, b, a);
        worldrenderer.setColorRGBA_F(1, 0, 0, 1);
        worldrenderer.setBrightness(0xEE);
        
        worldrenderer.addVertexWithUV(minX, maxY, maxZ, 0, 0);
        worldrenderer.addVertexWithUV(maxX, maxY, maxZ, 1, 0);
        worldrenderer.addVertexWithUV(maxX, maxY, minZ, 1, 1);
        worldrenderer.addVertexWithUV(minX, maxY, minZ, 0, 1);
        // bottom
        worldrenderer.addVertexWithUV(minX, minY, minZ, 0, 0);
        worldrenderer.addVertexWithUV(maxX, minY, minZ, 1, 0);
        worldrenderer.addVertexWithUV(maxX, minY, maxZ, 1, 1);
        worldrenderer.addVertexWithUV(minX, minY, maxZ, 0, 1);
        // negative z | north
        worldrenderer.addVertexWithUV(minX, maxY, minZ, 0, 0);
        worldrenderer.addVertexWithUV(maxX, maxY, minZ, 1, 0);
        worldrenderer.addVertexWithUV(maxX, minY, minZ, 1, 1);
        worldrenderer.addVertexWithUV(minX, minY, minZ, 0, 1);
        // positive z | south
        worldrenderer.addVertexWithUV(maxX, maxY, maxZ, 0, 0);
        worldrenderer.addVertexWithUV(minX, maxY, maxZ, 1, 0);
        worldrenderer.addVertexWithUV(minX, minY, maxZ, 1, 1);
        worldrenderer.addVertexWithUV(maxX, minY, maxZ, 0, 1);
        // positive x | east
        worldrenderer.addVertexWithUV(maxX, maxY, minZ, 0, 0);
        worldrenderer.addVertexWithUV(maxX, maxY, maxZ, 1, 0);
        worldrenderer.addVertexWithUV(maxX, minY, maxZ, 1, 1);
        worldrenderer.addVertexWithUV(maxX, minY, minZ, 0, 1);
        // negative x | west
        worldrenderer.addVertexWithUV(minX, maxY, maxZ, 0, 0);
        worldrenderer.addVertexWithUV(minX, maxY, minZ, 1, 0);
        worldrenderer.addVertexWithUV(minX, minY, minZ, 1, 1);
        worldrenderer.addVertexWithUV(minX, minY, maxZ, 0, 1);
        tessellator.draw();
	}
	
	public static final void renderSelectionBox(
			Tessellator tessellator, WorldRenderer worldrenderer,
			float minX, float minY, float minZ,
			float maxX, float maxY, float maxZ, float a
	) {
		float U = Math.round(maxX - minX);
		float V = Math.round(maxY - minY);
		float W = Math.round(maxZ - minZ);
		
        // top
        worldrenderer.startDrawingQuads();
        worldrenderer.setBrightness(0xEE);
        worldrenderer.setColorRGBA_F(1, 1, 1, a);
        
        worldrenderer.addVertexWithUV(minX, maxY, maxZ, 0, 0);
        worldrenderer.addVertexWithUV(maxX, maxY, maxZ, U, 0);
        worldrenderer.addVertexWithUV(maxX, maxY, minZ, U, W);
        worldrenderer.addVertexWithUV(minX, maxY, minZ, 0, W);
        // bottom
        worldrenderer.addVertexWithUV(minX, minY, minZ, 0, 0);
        worldrenderer.addVertexWithUV(maxX, minY, minZ, U, 0);
        worldrenderer.addVertexWithUV(maxX, minY, maxZ, U, W);
        worldrenderer.addVertexWithUV(minX, minY, maxZ, 0, W);
        // negative z | north
        worldrenderer.addVertexWithUV(minX, maxY, minZ, 0, 0);
        worldrenderer.addVertexWithUV(maxX, maxY, minZ, U, 0);
        worldrenderer.addVertexWithUV(maxX, minY, minZ, U, V);
        worldrenderer.addVertexWithUV(minX, minY, minZ, 0, V);
        // positive z | south
        worldrenderer.addVertexWithUV(maxX, maxY, maxZ, 0, 0);
        worldrenderer.addVertexWithUV(minX, maxY, maxZ, U, 0);
        worldrenderer.addVertexWithUV(minX, minY, maxZ, U, V);
        worldrenderer.addVertexWithUV(maxX, minY, maxZ, 0, V);
        // positive x | east
        worldrenderer.addVertexWithUV(maxX, maxY, minZ, 0, 0);
        worldrenderer.addVertexWithUV(maxX, maxY, maxZ, W, 0);
        worldrenderer.addVertexWithUV(maxX, minY, maxZ, W, V);
        worldrenderer.addVertexWithUV(maxX, minY, minZ, 0, V);
        // negative x | west
        worldrenderer.addVertexWithUV(minX, maxY, maxZ, 0, 0);
        worldrenderer.addVertexWithUV(minX, maxY, minZ, W, 0);
        worldrenderer.addVertexWithUV(minX, minY, minZ, W, V);
        worldrenderer.addVertexWithUV(minX, minY, maxZ, 0, V);
        tessellator.draw();
	}
	
	
}
