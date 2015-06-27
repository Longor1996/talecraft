package de.longor.talecraft.client.gui.qad;

import java.util.List;

import de.longor.talecraft.client.gui.vcui.VCUIRenderer;

public class QADLabel extends QADComponent {
	String text = "[label]";
	int x = 0;
	int y = 0;
	int color = 0xFFFFFFFF;
	int lastKnownWidth = 0;
	int lastKnownHeight = 0;
	boolean shadow = true;
	
	public QADLabel(String text, int x, int y) {
		this.text = text;
		this.x = x;
		this.y = y;
	}
	
	public QADLabel(String text, int x, int y, int color) {
		this.text = text;
		this.x = x;
		this.y = y;
		this.color = color;
	}
	
	public QADLabel(String text, int x, int y, boolean shadow) {
		this.text = text;
		this.x = x;
		this.y = y;
		this.shadow = shadow;
	}
	
	public QADLabel(String text, int x, int y, int color, boolean shadow) {
		this.text = text;
		this.x = x;
		this.y = y;
		this.color = color;
		this.shadow = shadow;
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

	@Override
	public void draw(int localMouseX, int localMouseY, float partialTicks, VCUIRenderer renderer) {
		lastKnownWidth = renderer.getFontRenderer().stringWidth(text);
		lastKnownHeight = renderer.getFontRenderer().fr.FONT_HEIGHT;
		renderer.drawString(text, x, y, color, shadow);
	}

	@Override
	public void onMouseClicked(int i, int j, int mouseButton) {}

	@Override
	public void onMouseReleased(int i, int j, int state) {}

	@Override
	public void onMouseClickMove(int i, int j, int clickedMouseButton, long timeSinceLastClick) {}

	@Override
	public void onKeyTyped(char typedChar, int typedCode) {}

	@Override
	public void onTickUpdate() {}
    
	@Override
	public boolean isPointInside(int mouseX, int mouseY) {
		if(lastKnownWidth == 0 || lastKnownHeight == 0)
			return false;
		
		int localMouseX = mouseX - x;
		int localMouseY = mouseY - y;
		return localMouseX >= 0 && localMouseY >= 0 && localMouseX < lastKnownWidth && localMouseY < lastKnownHeight;
	}
	
	public List<String> getTooltip(int mouseX, int mouseY) {
		return getTooltip();
	}
	
}
