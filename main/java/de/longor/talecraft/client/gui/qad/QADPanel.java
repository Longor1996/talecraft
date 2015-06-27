package de.longor.talecraft.client.gui.qad;

import java.util.List;

import com.google.common.collect.Lists;

import de.longor.talecraft.client.gui.vcui.VCUIRenderer;

public class QADPanel extends QADComponent {
	int x;
	int y;
	int width;
	int height;
	public List<QADComponent> components;
	
	public boolean enabled;
	public boolean visible;
	
	public QADPanel() {
		components = Lists.newArrayList();
		enabled = true;
		visible = true;
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
	
	@Override
	public void draw(int localMouseX, int localMouseY, float partialTicks, VCUIRenderer renderer) {
		if(!visible)
			return;
		
		renderer.offset(x, y);
		for(QADComponent component : components) {
			component.draw(localMouseX-x, localMouseY-y, partialTicks, renderer);
		}
		renderer.offset(-x, -y);
	}

	@Override
	public void onMouseClicked(int localMouseX, int localMouseY, int mouseButton) {
		if(!enabled) return;
		
		for(QADComponent component : components) {
			component.onMouseClicked(localMouseX-x, localMouseY-y, mouseButton);
		}
	}
	
	@Override
	public void onMouseReleased(int localMouseX, int localMouseY, int state) {
		if(!enabled) return;
		
		for(QADComponent component : components) {
			component.onMouseReleased(localMouseX-x, localMouseY-y, state);
		}
	}
	
	@Override
	public void onMouseClickMove(int localMouseX, int localMouseY, int clickedMouseButton, long timeSinceLastClick) {
		if(!enabled) return;
		
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
