package de.longor.talecraft.client.commands;

import de.longor.talecraft.proxy.ClientProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;

public final class ToggleEntityBoundsCommand extends CommandBase {
	@Override public String getName() {
		return "tcc_bounds";
	}

	@Override public String getCommandUsage(ICommandSender sender) {
		return "";
	}

	@Override public boolean canCommandSenderUse(ICommandSender sender) {
		return true;
	}
	
	@Override
	public void execute(ICommandSender sender, String[] args) throws CommandException {
		ClientProxy.shedule(new Runnable() {
			@Override public void run() {
				RenderManager mng = Minecraft.getMinecraft().getRenderManager();
				mng.setDebugBoundingBox(!mng.isDebugBoundingBox());
			}
		});
	}
	
}