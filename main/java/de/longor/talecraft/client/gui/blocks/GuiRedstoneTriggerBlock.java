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
import de.longor.talecraft.client.gui.invoke.BlockInvokeHolder;
import de.longor.talecraft.client.gui.invoke.InvokePanelBuilder;
import de.longor.talecraft.client.gui.qad.QADButton;
import de.longor.talecraft.client.gui.qad.QADComponent;
import de.longor.talecraft.client.gui.qad.QADGuiScreen;
import de.longor.talecraft.client.gui.qad.QADLabel;
import de.longor.talecraft.items.WandItem;
import de.longor.talecraft.network.StringNBTCommand;

public class GuiRedstoneTriggerBlock extends QADGuiScreen {
	RedstoneTriggerBlockTileEntity tileEntity;
	
	public GuiRedstoneTriggerBlock(RedstoneTriggerBlockTileEntity tileEntity) {
		this.tileEntity = tileEntity;
	}
	
	public void buildGui(ArrayList<QADComponent> components) {
		final BlockPos position = tileEntity.getPos();
		
		components.add(new QADLabel("Redstone Trigger @ " + position.getX() + " " + position.getY() + " " + position.getZ(), 2, 2));
		InvokePanelBuilder.build(this, components, 2, 16, tileEntity.getInvoke(), new BlockInvokeHolder(position, "triggerInvoke"), InvokePanelBuilder.INVOKE_TYPE_EDIT_ALLOWALL);
	}
	
}
