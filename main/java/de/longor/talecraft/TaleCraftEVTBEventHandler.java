package de.longor.talecraft;

import de.longor.talecraft.managers.TCWorldsManager;
import de.longor.talecraft.managers.TCWorldManager;
import de.longor.talecraft.proxy.ServerHandler;
import net.minecraft.server.ServerEula;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerUseItemEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;

public class TaleCraftEVTBEventHandler
{
	private TaleCraft taleCraft;
	
	public TaleCraftEVTBEventHandler(TaleCraft taleCraft)
	{
		this.taleCraft = taleCraft;
	}
	
	@SubscribeEvent
	public void worldLoad(WorldEvent.Load event)
	{
		taleCraft.coremanager.registerWorld(event.world);
	}
	
	@SubscribeEvent
	public void worldLoad(WorldEvent.Unload event)
	{
		taleCraft.coremanager.unregisterWorld(event.world);
	}
	
	@SubscribeEvent
	public void guiRenderPre(GuiScreenEvent.DrawScreenEvent.Pre event) {
		// event.gui.drawString(event.gui.mc.fontRendererObj, "Hello, World!", 1, 1, 0xFFFFFF);
	}
	
	@SubscribeEvent
	public void modelBake(ModelBakeEvent event) {
		taleCraft.proxy.asClient().modelBake(event);
	}
	
	@SubscribeEvent
	public void entityJoinWorld(EntityJoinWorldEvent event) {
		if(!event.world.isRemote)
			ServerHandler.handleEntityJoin(event.world, event.entity);
	}
	
	/*
	@SubscribeEvent
	public void playerJoin(PlayerUseItemEvent event)
	{
		System.out.println("[TALECRAFT INFO] Player used item! " + event);
	}
	//*/
}
