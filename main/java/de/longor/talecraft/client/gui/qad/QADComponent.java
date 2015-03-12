package de.longor.talecraft.client.gui.qad;

import de.longor.talecraft.client.gui.vcui.VCUIRenderer;

public abstract class QADComponent {
	public abstract int getX();
	public abstract int getY();
	
	public abstract void setX(int x);
	public abstract void setY(int y);
	
	public abstract void draw(int localMouseX, int localMouseY, float partialTicks, VCUIRenderer renderer);
	
	public abstract void onMouseClicked(int localMouseX, int localMouseY, int mouseButton);
	public abstract void onMouseReleased(int localMouseX, int localMouseY, int state);
	public abstract void onMouseClickMove(int localMouseX, int localMouseY, int clickedMouseButton, long timeSinceLastClick);
	public abstract void onKeyTyped(char typedChar, int typedCode);
	public abstract void onTickUpdate();
}
