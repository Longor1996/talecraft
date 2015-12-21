package de.longor.talecraft.client.render.renderers;

import org.lwjgl.opengl.GL11;

import de.longor.talecraft.TaleCraft;
import de.longor.talecraft.client.render.RenderModeHelper;
import de.longor.talecraft.proxy.ClientProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraftforge.client.IRenderHandler;

public class CustomSkyRenderer extends IRenderHandler {
	public static final CustomSkyRenderer instance = new CustomSkyRenderer();
	private boolean useDebugSky = false;
	
	@Override
	public void render(float partialTicks, WorldClient world, Minecraft mc) {
		int visualizationMode = ((ClientProxy)TaleCraft.instance.proxy).getRenderer().getVisualizationMode();
		
		if(useDebugSky) {
			renderDebugSky(partialTicks, world, mc);
			if(visualizationMode != 0) {
				RenderModeHelper.ENABLE(visualizationMode);
			}
			return;
		}
		
		if(visualizationMode != 0) {
			RenderModeHelper.ENABLE(visualizationMode);
		}
	}
	
	private void renderDebugSky(float partialTicks, WorldClient world, Minecraft mc) {
		GlStateManager.pushAttrib();
		GlStateManager.disableCull();
		GlStateManager.disableDepth();
		GlStateManager.disableFog();
		GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
		
		final float B = 8;
		
		Tessellator tess = Tessellator.getInstance();
		WorldRenderer ren = tess.getWorldRenderer();
		
		ren.startDrawingQuads();
		ren.setTranslation(0, 1, 0);
		ren.setColorRGBA(0, 0, 0, 255);
		
		// TOP
		ren.addVertex(+B, +B, -B);
		ren.addVertex(+B, +B, +B);
		ren.addVertex(-B, +B, +B);
		ren.addVertex(-B, +B, -B);
		// BOTTOM
		ren.addVertex(+B, -B, -B);
		ren.addVertex(+B, -B, +B);
		ren.addVertex(-B, -B, +B);
		ren.addVertex(-B, -B, -B);
		// ??? x
		ren.addVertex(-B, +B, -B);
		ren.addVertex(-B, +B, +B);
		ren.addVertex(-B, -B, +B);
		ren.addVertex(-B, -B, -B);
		// ??? x
		ren.addVertex(+B, +B, +B);
		ren.addVertex(+B, +B, -B);
		ren.addVertex(+B, -B, -B);
		ren.addVertex(+B, -B, +B);
		// ??? z
		ren.addVertex(+B, +B, -B);
		ren.addVertex(-B, +B, -B);
		ren.addVertex(-B, -B, -B);
		ren.addVertex(+B, -B, -B);
		// ??? z
		ren.addVertex(-B, +B, +B);
		ren.addVertex(+B, +B, +B);
		ren.addVertex(+B, -B, +B);
		ren.addVertex(-B, -B, +B);
		// end
		tess.draw();
		ren.setTranslation(0, 0, 0);
		
		GlStateManager.enableCull();
		GlStateManager.enableDepth();
		GlStateManager.popAttrib();
	}

	public void setDebugSky(boolean b) {
		useDebugSky = b;
	}
	
}
