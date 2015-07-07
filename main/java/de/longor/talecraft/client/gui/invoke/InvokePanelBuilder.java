package de.longor.talecraft.client.gui.invoke;

import java.util.ArrayList;

import scala.tools.nsc.settings.Final;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import de.longor.talecraft.TaleCraft;
import de.longor.talecraft.client.gui.blocks.GuiScriptBlock;
import de.longor.talecraft.client.gui.qad.QADButton;
import de.longor.talecraft.client.gui.qad.QADComponent;
import de.longor.talecraft.client.gui.qad.QADFACTORY;
import de.longor.talecraft.client.gui.qad.QADTextField;
import de.longor.talecraft.invoke.BlockTriggerInvoke;
import de.longor.talecraft.invoke.CommandInvoke;
import de.longor.talecraft.invoke.FileScriptInvoke;
import de.longor.talecraft.invoke.IInvoke;
import de.longor.talecraft.invoke.NullInvoke;
import de.longor.talecraft.items.WandItem;
import de.longor.talecraft.network.StringNBTCommand;
import de.longor.talecraft.proxy.ClientProxy;

public class InvokePanelBuilder {
	public static final int INVOKE_TYPE_EDIT_ALLOWALL = -1;
	public static final int INVOKE_TYPE_EDIT_ALLOW_NULL = 1;
	public static final int INVOKE_TYPE_EDIT_ALLOW_BLOCKTRIGGER = 2;
	public static final int INVOKE_TYPE_EDIT_ALLOW_SCRIPTFILE = 4;
	public static final int INVOKE_TYPE_EDIT_ALLOW_SCRIPTEMBEDDED = 8;
	public static final int INVOKE_TYPE_EDIT_ALLOW_COMMAND = 16;
	
	public static final ResourceLocation invokeEditIconLocation = new ResourceLocation("talecraft:textures/gui/invokeedit.png");
	public static final ResourceLocation playIconLocation = new ResourceLocation("talecraft:textures/gui/play.png");
	
	/**
	 * Maximum Width: 20 + ?
	 **/
	public static final void build(GuiScreen screen, ArrayList<QADComponent> components, int ox, int oy, IInvoke invoke, final IInvokeHolder holder, int invokeTypeFlags) {
		
		// TODO: IMPLEMENT THIS BUTTON!
		if(invokeTypeFlags != 0) {
			QADButton button = new QADButton(ox, oy, 20, "");
			button.setEnabled(true);
			button.setAction(new InvokeSwitchAction(invokeTypeFlags, holder, screen));
			button.setIcon(invokeEditIconLocation);
			button.setTooltip("Change Invoke Type");
			components.add(button);
			ox += 20 + 2;
		}
		
		if(invoke == null || invoke instanceof NullInvoke) {
			components.add(QADFACTORY.createLabel("Null Invoke", ox, oy + 6));
			return;
		}
		
		if(invoke instanceof CommandInvoke) {
			build_command(components, ox, oy, (CommandInvoke) invoke, holder);
			return;
		}
		
		if(invoke instanceof BlockTriggerInvoke) {
			build_blocktrigger(components, ox, oy, holder);
			return;
		}
		
		if(invoke instanceof FileScriptInvoke) {
			build_filescript(components, ox, oy, (FileScriptInvoke) invoke, holder);
		}
		
	}
	
	private static void build_command(ArrayList<QADComponent> components,
			int ox, int oy, CommandInvoke invoke, final IInvokeHolder holder) {
		
		final QADTextField scriptName = QADFACTORY.createTextField(invoke.getCommand(), ox+1, oy+2, 100-2);
		scriptName.setTooltip("The command to execute.");
		components.add(scriptName);
		
		QADButton buttonApply = QADFACTORY.createButton("Apply", ox+100+2, oy, 40, null);
		buttonApply.setAction(new Runnable() {
			@Override public void run() {
				NBTTagCompound invokeData = new NBTTagCompound();
				String text = scriptName.getText();
				invokeData.setString("type", "CommandInvoke");
				invokeData.setString("command", text);
				
				holder.sendInvokeUpdate(invokeData);
			}
		});
		buttonApply.setTooltip("Saves the settings.", "There is no auto-save so make","sure to press this button.");
		components.add(buttonApply);
		
//		QADButton buttonExecute = QADFACTORY.createButton("E", ox+100+4+40+2, oy, 20, null);
//		buttonExecute.setAction(new Runnable() {
//			@Override public void run() {
//				holder.sendCommand("execute", null);
//			}
//		});
//		buttonExecute.setTooltip("Prompts the server to","execute the command.");
//		components.add(buttonExecute);
		
	}
	
	private static void build_filescript(ArrayList<QADComponent> components,
			int ox, int oy, FileScriptInvoke invoke, final IInvokeHolder holder) {
		
		final QADTextField scriptName = QADFACTORY.createTextField(invoke.getScriptName(), ox+1, oy+2, 100-2);
		scriptName.setTooltip("The file-name of the script to execute.");
		components.add(scriptName);
		
		QADButton buttonApply = QADFACTORY.createButton("Apply", ox+100+2, oy, 40, null);
		buttonApply.setAction(new Runnable() {
			@Override public void run() {
				NBTTagCompound commandData = new NBTTagCompound();
				NBTTagCompound invokeData = new NBTTagCompound();
				commandData.setTag("scriptInvoke", invokeData);
				
				String text = scriptName.getText();
				invokeData.setString("type", "FileScriptInvoke");
				invokeData.setString("scriptFileName", text);
				
				holder.sendInvokeUpdate(invokeData);
			}
		});
		buttonApply.setTooltip("Saves the settings.", "There is no auto-save, so make","sure to press this button.");
		components.add(buttonApply);
		
		
		QADButton buttonReload = QADFACTORY.createButton("R", ox+100+4+40+2, oy, 20, null);
		buttonReload.setAction(new Runnable() {
			@Override public void run() {
				holder.sendCommand("reload", null);
			}
		});
		buttonReload.setTooltip("Prompts the server to","reload the script.");
		components.add(buttonReload);
		
		
		QADButton buttonExecute = QADFACTORY.createButton("E", ox+100+4+40+2+20+2, oy, 20, null);
		buttonExecute.setAction(new Runnable() {
			@Override public void run() {
				holder.sendCommand("execute", null);
			}
		});
		buttonExecute.setTooltip("Prompts the server to","execute the script.");
		components.add(buttonExecute);
		
		
		QADButton buttonReloadExecute = QADFACTORY.createButton("R+E", ox+100+4+40+2+20+2+20+2, oy, 24, null);
		buttonReloadExecute.setAction(new Runnable() {
			@Override public void run() {
				holder.sendCommand("reloadexecute", null);
			}
		});
		buttonReloadExecute.setTooltip("Prompts the server to reload","and then execute the script.");
		components.add(buttonReloadExecute);
		
	}
	
	

	private static void build_blocktrigger(ArrayList<QADComponent> components, int ox, int oy, final IInvokeHolder holder) {
		components.add(QADFACTORY.createButton("Set Region", ox, oy, 100, new Runnable() {
			@Override public void run() {
				EntityPlayer player = Minecraft.getMinecraft().thePlayer;
				int[] bounds = WandItem.getBoundsFromPLAYERorNULL(player);
				
				if(bounds == null){
					Minecraft.getMinecraft().ingameGUI.getChatGUI().addToSentMessages(EnumChatFormatting.RED+"Error: "+EnumChatFormatting.RESET+"Wand selection is invalid.");
					return;
				}
				
				NBTTagCompound invokeData = new NBTTagCompound();
				invokeData.setString("type", "BlockTriggerInvoke");
				invokeData.setIntArray("bounds", bounds);
				
				holder.sendInvokeUpdate(invokeData);
			}
		}).setTooltip(
				"Sets the region that is triggered",
				"when this invoke is run."
		));
		
		components.add(QADFACTORY.createButton(playIconLocation, ox+100+2, oy, 20, new Runnable() {
			@Override public void run() {
				holder.sendCommand("trigger", null);
			}
		}).setTooltip("Trigger this invoke."));
	}
	
	public static class InvokeSwitchAction implements Runnable {
		int invokeTypeFlags;
		IInvokeHolder holder;
		GuiScreen screen;
		
		public InvokeSwitchAction(int invokeTypeFlags, IInvokeHolder holder, GuiScreen screen) {
			this.invokeTypeFlags = invokeTypeFlags;
			this.holder = holder;
			this.screen = screen;
		}
		
		@Override
		public void run() {
			Minecraft.getMinecraft().displayGuiScreen(new InvokeSwitchGui(invokeTypeFlags, holder, screen));
		}
	}

}
