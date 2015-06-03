package de.longor.talecraft.client.render;

import com.sun.xml.internal.txw2.TXW;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.WorldRenderer.State;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;

public class BoxRenderer {
	private static final Vec3 UP = new Vec3(0, 1, 0);
	
	public static final void renderBoxLine(Tessellator tessellator, WorldRenderer worldrenderer,
			float x0, float y0, float z0, float x1, float y1, float z1,
			float r, float g, float b, float a) {
		
		float dx = x1 - x0;
		float dy = y1 - y0;
		float dz = z1 - z0;
		
		float lengthSquared = dx*dx + dy*dy + dz*dz;
		if(lengthSquared == 0) return;
		
		float length = (float) Math.sqrt(lengthSquared);
		float nx = dx / length;
		float ny = dy / length;
		float nz = dz / length;
		
        worldrenderer.startDrawingQuads();
        worldrenderer.setColorRGBA_F(r, g, b, a);
        worldrenderer.setBrightness(0xFF);
        float minX,minY,minZ,maxX,maxY,maxZ,tx,ty,tz, size = 0.05f;
		for(float t = 0, step = 1f / 5f; t < length; t += step) {
			tx = x0 + nx*t;
			ty = y0 + ny*t;
			tz = z0 + nz*t;
			
			minX = tx - size;
			minY = ty - size;
			minZ = tz - size;
			maxX = tx + size;
			maxY = ty + size;
			maxZ = tz + size;
			renderBoxEmb(tessellator, worldrenderer, minX, minY, minZ, maxX, maxY, maxZ);
		}
        tessellator.draw();
		
	}
	
	// XXX: Finish Implementation
//	public static final void renderBoxLine(Tessellator tessellator, WorldRenderer worldrenderer,
//			float x0, float y0, float z0, float x1, float y1, float z1,
//			float r, float g, float b, float a) {
//
//		// float ???;
//		// N = Negative / Min
//		// P = Positive / Max
//
//		float dx = x1 - x0;
//		float dy = y1 - y0;
//		float dz = z1 - z0;
//
//		float lengthSquared = dx*dx + dy*dy + dz*dz;
//		if(lengthSquared == 0) return;
//
//		float length = (float) Math.sqrt(lengthSquared);
//
//		Vec3 p0 = new Vec3(x0, y0, z0);
//		Vec3 p1 = new Vec3(x1, y1, z1);
//
//		Vec3 d = new Vec3(dx, dy, dz);
//
//		Vec3 forward = new Vec3(dx/length, dy/length, dz/length);
//		Vec3 up = UP.addVector(0, 0, 0);
//		Vec3 left = up.crossProduct(forward);
//
//		float nnn;
//		float pnn;
//		float pnp;
//		float nnp;
//
//		float npn;
//		float ppn;
//		float ppp;
//		float npp;
//
//        worldrenderer.startDrawingQuads();
//        worldrenderer.setBrightness(0xEE);
//	}
	
	public static final void renderBoxEmb(Tessellator tessellator, WorldRenderer worldrenderer, float minX, float minY, float minZ, float maxX, float maxY, float maxZ) {
        // top
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
	}
	
	
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
	
	public static final void renderBox(Tessellator tessellator, WorldRenderer worldrenderer, AxisAlignedBB aabb, float r, float g, float b, float a) {
		float minX = (float) aabb.minX;
		float minY = (float) aabb.minY;
		float minZ = (float) aabb.minZ;
		float maxX = (float) aabb.maxX;
		float maxY = (float) aabb.maxY;
		float maxZ = (float) aabb.maxZ;
		
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
	
	public static final void renderBox(
			Tessellator tessellator, WorldRenderer worldrenderer,
			float minX, float minY, float minZ,
			float maxX, float maxY, float maxZ,
			float r, float g, float b, float a
	) {
        GlStateManager.color(r, g, b, a);
        
        // top
        worldrenderer.startDrawingQuads();
        
        worldrenderer.setColorRGBA_F(r, g, b, a);
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
			double minX, double minY, double minZ,
			double maxX, double maxY, double maxZ,
			float r, float g, float b, float a
	) {
        worldrenderer.startDrawingQuads();
        
        worldrenderer.setColorRGBA_F(r, g, b, a);
        worldrenderer.setBrightness(0xFF);
        
        // top
        worldrenderer.setNormal(0, 1, 0);
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
        worldrenderer.setColorRGBA_F(1, 1, 1, a);
        worldrenderer.setBrightness(0xEE);
        
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
