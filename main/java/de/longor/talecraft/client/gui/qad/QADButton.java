package de.longor.talecraft.client.gui.qad;

import com.mojang.realmsclient.dto.RealmsServer.McoServerComparator;

import de.longor.talecraft.client.gui.vcui.VCUIRenderer;
import de.longor.talecraft.client.render.EXTFontRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class QADButton extends QADComponent {
    protected static final ResourceLocation buttonTextures = new ResourceLocation("minecraft:textures/gui/widgets.png");
    
    Runnable clickRunnable = null;
	String text = "[button]";
	int x = 0;
	int y = 0;
	int width;
	int height = 20;
	boolean enabled = true;
    boolean hovered = false;
    

    public QADButton(int x, int y, int width, String text)
    {
        this.text = text;
        
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = 20;
        
        this.enabled = true;
    }
    
    public QADButton setAction(Runnable handler) {
    	clickRunnable = handler;
    	return this;
    }
    
    protected int getHoverState(boolean mouseOver)
    {
        byte b0 = 1;

        if (!this.enabled)
        {
            b0 = 0;
        }
        else if (mouseOver)
        {
            b0 = 2;
        }

        return b0;
    }

	@Override
	public int getX() {
		return x;
	}

	@Override
	public int getY() {
		return y;
	}

	@Override
	public void setX(int x) {
		this.x = x;
	}

	@Override
	public void setY(int y) {
		this.y = y;
	}

    public int getButtonWidth()
    {
        return this.width;
    }

    public void setWidth(int width)
    {
        this.width = width;
    }

	@Override
	public void draw(int localMouseX, int localMouseY, float partialTicks, VCUIRenderer renderer) {
		
        // if (this.visible)
        {
            EXTFontRenderer fontrenderer = renderer.getFontRenderer();
            renderer.bindTexture(buttonTextures);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            this.hovered = localMouseX >= 0 && localMouseY >= 0 && localMouseX < this.width && localMouseY < this.height;
            int k = this.getHoverState(this.hovered);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            GlStateManager.blendFunc(770, 771);
            renderer.drawTexturedModalRectangle(this.x, this.y, 0, 46 + k * 20, this.width / 2, this.height);
            renderer.drawTexturedModalRectangle(this.x + this.width / 2, this.y, 200 - this.width / 2, 46 + k * 20, this.width / 2, this.height);
            // this.mouseDragged(mc, mouseX, mouseY);
            
            int l = 14737632;
            
            if (!this.enabled)
            {
                l = 10526880;
            }
            else if (this.hovered)
            {
                l = 16777120;
            }
            
            fontrenderer.drawCenteredString(text, x + width / 2, y + (height - 8) / 2, l, true);
        }
	}
	
	@Override
	public void onMouseClicked(int i, int j, int mouseButton) {
		
	}
	
	@Override
	public void onMouseReleased(int localMouseX, int localMouseY, int state) {
		boolean hit = localMouseX >= 0 && localMouseY >= 0 && localMouseX < this.width && localMouseY < this.height;
		
		if(state != 0 || !hit)
			return;
		
		playPressSound();
		
		if(clickRunnable != null)
			clickRunnable.run();
	}
	
	@Override
	public void onMouseClickMove(int i, int j, int clickedMouseButton, long timeSinceLastClick) {}

	@Override
	public void onKeyTyped(char typedChar, int typedCode) {}

	@Override
	public void onTickUpdate() {}
	
    public void playPressSound() {
    	SoundHandler soundHandlerIn = Minecraft.getMinecraft().getSoundHandler();
        soundHandlerIn.playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
    }
	
}
