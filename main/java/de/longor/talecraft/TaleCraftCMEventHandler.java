package de.longor.talecraft;

import com.google.common.eventbus.Subscribe;

import de.longor.talecraft.managers.TCWorldsManager;
import de.longor.talecraft.managers.TCWorldManager;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppedEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TaleCraftCMEventHandler
{
	private TaleCraft taleCraft;
	
	public TaleCraftCMEventHandler(TaleCraft taleCraft)
	{
		this.taleCraft = taleCraft;
	}
	
	@Subscribe
	public void serverStarting(FMLServerStartingEvent event)
	{
		System.out.println("Server starting: " + event + " [TCINFO]");
	}
	
	@Subscribe
	public void serverStopping(FMLServerStoppingEvent event)
	{
		System.out.println("Server stopping: " + event + " [TCINFO]");
	}
	
	@Subscribe
	public void serverStarted(FMLServerStartedEvent event)
	{
		System.out.println("Server started: " + event + " [TCINFO]");
	}
	
	@Subscribe
	public void serverStopped(FMLServerStoppedEvent event)
	{
		System.out.println("Server stopped: " + event + " [TCINFO]");
	}
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void keyEvent(KeyInputEvent event) {
		if(FMLCommonHandler.instance().getSide().isClient()) {
			if(taleCraft.proxy instanceof de.longor.talecraft.proxy.ClientProxy) {
				((de.longor.talecraft.proxy.ClientProxy)taleCraft.proxy).keyEvent(event);
			}
		}
	}
	
	@SubscribeEvent
	public void tick(TickEvent event) {
		taleCraft.proxy.tick(event);
	}
	
	@SubscribeEvent
	public void tickWorld(WorldTickEvent event) {
		taleCraft.proxy.tickWorld(event);
	}
	
}
