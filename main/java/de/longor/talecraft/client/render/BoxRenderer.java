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
        worldrenderer.setBrightness(0xEE);
        
        worldrenderer.setColorRGBA_F(r, g, b, a);
        
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
        // top
        worldrenderer.startDrawingQuads();
        worldrenderer.setBrightness(0xEE);
        
        worldrenderer.setColorRGBA_F(0, 1, 1, a); worldrenderer.addVertexWithUV(minX, maxY, maxZ, 0, 0);
        worldrenderer.setColorRGBA_F(1, 1, 1, a); worldrenderer.addVertexWithUV(maxX, maxY, maxZ, 1, 0);
        worldrenderer.setColorRGBA_F(1, 1, 0, a); worldrenderer.addVertexWithUV(maxX, maxY, minZ, 1, 1);
        worldrenderer.setColorRGBA_F(0, 1, 0, a); worldrenderer.addVertexWithUV(minX, maxY, minZ, 0, 1);
        // bottom
        worldrenderer.setColorRGBA_F(0, 0, 0, a); worldrenderer.addVertexWithUV(minX, minY, minZ, 0, 0);
        worldrenderer.setColorRGBA_F(1, 0, 0, a); worldrenderer.addVertexWithUV(maxX, minY, minZ, 1, 0);
        worldrenderer.setColorRGBA_F(1, 0, 1, a); worldrenderer.addVertexWithUV(maxX, minY, maxZ, 1, 1);
        worldrenderer.setColorRGBA_F(0, 0, 1, a); worldrenderer.addVertexWithUV(minX, minY, maxZ, 0, 1);
        // negative z | north
        worldrenderer.setColorRGBA_F(0, 1, 0, a); worldrenderer.addVertexWithUV(minX, maxY, minZ, 0, 0);
        worldrenderer.setColorRGBA_F(1, 1, 0, a); worldrenderer.addVertexWithUV(maxX, maxY, minZ, 1, 0);
        worldrenderer.setColorRGBA_F(1, 0, 0, a); worldrenderer.addVertexWithUV(maxX, minY, minZ, 1, 1);
        worldrenderer.setColorRGBA_F(0, 0, 0, a); worldrenderer.addVertexWithUV(minX, minY, minZ, 0, 1);
        // positive z | south
        worldrenderer.setColorRGBA_F(1, 1, 1, a); worldrenderer.addVertexWithUV(maxX, maxY, maxZ, 0, 0);
        worldrenderer.setColorRGBA_F(0, 1, 1, a); worldrenderer.addVertexWithUV(minX, maxY, maxZ, 1, 0);
        worldrenderer.setColorRGBA_F(0, 0, 1, a); worldrenderer.addVertexWithUV(minX, minY, maxZ, 1, 1);
        worldrenderer.setColorRGBA_F(1, 0, 1, a); worldrenderer.addVertexWithUV(maxX, minY, maxZ, 0, 1);
        // positive x | east
        worldrenderer.setColorRGBA_F(1, 1, 0, a); worldrenderer.addVertexWithUV(maxX, maxY, minZ, 0, 0);
        worldrenderer.setColorRGBA_F(1, 1, 1, a); worldrenderer.addVertexWithUV(maxX, maxY, maxZ, 1, 0);
        worldrenderer.setColorRGBA_F(1, 0, 1, a); worldrenderer.addVertexWithUV(maxX, minY, maxZ, 1, 1);
        worldrenderer.setColorRGBA_F(1, 0, 0, a); worldrenderer.addVertexWithUV(maxX, minY, minZ, 0, 1);
        // negative x | west
        worldrenderer.setColorRGBA_F(0, 1, 1, a); worldrenderer.addVertexWithUV(minX, maxY, maxZ, 0, 0);
        worldrenderer.setColorRGBA_F(0, 1, 0, a); worldrenderer.addVertexWithUV(minX, maxY, minZ, 1, 0);
        worldrenderer.setColorRGBA_F(0, 0, 0, a); worldrenderer.addVertexWithUV(minX, minY, minZ, 1, 1);
        worldrenderer.setColorRGBA_F(0, 0, 1, a); worldrenderer.addVertexWithUV(minX, minY, maxZ, 0, 1);
        tessellator.draw();
	}
	
	
}
