package de.longor.talecraft.client.gui.qad;

import de.longor.talecraft.client.gui.vcui.VCUIRenderer;

public class QADLabel extends QADComponent {
	String text = "[label]";
	int x = 0;
	int y = 0;
	int color = 0xFFFFFFFF;
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
		renderer.getFontRenderer().drawString(text, x, y, color, shadow);
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
	
}
