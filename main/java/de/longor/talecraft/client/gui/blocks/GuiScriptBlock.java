package de.longor.talecraft.client.gui.blocks;

import java.util.ArrayList;
import java.util.Arrays;

import com.google.common.collect.Lists;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.Constants.NBT;
import de.longor.talecraft.TaleCraft;
import de.longor.talecraft.blocks.util.tileentity.RedstoneTriggerBlockTileEntity;
import de.longor.talecraft.blocks.util.tileentity.RelayBlockTileEntity;
import de.longor.talecraft.blocks.util.tileentity.ScriptBlockTileEntity;
import de.longor.talecraft.client.gui.qad.QADButton;
import de.longor.talecraft.client.gui.qad.QADComponent;
import de.longor.talecraft.client.gui.qad.QADFACTORY;
import de.longor.talecraft.client.gui.qad.QADGuiScreen;
import de.longor.talecraft.client.gui.qad.QADLabel;
import de.longor.talecraft.client.gui.qad.QADTextField;
import de.longor.talecraft.items.WandItem;
import de.longor.talecraft.network.StringNBTCommand;

public class GuiScriptBlock extends QADGuiScreen {
	ScriptBlockTileEntity tileEntity;
	
	public GuiScriptBlock(ScriptBlockTileEntity tileEntity) {
		this.tileEntity = tileEntity;
	}
	
	public void buildGui(ArrayList<QADComponent> components) {
		final BlockPos position = tileEntity.getPos();
		
		components.add(new QADLabel("Script Block @ " + position.getX() + " " + position.getY() + " " + position.getZ(), 2, 2));
		
		components.add(new QADLabel("Script-File", 4, 22+6));
		
		final QADTextField scriptName = QADFACTORY.createTextField(tileEntity.getScriptName(), 4+60+2, 22+2, 120-4);
		scriptName.tooltip = Lists.newArrayList("The file-name of the script to execute.");
		components.add(scriptName);
		
		QADButton buttonApply = QADFACTORY.createButton("Apply", 4+60+120+2, 22, 40, null);
		buttonApply.setAction(new Runnable() {
			@Override public void run() {
				// apply
				{
					String commandString = "blockdatamerge:"+position.getX() + " " + position.getY() + " " + position.getZ();
					NBTTagCompound commandData = new NBTTagCompound();
					NBTTagCompound invokeData = new NBTTagCompound();
					commandData.setTag("scriptInvoke", invokeData);
					
					String text = scriptName.getText();
					invokeData.setString("type", "FileScriptInvoke");
					invokeData.setString("scriptFileName", text);
					
					TaleCraft.instance.simpleNetworkWrapper.sendToServer(new StringNBTCommand(commandString, commandData));
				}
			}
		});
		buttonApply.setTooltip("Saves the settings.", "There is no auto-save,","so make sure to press this button.");
		components.add(buttonApply);
		
		QADButton buttonReload = QADFACTORY.createButton("Reload Script", 2, 48, 90, null);
		buttonReload.setAction(new Runnable() {
			@Override public void run() {
				String commandString = "blockcommand:"+position.getX() + " " + position.getY() + " " + position.getZ();
				NBTTagCompound commandData = new NBTTagCompound();
				commandData.setString("command", "reload");
				TaleCraft.instance.simpleNetworkWrapper.sendToServer(new StringNBTCommand(commandString, commandData));
				GuiScriptBlock.this.mc.displayGuiScreen(null);
			}
		});
		buttonReload.setTooltip("Prompts the server to reload the script.");
		components.add(buttonReload);
		
		QADButton buttonExecute = QADFACTORY.createButton("Execute Script", 2 + 90 + 2, 48, 90, null);
		buttonExecute.setAction(new Runnable() {
			@Override public void run() {
				String commandString = "blockcommand:"+position.getX() + " " + position.getY() + " " + position.getZ();
				NBTTagCompound commandData = new NBTTagCompound();
				commandData.setString("command", "execute");
				TaleCraft.instance.simpleNetworkWrapper.sendToServer(new StringNBTCommand(commandString, commandData));
				GuiScriptBlock.this.mc.displayGuiScreen(null);
			}
		});
		buttonExecute.setTooltip("Prompts the server to execute the script.");
		components.add(buttonExecute);
		
		QADButton buttonReloadExecute = QADFACTORY.createButton("R+E", 4+60+120+2, 48, 40, null);
		buttonReloadExecute.setAction(new Runnable() {
			@Override public void run() {
				String commandString = "blockcommand:"+position.getX() + " " + position.getY() + " " + position.getZ();
				NBTTagCompound commandData = new NBTTagCompound();
				commandData.setString("command", "reloadexecute");
				TaleCraft.instance.simpleNetworkWrapper.sendToServer(new StringNBTCommand(commandString, commandData));
				GuiScriptBlock.this.mc.displayGuiScreen(null);
			}
		});
		buttonReloadExecute.setTooltip("Prompts the server to reload and then execute the script.");
		components.add(buttonReloadExecute);
	}
	
}
