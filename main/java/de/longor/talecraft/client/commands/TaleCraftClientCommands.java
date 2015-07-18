package de.longor.talecraft.client.commands;

import de.longor.talecraft.TaleCraft;
import de.longor.talecraft.proxy.ClientProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.client.ClientCommandHandler;

public class TaleCraftClientCommands {
	public static Minecraft mc = Minecraft.getMinecraft();
	public static ClientProxy client = TaleCraft.proxy.asClient();
	public static ClientCommandHandler instance = ClientCommandHandler.instance;
	
	public static void init() {
		
		instance.registerCommand(new CommandBase() {
			@Override public String getName() {
				return "tcc_resreload";
			}
			@Override public String getCommandUsage(ICommandSender sender) {
				return "";
			}
			@Override public boolean canCommandSenderUse(ICommandSender sender) {
		    	return true;
		    }
			@Override
			public void execute(ICommandSender sender, String[] args) throws CommandException {
				shedule(new Runnable() {
					@Override public void run() {
						Minecraft.getMinecraft().refreshResources();
					}
				});
			}
		});
		
		instance.registerCommand(new CommandBase() {
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
				shedule(new Runnable() {
					@Override public void run() {
						mc.getRenderManager().setDebugBoundingBox(!mc.getRenderManager().isDebugBoundingBox());
					}
				});
			}
		});
		
		instance.registerCommand(new CommandBase() {
			@Override public String getName() {
				return "tcc_clearchat";
			}
			@Override public String getCommandUsage(ICommandSender sender) {
				return "";
			}
			@Override public boolean canCommandSenderUse(ICommandSender sender) {
		    	return true;
		    }
			@Override
			public void execute(ICommandSender sender, String[] args) throws CommandException {
				shedule(new Runnable() {
					@Override public void run() {
						mc.ingameGUI.getChatGUI().clearChatMessages();
					}
				});
			}
		});
		
		instance.registerCommand(new CommandBase() {
			@Override public String getName() {
				return "tcc_vismode";
			}
			@Override public String getCommandUsage(ICommandSender sender) {
				return "";
			}
			@Override public boolean canCommandSenderUse(ICommandSender sender) {
		    	return true;
		    }
			@Override
			public void execute(ICommandSender sender, String[] args) throws CommandException {
				final int mode = args.length > 0 ? super.parseInt(args[0]) : 0;
				
				shedule(new Runnable() {
					@Override public void run() {
						client.setVisualizationMode(mode);
					}
				});
			}
		});
		
		instance.registerCommand(new CommandBase() {
			@Override public String getName() {
				return "tcc_tpo";
			}
			@Override public String getCommandUsage(ICommandSender sender) {
				return "";
			}
			@Override public boolean canCommandSenderUse(ICommandSender sender) {
		    	return true;
		    }
			@Override
			public void execute(ICommandSender sender, String[] args) throws CommandException {
				shedule(new Runnable() {
					@Override public void run() {
						if(mc.thePlayer != null) {
							mc.thePlayer.sendChatMessage("/tp 0 255 0");
						}
					}
				});
			}
		});
		
		instance.registerCommand(new CommandBase() {
			@Override public String getName() {
				return "tcc_infobar";
			}
			@Override public String getCommandUsage(ICommandSender sender) {
				return "<true/false>";
			}
			@Override public boolean canCommandSenderUse(ICommandSender sender) {
		    	return true;
		    }
			@Override
			public void execute(ICommandSender sender, String[] args) throws CommandException {
				if(args.length != 1) {
					return;
				}
				
				boolean flag = this.parseBoolean(args[0]);
				ClientProxy.settings.setBoolean("client.infobar.enabled", flag);
			}
		});
		
		instance.registerCommand(new CommandBase() {
			@Override public String getName() {
				return "tcc_pastereach";
			}
			@Override public String getCommandUsage(ICommandSender sender) {
				return "<0..64>";
			}
			@Override public boolean canCommandSenderUse(ICommandSender sender) {
		    	return true;
		    }
			@Override
			public void execute(ICommandSender sender, String[] args) throws CommandException {
				if(args.length != 1) {
					return;
				}
				
				int reach = this.parseInt(args[0], 1, 64);
				ClientProxy.settings.setInteger("item.paste.reach", reach);
				ClientProxy.settings.send();
			}
		});
		
		instance.registerCommand(new CommandBase() {
			@Override public String getName() {
				return "tcc_r_invokeviz";
			}
			@Override public String getCommandUsage(ICommandSender sender) {
				return "<true/false>";
			}
			@Override public boolean canCommandSenderUse(ICommandSender sender) {
		    	return true;
		    }
			@Override
			public void execute(ICommandSender sender, String[] args) throws CommandException {
				if(args.length != 1) {
					sender.addChatMessage(new ChatComponentText("Enables/Disables the invoke visualization."));
					return;
				}
				
				boolean flag = this.parseBoolean(args[0]);
				ClientProxy.settings.setBoolean("client.render.invokeVisualize", flag);
			}
		});
		
	}
	
	public static void shedule(Runnable runnable) {
		client.sheduleClientTickTask(runnable);
	}
	
}
