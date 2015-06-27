package de.longor.talecraft.client.gui.qad;

public class QADButtonBox extends QADScrollPanel {
	
	public QADButtonBox(QADButton...buttons) {
		int iy = 0;
		int hb = 20;
		
		for(QADButton button : buttons) {
			components.add(button);
			
			button.y = iy * hb;
			iy++;
		}
		
		this.viewH = buttons.length * hb;
	}
	
}
