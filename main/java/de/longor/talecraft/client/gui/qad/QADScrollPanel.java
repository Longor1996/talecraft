package de.longor.talecraft.client.gui.qad;

import java.util.List;

import com.google.common.collect.Lists;

import de.longor.talecraft.TaleCraft;
import de.longor.talecraft.client.gui.vcui.VCUIRenderer;

public class QADScrollPanel extends QADComponent {
	int x;
	int y;
	int width;
	int height;
	public List<QADComponent> components;
	
	int viewY; // position of the view
	int viewH; // height of the content
	
	public boolean enabled;
	public boolean visible;
	
	public QADScrollPanel() {
		components = Lists.newArrayList();
		enabled = true;
		visible = true;
		
		viewY = 0;
		viewH = 300;
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

	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public void setSize(int w, int h) {
		this.width = w;
		this.height = h;
		
		if(height > viewH) {
			height = viewH;
		}
	}
	
	@Override
	public void draw(int localMouseX, int localMouseY, float partialTicks, VCUIRenderer renderer) {
		if(!visible)
			return;
		
		renderer.drawRectangle(x, y, x+width, y+height, 0x80000000);
		
		localMouseY += viewY;
		
		renderer.pushScissor(x,y,width,height);
		renderer.offset(x, y-viewY);
		for(QADComponent component : components) {
			component.draw(localMouseX-component.getX(), localMouseY-component.getY(), partialTicks, renderer);
		}
		renderer.offset(-x, -y+viewY);
		renderer.popScissor();
		
		renderer.drawGradientRectangle(x, y, x+width-4, y+8, 0xFF000000, 0);
		renderer.drawGradientRectangle(x, y+height-8, x+width-4, y+height, 0, 0xFF000000);
		
		// draw scroll point
		{
			// compute what part of the view we are looking at right now
			float scrollStart = viewY;
			
			if(viewY + height >= viewH) {
				viewY = viewH - height;
			}
			
			float scrollEnd = viewY + height;
			float height = this.height;
			
			if(scrollEnd >= viewH) {
				scrollEnd = viewH;
			}
			
			int __scST = (int) ((scrollStart / viewH) * height);
			int __scED = (int) ((scrollEnd / viewH) * height);
			
			renderer.drawRectangle(x+width-4, y, x+width, y+this.height, 0x80000000);
			renderer.drawRectangle(x+width-4, y+__scST, x+width, y+__scED, 0xFFFFFFFF);
		}
	}

	@Override
	public void onMouseClicked(int localMouseX, int localMouseY, int mouseButton) {
		if(!enabled) return;
		
		localMouseX -= x;
		localMouseY -= y;
		localMouseY += viewY;
		
		for(QADComponent component : components) {
			component.onMouseClicked(localMouseX, localMouseY, mouseButton);
		}
	}
	
	@Override
	public void onMouseReleased(int localMouseX, int localMouseY, int state) {
		if(!enabled) return;
		
		localMouseX -= x;
		localMouseY -= y;
		localMouseY += viewY;
		
		for(QADComponent component : components) {
			component.onMouseReleased(localMouseX, localMouseY, state);
		}
	}
	
	@Override
	public void onMouseClickMove(int localMouseX, int localMouseY, int clickedMouseButton, long timeSinceLastClick) {
		if(!enabled) return;
		
		if(clickedMouseButton != 0 && isPointInside(localMouseX+x, localMouseY+y)) {
			float normalized = ((float)localMouseY / (float)height); // 0..1
			float scaled = (normalized * viewH);
			
			float scrollStart = viewY;
			float scrollEnd = viewY + height;
			
			float __scST = (scrollStart / (float)viewH) * (float)height;
			float __scED = (scrollEnd / (float)viewH) * (float)height;
			
			float barHeight = (__scED - __scST) * ((float)viewH/(float)height);
			float barHalfHeight = barHeight / 2f;
			
			viewY = (int) (scaled - barHalfHeight); // 0..viewH
			
			if(viewY < 0) {
				viewY = 0;
			}
			
			if(viewY + height >= viewH) {
				viewY = viewH - height;
			}
		}
		
		for(QADComponent component : components) {
			component.onMouseClickMove(localMouseX-x, localMouseY-y, clickedMouseButton, timeSinceLastClick);
		}
	}
	
	@Override
	public void onKeyTyped(char typedChar, int typedCode) {
		if(!enabled) return;
		
		for(QADComponent component : components) {
			component.onKeyTyped(typedChar, typedCode);
		}
	}
	
	@Override
	public void onTickUpdate() {
		if(!enabled) return;
		
		for(QADComponent component : components) {
			component.onTickUpdate();
		}
	}
	
	@Override
	public boolean isPointInside(int mouseX, int mouseY) {
		int localMouseX = mouseX - x;
		int localMouseY = mouseY - y;
		return localMouseX >= 0 && localMouseY >= 0 && localMouseX < this.width && localMouseY < this.height;
	}
	
	public List<String> getTooltip(int mouseX, int mouseY) {
		List<String> tooltip = null;
		
		for(QADComponent component : components) {
			tooltip = component.getTooltip(mouseX-x, mouseY-y);
			if(tooltip != null) break;
		}
		
		return tooltip;
	}
	
}
