package de.longor.talecraft.client.gui.qad;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import de.longor.talecraft.TaleCraft;
import de.longor.talecraft.TaleCraftBlocks;
import de.longor.talecraft.client.gui.vcui.VCUIComponent;
import de.longor.talecraft.client.gui.vcui.VCUIRenderer;
import de.longor.talecraft.proxy.ClientProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.fml.relauncher.Side;

/**
 * A class that is to be used to simplify GUIScreen creation.
 **/
@SideOnly(Side.CLIENT)
public class QADGuiScreen extends GuiScreen {
	public static final VCUIRenderer instance = new VCUIRenderer();
	public ArrayList<QADComponent> components;
	public GuiScreen returnScreen;
	private GuiScreen behindScreen;
	
	public QADGuiScreen() {
		super.allowUserInput = false;
		components = null;
		behindScreen = null;
	}
	
	public void buildGui(ArrayList<QADComponent> components) {
		// dont do anything, let the extended classes do their thing!
	}
	
	public void layoutGui() {
		
	}
	
	public void updateGui() {
		
	}
	
	/** ********************************* **/
	/**                                   **/
	/** Everything following is final and **/
	/** should not be overriden.          **/
	/**                                   **/
	/** ********************************* **/
	/****/
	
	@Override
    public final boolean doesGuiPauseGame() {
        return true;
    }
	
	@Override
    public final void initGui() {
		// TaleCraft.logger.info("Gui.init() -> " + this.getClass().getName());
    	Keyboard.enableRepeatEvents(true);
		
    	if(components == null) {
    		components = new ArrayList<QADComponent>();
    		
    		try {
    			buildGui(components);
    		} catch(Exception e) {
    			e.printStackTrace();
    		}
    	}
    	
    	layoutGui();
    }
	
	@Override
    public final void onGuiClosed() {
		// TaleCraft.logger.info("Gui.close() -> " + this.getClass().getName());
		Keyboard.enableRepeatEvents(false);
		
		if(behindScreen != null) {
			TaleCraft.proxy.asClient().sheduleClientTickTask(new Runnable() {
				@Override public void run() {
					mc.displayGuiScreen(behindScreen);
				}
			});
		}
    }
	
	@Override
    protected final void mouseClicked(int mouseX, int mouseY, int mouseButton) {
		for(QADComponent component : components) {
			component.onMouseClicked(mouseX-component.getX(), mouseY-component.getY(), mouseButton);
		}
    }
	
	@Override
    protected final void mouseReleased(int mouseX, int mouseY, int state) {
		for(QADComponent component : components) {
			component.onMouseReleased(mouseX-component.getX(), mouseY-component.getY(), state);
		}
    }
	
	@Override
    protected final void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
		for(QADComponent component : components) {
			component.onMouseClickMove(mouseX-component.getX(), mouseY-component.getY(), clickedMouseButton, timeSinceLastClick);
		}
    }
	
	@Override
	public final void keyTyped(char typedChar, int typedCode) {
		if(typedCode == Keyboard.KEY_ESCAPE) {
			mc.displayGuiScreen(returnScreen);
			mc.inGameHasFocus = returnScreen == null;
			return;
		}
		
		for(QADComponent component : components) {
			component.onKeyTyped(typedChar, typedCode);
		}
	}
	
	@Override
    public final void updateScreen() {
		this.updateGui();
		
		for(QADComponent component : components) {
			component.onTickUpdate();
		}
    }
	
	@Override
    public final void drawScreen(int mouseX, int mouseY, float partialTicks) {
		if(behindScreen != null) {
			behindScreen.drawScreen(-9999, -9999, partialTicks);
		}
		
		instance.setCurrentScreen(this, this.zLevel, this.fontRendererObj, this.itemRender);
		instance.drawDefaultBackground();
		
		if(Boolean.TRUE.booleanValue() && Mouse.isInsideWindow()) {
			final int color = 0x1ACCEEFF;
			instance.drawHorizontalLine(0, width, mouseY, color);
			instance.drawVerticalLine(mouseX, -1, height, color);
		}
		
		for(QADComponent component : components) {
			component.draw(mouseX-component.getX(), mouseY-component.getY(), partialTicks, instance);
		}
		
		for(QADComponent component : components) {
			if(component.isPointInside(mouseX, mouseY)) {
				List<String> text = component.getTooltip(mouseX, mouseY);
				
				if(text != null) {
					this.drawHoveringText(text, mouseX, mouseY);
					break;
				}
			}
		}
		
    }
	
	public void setBehind(GuiScreen behind) {
		behindScreen = behind;
	}
	
	public GuiScreen getBehind() {
		return behindScreen;
	}
	
}
