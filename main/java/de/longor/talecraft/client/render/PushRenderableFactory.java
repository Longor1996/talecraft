package de.longor.talecraft.client.render;

import de.longor.talecraft.TaleCraft;
import de.longor.talecraft.client.render.temporables.LineToBoxTemporable;
import net.minecraft.nbt.NBTTagCompound;

public class PushRenderableFactory {
	
	public static final ITemporaryRenderable parsePushRenderableFromNBT(NBTTagCompound data) {
		String type = data.getString("type");
		
		if(type == null || type.isEmpty()) {
			return null;
		}
		
		TaleCraft.logger.info("New Push-Renderable: " + data.toString());
		TaleCraft.logger.info("Parsing: " + type);
		
		if(type.equals("line-to-box")) {
			int[] src = data.getIntArray("src");
			int[] box = data.getIntArray("box");
			long deletionTimepoint = System.currentTimeMillis() + 1000L;
			
			if(src.length != 3)
				return null;
			if(box.length != 6)
				return null;
			
			LineToBoxTemporable ren = new LineToBoxTemporable();
			ren.box = box;
			ren.src = src;
			ren.deletionTimepoint = deletionTimepoint;
			
			TaleCraft.logger.info("Parsed: " + ren);
			return ren;
		}
		
		return null;
	}
	
}
