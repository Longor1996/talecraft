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
import de.longor.talecraft.client.gui.qad.QADGuiScreen;
import de.longor.talecraft.client.gui.qad.QADLabel;
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
		
		QADButton setRegionButton = new QADButton(2, 16, 80, "Set Region");
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
				invokeData.setString("type", "blockRegionTrigger");
				invokeData.setIntArray("bounds", bounds);
				
				TaleCraft.instance.simpleNetworkWrapper.sendToServer(new StringNBTCommand(commandString, commandData));
			}
		});
		components.add(setRegionButton);
		
	}
	
}
