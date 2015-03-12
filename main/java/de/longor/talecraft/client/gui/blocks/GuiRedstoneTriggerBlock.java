package de.longor.talecraft.client.gui.blocks;

import java.util.ArrayList;

import de.longor.talecraft.client.gui.qad.QADButton;
import de.longor.talecraft.client.gui.qad.QADComponent;
import de.longor.talecraft.client.gui.qad.QADGuiScreen;

public class GuiRedstoneTriggerBlock extends QADGuiScreen {
	
	public void buildGui(ArrayList<QADComponent> components) {
		
		// build gui
		
		/*
		for(int i = 0; i < 6; i++) {
			final int I = i;
			components.add(new QADButton(0, 20+i*20, 120, i+" Click me!").setAction(new Runnable() {
				@Override public void run() {
					mc.thePlayer.sendChatMessage(I+" Hello!");
				}
			}));
		}
		//*/
	}
	
	public void layoutGui() {
		
	}
	
}
