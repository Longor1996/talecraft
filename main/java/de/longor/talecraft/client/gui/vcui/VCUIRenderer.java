package de.longor.talecraft.client.gui.vcui;

import de.longor.talecraft.client.render.EXTFontRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class VCUIRenderer {
	EXTFontRenderer fontRenderer = new EXTFontRenderer();
	FontRenderer nativeFontRenderer;
	RenderItem itemRender;
	GuiScreen guiScreen;
	Minecraft minecraft;
	
	float zLevel = 0;
	int offsetX = 0;
	int offsetY = 0;
	int uiWidth = 0;
	int uiHeight = 0;
	
	public void setCurrentScreen(GuiScreen guiScreen, float zLevel, FontRenderer fontRendererObj, RenderItem itemRender) {
		this.minecraft = guiScreen.mc;
		this.guiScreen = guiScreen;
		
		this.zLevel = zLevel;
		this.uiWidth = guiScreen.width;
		this.uiHeight = guiScreen.height;
		
		this.fontRenderer.setFontRenderer(fontRendererObj);
		this.nativeFontRenderer = fontRendererObj;
		this.itemRender = itemRender;
		
		this.offsetX = 0;
		this.offsetY = 0;
	}
	
	
	
	
	public void drawDefaultBackground() {
		guiScreen.drawDefaultBackground();
	}
	
	public void drawHorizontalLine(int startX, int endX, int y, int color) {
		do_drawHorizontalLine(startX+offsetX, endX+offsetX, y+offsetY, color);
	}
	
	public void drawVerticalLine(int x, int startY, int endY, int color) {
		do_drawVerticalLine(x+offsetX, startY+offsetY, endY+offsetY, color);
	}
	
	public void drawRectangle(int left, int top, int right, int bottom, int color) {
		do_drawRect(left+offsetX, top+offsetY, right+offsetX, bottom+offsetY, color);
	}
	
	public void drawGradientRectangle(int left, int top, int right, int bottom, int startColor, int endColor) {
		do_drawGradientRect(left+offsetX, top+offsetY, right+offsetX, bottom+offsetY, startColor, endColor);
	}
	
	public void drawItemStack(ItemStack item, int x, int y) {
        RenderHelper.enableGUIStandardItemLighting();
        GlStateManager.disableLighting();
        GlStateManager.enableRescaleNormal();
        GlStateManager.enableColorMaterial();
        GlStateManager.enableLighting();
		itemRender.renderItemIntoGUI(item, x+offsetX, y+offsetY);
        GlStateManager.enableLighting();
        GlStateManager.enableDepth();
        RenderHelper.enableStandardItemLighting();
	}
	
	public void drawString(String text, int x, int y, int color, boolean shadow) {
		fontRenderer.drawString(text, x, y, color, shadow);
	}
	
	public void drawString(String text, int x, int y, boolean shadow) {
		fontRenderer.drawString(text, x, y, 0xFFFFFFFF, shadow);
	}
	
	public void drawString(String text, int x, int y, int color) {
		fontRenderer.drawString(text, x, y, color, false);
	}
	
	public void drawString(String text, int x, int y) {
		fontRenderer.drawString(text, x, y, 0xFFFFFFFF, false);
	}
	
	public void drawStringWithShadow(String text, int x, int y, int color) {
		fontRenderer.drawString(text, x, y, color, true);
	}
	
	public void drawStringWithShadow(String text, int x, int y) {
		fontRenderer.drawString(text, x, y, 0xFFFFFFFF, true);
	}
	
	public EXTFontRenderer getFontRenderer() {
		return fontRenderer;
	}
	
	public void bindTexture(ResourceLocation resource) {
		minecraft.getTextureManager().bindTexture(resource);
	}
	
	public void drawTexturedModalRectangle(int x, int y, int textureX, int textureY, int width, int height) {
		do_drawTexturedModalRect(x+offsetX, y+offsetY, textureX, textureY, width, height);
	}
	
	public void drawTexturedModalRectangle(float xCoord, float yCoord, int minU, int minV, int maxU, int maxV) {
		do_drawTexturedModalRect(xCoord, yCoord, minU, minV, maxU, maxV);
	}
	
	public void drawTexturedModalRectangle(int xCoord, int yCoord, TextureAtlasSprite textureSprite, int width, int height) {
		do_drawTexturedModalRect(xCoord+offsetX, yCoord+offsetY, textureSprite, width, height);
	}
	
	public void drawModalRectangleWithCustomSizedTexture(int x, int y, float u, float v, int width, int height, float textureWidth, float textureHeight) {
		do_drawModalRectWithCustomSizedTexture(x+offsetX, y+offsetY, u, v, width, height, textureWidth, textureHeight);
	}
	
	public void drawScaledCustomSizeModalRectangle(int x, int y, float u, float v, int uWidth, int vHeight, int width, int height, float tileWidth, float tileHeight) {
		do_drawScaledCustomSizeModalRect(x+offsetX, y+offsetY, u, v, uWidth, vHeight, width, height, tileWidth, tileHeight);
	}
	
	
	
	
	
    /********************************************************/
    /**                                                    **/
    /** These methods where copied from vanilla Minecraft, **/
    /** because the creator of this mod was very lazy.     **/
    /**                                                    **/
    /********************************************************/
	
	/****/
	private void do_drawHorizontalLine(int startX, int endX, int y, int color)
    {
        if (endX < startX)
        {
            int i1 = startX;
            startX = endX;
            endX = i1;
        }

        do_drawRect(startX, y, endX + 1, y + 1, color);
    }
	
	private void do_drawVerticalLine(int x, int startY, int endY, int color)
    {
        if (endY < startY)
        {
            int i1 = startY;
            startY = endY;
            endY = i1;
        }

        do_drawRect(x, startY + 1, x + 1, endY, color);
    }
    
	private void do_drawRect(int left, int top, int right, int bottom, int color)
    {
        int j1;

        if (left < right)
        {
            j1 = left;
            left = right;
            right = j1;
        }

        if (top < bottom)
        {
            j1 = top;
            top = bottom;
            bottom = j1;
        }

        float f3 = (float)(color >> 24 & 255) / 255.0F;
        float f = (float)(color >> 16 & 255) / 255.0F;
        float f1 = (float)(color >> 8 & 255) / 255.0F;
        float f2 = (float)(color & 255) / 255.0F;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(f, f1, f2, f3);
        worldrenderer.startDrawingQuads();
        worldrenderer.addVertex((double)left, (double)bottom, 0.0D);
        worldrenderer.addVertex((double)right, (double)bottom, 0.0D);
        worldrenderer.addVertex((double)right, (double)top, 0.0D);
        worldrenderer.addVertex((double)left, (double)top, 0.0D);
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }
	
	private void do_drawGradientRect(int left, int top, int right, int bottom, int startColor, int endColor)
    {
        float f = (float)(startColor >> 24 & 255) / 255.0F;
        float f1 = (float)(startColor >> 16 & 255) / 255.0F;
        float f2 = (float)(startColor >> 8 & 255) / 255.0F;
        float f3 = (float)(startColor & 255) / 255.0F;
        float f4 = (float)(endColor >> 24 & 255) / 255.0F;
        float f5 = (float)(endColor >> 16 & 255) / 255.0F;
        float f6 = (float)(endColor >> 8 & 255) / 255.0F;
        float f7 = (float)(endColor & 255) / 255.0F;
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.shadeModel(7425);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.startDrawingQuads();
        worldrenderer.setColorRGBA_F(f1, f2, f3, f);
        worldrenderer.addVertex((double)right, (double)top, (double)this.zLevel);
        worldrenderer.addVertex((double)left, (double)top, (double)this.zLevel);
        worldrenderer.setColorRGBA_F(f5, f6, f7, f4);
        worldrenderer.addVertex((double)left, (double)bottom, (double)this.zLevel);
        worldrenderer.addVertex((double)right, (double)bottom, (double)this.zLevel);
        tessellator.draw();
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }

	private void do_drawTexturedModalRect(int x, int y, int textureX, int textureY, int width, int height)
    {
        float f = 0.00390625F;
        float f1 = 0.00390625F;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.startDrawingQuads();
        worldrenderer.addVertexWithUV((double)(x + 0), (double)(y + height), (double)this.zLevel, (double)((float)(textureX + 0) * f), (double)((float)(textureY + height) * f1));
        worldrenderer.addVertexWithUV((double)(x + width), (double)(y + height), (double)this.zLevel, (double)((float)(textureX + width) * f), (double)((float)(textureY + height) * f1));
        worldrenderer.addVertexWithUV((double)(x + width), (double)(y + 0), (double)this.zLevel, (double)((float)(textureX + width) * f), (double)((float)(textureY + 0) * f1));
        worldrenderer.addVertexWithUV((double)(x + 0), (double)(y + 0), (double)this.zLevel, (double)((float)(textureX + 0) * f), (double)((float)(textureY + 0) * f1));
        tessellator.draw();
    }

	private void do_drawTexturedModalRect(float xCoord, float yCoord, int minU, int minV, int maxU, int maxV)
    {
        float f2 = 0.00390625F;
        float f3 = 0.00390625F;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.startDrawingQuads();
        worldrenderer.addVertexWithUV((double)(xCoord + 0.0F), (double)(yCoord + (float)maxV), (double)this.zLevel, (double)((float)(minU + 0) * f2), (double)((float)(minV + maxV) * f3));
        worldrenderer.addVertexWithUV((double)(xCoord + (float)maxU), (double)(yCoord + (float)maxV), (double)this.zLevel, (double)((float)(minU + maxU) * f2), (double)((float)(minV + maxV) * f3));
        worldrenderer.addVertexWithUV((double)(xCoord + (float)maxU), (double)(yCoord + 0.0F), (double)this.zLevel, (double)((float)(minU + maxU) * f2), (double)((float)(minV + 0) * f3));
        worldrenderer.addVertexWithUV((double)(xCoord + 0.0F), (double)(yCoord + 0.0F), (double)this.zLevel, (double)((float)(minU + 0) * f2), (double)((float)(minV + 0) * f3));
        tessellator.draw();
    }
	
	private void do_drawTexturedModalRect(int xCoord, int yCoord, TextureAtlasSprite textureSprite, int width, int height)
    {
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.startDrawingQuads();
        worldrenderer.addVertexWithUV(xCoord + 0	, yCoord + height	, this.zLevel, textureSprite.getMinU(), textureSprite.getMaxV());
        worldrenderer.addVertexWithUV(xCoord + width, yCoord + height	, this.zLevel, textureSprite.getMaxU(), textureSprite.getMaxV());
        worldrenderer.addVertexWithUV(xCoord + width, yCoord + 0		, this.zLevel, textureSprite.getMaxU(), textureSprite.getMinV());
        worldrenderer.addVertexWithUV(xCoord + 0	, yCoord + 0		, this.zLevel, textureSprite.getMinU(), textureSprite.getMinV());
        tessellator.draw();
    }
	
	private void do_drawModalRectWithCustomSizedTexture(int x, int y, float u, float v, int width, int height, float textureWidth, float textureHeight)
    {
        float f4 = 1.0F / textureWidth;
        float f5 = 1.0F / textureHeight;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.startDrawingQuads();
        worldrenderer.addVertexWithUV((double)x, (double)(y + height), 0.0D, (double)(u * f4), (double)((v + (float)height) * f5));
        worldrenderer.addVertexWithUV((double)(x + width), (double)(y + height), 0.0D, (double)((u + (float)width) * f4), (double)((v + (float)height) * f5));
        worldrenderer.addVertexWithUV((double)(x + width), (double)y, 0.0D, (double)((u + (float)width) * f4), (double)(v * f5));
        worldrenderer.addVertexWithUV((double)x, (double)y, 0.0D, (double)(u * f4), (double)(v * f5));
        tessellator.draw();
    }

	private void do_drawScaledCustomSizeModalRect(int x, int y, float u, float v, int uWidth, int vHeight, int width, int height, float tileWidth, float tileHeight)
    {
        float f4 = 1.0F / tileWidth;
        float f5 = 1.0F / tileHeight;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.startDrawingQuads();
        worldrenderer.addVertexWithUV((double)x, (double)(y + height), 0.0D, (double)(u * f4), (double)((v + (float)vHeight) * f5));
        worldrenderer.addVertexWithUV((double)(x + width), (double)(y + height), 0.0D, (double)((u + (float)uWidth) * f4), (double)((v + (float)vHeight) * f5));
        worldrenderer.addVertexWithUV((double)(x + width), (double)y, 0.0D, (double)((u + (float)uWidth) * f4), (double)(v * f5));
        worldrenderer.addVertexWithUV((double)x, (double)y, 0.0D, (double)(u * f4), (double)(v * f5));
        tessellator.draw();
    }
	
}
