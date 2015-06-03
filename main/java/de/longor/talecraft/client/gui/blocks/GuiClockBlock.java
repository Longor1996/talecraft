package de.longor.talecraft.client.gui.blocks;

import java.util.ArrayList;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import de.longor.talecraft.TaleCraft;
import de.longor.talecraft.blocks.ClockBlockTileEntity;
import de.longor.talecraft.blocks.RedstoneTriggerTileEntity;
import de.longor.talecraft.client.gui.TCGuiScreen;
import de.longor.talecraft.client.gui.qad.QADButton;
import de.longor.talecraft.client.gui.qad.QADComponent;
import de.longor.talecraft.client.gui.qad.QADFACTORY;
import de.longor.talecraft.client.gui.qad.QADGuiScreen;
import de.longor.talecraft.client.gui.qad.QADLabel;
import de.longor.talecraft.client.gui.qad.QADTextField;
import de.longor.talecraft.items.WandItem;
import de.longor.talecraft.network.StringNBTCommand;

public class GuiClockBlock extends QADGuiScreen {
	ClockBlockTileEntity tileEntity;
	
	public GuiClockBlock(ClockBlockTileEntity tileEntity) {
		this.tileEntity = tileEntity;
	}
	
	public void buildGui(ArrayList<QADComponent> components) {
		final BlockPos position = tileEntity.getPos();
		
		components.add(new QADLabel("Clock Block @ " + position.getX() + " " + position.getY() + " " + position.getZ(), 2, 2));
		
		int column0x = 4;
		int column0w = 60;
		int column1x = column0x + column0w + 2;
		int column1w = 100;
		int column2x = column1x + column1w + 2;
		int column2w = 100;
		int row0y = 22*1;
		int row1y = 22*2;
		int row2y = 22*3;
		int row3y = 22*4;
		int row4y = 22*5;
		int row5y = 22*6;
		int row6y = 22*7;
		int row7y = 22*8;
		
		{
			StringBuilder b = new StringBuilder(64);
			b.append(EnumChatFormatting.RED).append("Last known State: ").append(EnumChatFormatting.RESET);
			b.append(EnumChatFormatting.BOLD).append(tileEntity.active ? "ON" : "OFF").append(EnumChatFormatting.RESET);
			b.append(", repeat: "+tileEntity.repeat);
			b.append(", speed: "+tileEntity.speed);
			b.append(", time: "+tileEntity.time);
			
			components.add(new QADLabel(b.toString(), column0x, row6y+6));
		}
		
		components.add(new QADLabel(EnumChatFormatting.YELLOW + "Region", column0x, row0y+6));
		components.add(new QADLabel("Repeats", column0x, row1y+6));
		components.add(new QADLabel("Speed", column0x, row2y+6));
		components.add(new QADLabel("Time", column0x, row3y+6));
		
		final QADTextField fieldRepeat = QADFACTORY.createNumberTextField(tileEntity.set_repeat, column1x+2, row1y+2, column1w-4, 1000000, 0);
		final QADTextField fieldSpeed = QADFACTORY.createNumberTextField(tileEntity.set_speed, column1x+2, row2y+2, column1w-4, 20*60, 1);
		final QADTextField fieldTime = QADFACTORY.createNumberTextField(tileEntity.set_time, column1x+2, row3y+2, column1w-4, 20*60*1, 1);
		components.add(fieldRepeat);
		components.add(fieldSpeed);
		components.add(fieldTime);
		
		QADButton setDataButton = QADFACTORY.createButton("Apply", column1x, row4y, column1w, null);
		setDataButton.setAction(new Runnable() {
			@Override public void run() {
				String commandString = "blockdatamerge:"+position.getX() + " " + position.getY() + " " + position.getZ();
				NBTTagCompound commandData = new NBTTagCompound();
				NBTTagCompound invokeData = new NBTTagCompound();
				commandData.setTag("clockInvoke", invokeData);
				
				commandData.setInteger("init_repeat", fieldRepeat.asInteger(10));
				commandData.setInteger("init_speed", fieldSpeed.asInteger(1));
				commandData.setInteger("init_time", fieldTime.asInteger(20));
				
				TaleCraft.instance.simpleNetworkWrapper.sendToServer(new StringNBTCommand(commandString, commandData));
			}
		});
		components.add(setDataButton);
		
		QADButton buttonStart = QADFACTORY.createButton("Start", column0x, row7y, column0w, null);
		buttonStart.setAction(new Runnable() {
			@Override public void run() {
				String commandString = "blockcommand:"+position.getX() + " " + position.getY() + " " + position.getZ();
				NBTTagCompound commandData = new NBTTagCompound();
				commandData.setString("command", "start");
				TaleCraft.instance.simpleNetworkWrapper.sendToServer(new StringNBTCommand(commandString, commandData));
				GuiClockBlock.this.mc.displayGuiScreen(null);
			}
		});
		components.add(buttonStart);
		
		QADButton buttonPause = QADFACTORY.createButton("Pause", column1x, row7y, column1w, null);
		buttonPause.setAction(new Runnable() {
			@Override public void run() {
				String commandString = "blockcommand:"+position.getX() + " " + position.getY() + " " + position.getZ();
				NBTTagCompound commandData = new NBTTagCompound();
				commandData.setString("command", "pause");
				TaleCraft.instance.simpleNetworkWrapper.sendToServer(new StringNBTCommand(commandString, commandData));
				GuiClockBlock.this.mc.displayGuiScreen(null);
			}
		});
		components.add(buttonPause);
		
		QADButton buttonStop = QADFACTORY.createButton("Stop", column2x, row7y, column2w, null);
		buttonStop.setAction(new Runnable() {
			@Override public void run() {
				String commandString = "blockcommand:"+position.getX() + " " + position.getY() + " " + position.getZ();
				NBTTagCompound commandData = new NBTTagCompound();
				commandData.setString("command", "stop");
				TaleCraft.instance.simpleNetworkWrapper.sendToServer(new StringNBTCommand(commandString, commandData));
				GuiClockBlock.this.mc.displayGuiScreen(null);
			}
		});
		components.add(buttonStop);
		
		QADButton setRegionButton = new QADButton(column1x, row0y, column1w, "Set Region");
		setRegionButton.setAction(new Runnable() {
			@Override public void run() {
				EntityPlayer player = Minecraft.getMinecraft().thePlayer;
				int[] bounds = WandItem.getBoundsFromPLAYERorNULL(player);
				
				if(bounds == null){
					Minecraft.getMinecraft().ingameGUI.getChatGUI().addToSentMessages(EnumChatFormatting.RED+"Error: "+EnumChatFormatting.RESET+"Wand selection is invalid.");
					return;
				}
				
				String commandString = "blockdatamerge:"+position.getX() + " " + position.getY() + " " + position.getZ();
				NBTTagCompound commandData = new NBTTagCompound();
				NBTTagCompound invokeData = new NBTTagCompound();
				commandData.setTag("clockInvoke", invokeData);
				invokeData.setString("type", "BlockTriggerInvoke");
				invokeData.setIntArray("bounds", bounds);
				
				TaleCraft.instance.simpleNetworkWrapper.sendToServer(new StringNBTCommand(commandString, commandData));
			}
		});
		components.add(setRegionButton);
		
	}
	
}
