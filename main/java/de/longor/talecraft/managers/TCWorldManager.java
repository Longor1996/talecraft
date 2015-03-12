package de.longor.talecraft.managers;

import de.longor.talecraft.TaleCraft;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;

public class TCWorldManager {
	private final TaleCraft taleCraft;
	private final World world;
	
	public TCWorldManager(TaleCraft tc, World w) {
		taleCraft = tc;
		world = w;
	}
	
	public void init() {
		taleCraft.logger.info("Initializing new TCWorldManager -> " + this + " @" + world.hashCode());
	}
	
	public void dispose() {
		taleCraft.logger.info("Disposing of TCWorldManager -> " + this + " @" + world.getWorldInfo());
	}
	
	public void tickWorld(WorldTickEvent event) {
		if(!(event.world instanceof WorldServer))
			return;
		
		// System.out.println("TICKING WORLD -> @" + event.world);
		// TaleCraft.proxy.tick(event);
	}
	
}
