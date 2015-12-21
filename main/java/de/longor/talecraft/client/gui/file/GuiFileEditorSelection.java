package de.longor.talecraft.client.gui.file;

import de.longor.talecraft.client.gui.qad.QADActions;
import de.longor.talecraft.client.gui.qad.QADBoxLabel;
import de.longor.talecraft.client.gui.qad.QADButton;
import de.longor.talecraft.client.gui.qad.QADGuiScreen;
import de.longor.talecraft.client.gui.qad.QADLabel;
import de.longor.talecraft.client.gui.qad.QADPanel;
import de.longor.talecraft.client.gui.qad.QADScrollPanel;
import de.longor.talecraft.client.render.renderables.SelectionBoxRenderer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;

public class GuiFileEditorSelection extends QADGuiScreen {
	private NBTTagCompound currentData;
	private QADScrollPanel selectionBoxContainer;
	private QADPanel toolbar;
	
	public GuiFileEditorSelection(NBTTagCompound data) {
		currentData = data;
	}
	
	public void buildGui() {
		toolbar = this.addComponent(new QADPanel());
		toolbar.setBackgroundColor(0);
		toolbar.setSize(getWidth(), 24);
		toolbar.setPosition(0, 0);
		
		System.out.println("-> " + currentData);
		
		{
			QADBoxLabel label = new QADBoxLabel(
					"Select Editor for "+EnumChatFormatting.YELLOW+currentData.getString("name"),
					0, 0, getWidth(), toolbar.getHeight()
			);
			label.setAlign(1, 1);
			label.setName("toolbar.title");
			toolbar.addComponent(label);
		}
		
		selectionBoxContainer = this.addComponent(new QADScrollPanel());
		selectionBoxContainer.setViewportHeight(2000);
		selectionBoxContainer.setBackground(2);
		
		{
			int yOff = 4;
			int sizeW = 200;
			
			{
				QADButton button = new QADButton("CANCEL", QADButton.ICON_EDITOR_NIL);
				selectionBoxContainer.addComponent(button);
				button.setBounds(0, yOff, sizeW, 24); yOff += 24 + 4;
				button.setAction(QADActions.newBackToGameAction());
			}
			
			{
				QADButton button = new QADButton("Text Editor", QADButton.ICON_EDITOR_TXT);
				selectionBoxContainer.addComponent(button);
				button.setBounds(0, yOff, sizeW, 24); yOff += 24 + 4;
				button.setAction(QADActions.newBackToGameAction()); // TODO
			}
			
			{
				QADButton button = new QADButton("NBT Editor", QADButton.ICON_EDITOR_NBT);
				selectionBoxContainer.addComponent(button);
				button.setBounds(0, yOff, sizeW, 24); yOff += 24 + 4;
				button.setEnabled(false);
				button.setAction(QADActions.newBackToGameAction()); // TODO
			}
			
			{
				QADButton button = new QADButton("Binary Editor", QADButton.ICON_EDITOR_BIN);
				selectionBoxContainer.addComponent(button);
				button.setBounds(0, yOff, sizeW, 24); yOff += 24 + 4;
				button.setEnabled(false);
				button.setAction(QADActions.newBackToGameAction()); // TODO
			}
			
			selectionBoxContainer.setViewportHeight(yOff - 2);
		}
	}
	
	public void layoutGui() {
		toolbar.setWidth(getWidth());
		((QADBoxLabel)toolbar.getComponentByName("toolbar.title")).setWidth(getWidth());
		
		selectionBoxContainer.setSize(200, this.height - 24);
		selectionBoxContainer.setPosition(width/2 - 200/2, 24);
	}
	
}
