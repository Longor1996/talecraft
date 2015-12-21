package de.longor.talecraft.client.gui.file;

import io.netty.util.internal.StringUtil;

import java.util.List;

import com.google.common.collect.Lists;
import com.sun.xml.internal.bind.v2.model.core.ID;

import de.longor.talecraft.TaleCraft;
import de.longor.talecraft.client.ClientResources;
import de.longor.talecraft.client.gui.qad.QADBoxLabel;
import de.longor.talecraft.client.gui.qad.QADButton;
import de.longor.talecraft.client.gui.qad.QADGuiScreen;
import de.longor.talecraft.client.gui.qad.QADLabel;
import de.longor.talecraft.client.gui.qad.QADPanel;
import de.longor.talecraft.client.gui.qad.QADRectangularComponent;
import de.longor.talecraft.client.gui.qad.QADScrollPanel;
import de.longor.talecraft.proxy.ClientProxy;
import de.longor.talecraft.util.StringUtils;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.Constants.NBT;

public class GuiFileBrowser extends QADGuiScreen {
	private NBTTagCompound currentData;
	private QADRectangularComponent main;
	private QADPanel toolbar;
	
	public GuiFileBrowser(NBTTagCompound data) {
		currentData = data;
	}
	
	public void buildGui() {
		toolbar = this.addComponent(new QADPanel());
		toolbar.setBackgroundColor(0);
		toolbar.setSize(getWidth(), 24);
		toolbar.setPosition(0, 0);
		
		/*
		toolbar.addComponent(new QADButton(1+22*0,2,QADButton.ICON_INVEDIT));
		toolbar.addComponent(new QADButton(1+22*1,2,QADButton.ICON_DELETE));
		toolbar.addComponent(new QADButton(1+22*2,2,QADButton.ICON_SAVE));
		toolbar.addComponent(new QADButton(1+22*3,2,QADButton.ICON_NEW));
		//*/
		
		if(currentData.hasKey("content")) {
			QADScrollPanel scroll = new QADScrollPanel();
			main = scroll;
			int vHeight = 4;
			
			if(currentData.hasKey("parent") && !currentData.getString("location").equals("/")) {
				QADLabel label = new QADLabel("UP", 2, vHeight);
				label.setTooltip("Go to parent directory:",currentData.getString("parent"));
				scroll.addComponent(label);
				
				label.setOnClickHandler(new Runnable() {
					final String path = currentData.getString("parent");
					@Override public void run() {
						ClientProxy.proxy.sendChatMessage("/tc_file open " + path.replace(" ", "%20"));
					}
				});
				
				vHeight += 16;
			}
			
			String directoryTemp = currentData.getString("location");
			if(!directoryTemp.endsWith("/")) {
				directoryTemp = directoryTemp.concat("/");
			}
			final String directory = directoryTemp;
			
			NBTTagList content = currentData.getTagList("content", NBT.TAG_COMPOUND);
			if(content != null && !content.hasNoTags()) {
				
				for(int i = 0; i < content.tagCount(); i++) {
					// data
					final NBTTagCompound file = content.getCompoundTagAt(i);
					final String name = file.getString("name");
					final String type = file.getString("type");
					
					// mutable stuff
					int labelColor = 0xFFFFFFFF;
					Runnable clickHandler = null;
					List<String> tooltip = Lists.newArrayList();
					
					tooltip.add("Name: " + name);
					tooltip.add("Type: " + type);
					
					if(type.equals("dir")) {
						labelColor = 0xFF7F7F00;
						tooltip.add("Entries: " + file.getLong("entries"));
						tooltip.add("Path: " + file.getString("location"));
						
						clickHandler = new Runnable() {
							final String path = file.getString("location");
							@Override public void run() {
								ClientProxy.proxy.sendChatMessage("/tc_file open " + path.replace(" ", "%20"));
							}
						};
					} else if(type.equals("file")) {
						long size = file.getLong("size");
						tooltip.add("Size: " + StringUtils.humanReadableByteCount(size));
						
						boolean canOpen = size <= 32768;
						
						if(!file.getBoolean("flag.r")) {
							canOpen = false;
						}
						
						if(name.endsWith(".zip")) {
							canOpen = false;
						}
						if(name.endsWith(".gz")) {
							canOpen = false;
						}
						if(name.endsWith(".rar")) {
							canOpen = false;
						}
						
						if(canOpen) {
							clickHandler = new Runnable() {
								final String path = file.getString("location");
								@Override public void run() {
									String FILE = directory.concat(path).concat(name).replace(" ", "%20");
									ClientProxy.proxy.sendChatMessage("/tc_file open " + FILE);
								}
							};
						} else {
							labelColor = 0xFF7F0000;
						}
					}
					
					// Create the label!
					QADLabel label = new QADLabel(name, 6, vHeight, labelColor, false);
					scroll.addComponent(label);
					label.setTooltip(tooltip);
					
					if(clickHandler != null) {
						label.setOnClickHandler(clickHandler);
					}
					
					vHeight += 14;
				}
			} else {
				scroll.addComponent(new QADLabel("Empty directory.", 0, vHeight+2));
				vHeight += 14;
			}
			
			vHeight += 2;
			scroll.setViewportHeight(vHeight);
			scroll.setHeight(getHeight());
			scroll.setPosition(0, 24);
		} else if(currentData.hasKey("error")){
			main = new QADBoxLabel(EnumChatFormatting.RED+currentData.getString("error"), 0, 24, getWidth(), getHeight());
		} else {
			QADLabel label = new QADLabel("Go back.", 2, 2);
			label.setTooltip("Go to parent directory:",currentData.getString("parent"));
			main = label;
			
			label.setOnClickHandler(new Runnable() {
				final String path = currentData.getString("parent");
				@Override public void run() {
					ClientProxy.proxy.sendChatMessage("/tc_file open " + path.replace(" ", "%20"));
				}
			});
		}
		
		this.addComponent(main);
	}
	
	public void layoutGui() {
		toolbar.setWidth(getWidth());
		
		if(main.canResize()) {
			main.setSize(getWidth(), getHeight()-24);
		}
		
		if(main instanceof QADBoxLabel) {
			((QADBoxLabel) main).setAlign(1, 1);
		}
		
	}
	
}
