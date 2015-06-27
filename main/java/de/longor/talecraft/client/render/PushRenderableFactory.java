package de.longor.talecraft.client.render;

import de.longor.talecraft.TaleCraft;
import de.longor.talecraft.client.render.temporables.BlockPosTemporable;
import de.longor.talecraft.client.render.temporables.LineToBoxTemporable;
import net.minecraft.nbt.NBTTagCompound;

public class PushRenderableFactory {
	
	public static final ITemporaryRenderable parsePushRenderableFromNBT(NBTTagCompound data) {
		String type = data.getString("type");
		
		if(type == null || type.isEmpty()) {
			return null;
		}
		
		// TaleCraft.logger.info("New Push-Renderable: " + type + " :" + data.toString());
		
		if(type.equals("pos-marker")) {
			int[] pos = data.getIntArray("pos");
			int color = data.getInteger("color");
			long deletionTimepoint = System.currentTimeMillis() + 250L;
			
			if(pos.length != 3)
				return null;
			
			BlockPosTemporable ren = new BlockPosTemporable();
			ren.pos = pos;
			ren.color = color;
			ren.deletionTimepoint = deletionTimepoint;
			return ren;
		}
		
		if(type.equals("line-to-box")) {
			int[] src = data.getIntArray("src");
			int[] box = data.getIntArray("box");
			long deletionTimepoint = System.currentTimeMillis() + 100L;
			
			if(src.length != 3)
				return null;
			if(box.length != 6)
				return null;
			
			LineToBoxTemporable ren = new LineToBoxTemporable();
			ren.box = box;
			ren.src = src;
			ren.deletionTimepoint = deletionTimepoint;
			
			// TaleCraft.logger.info("Parsed: " + ren);
			return ren;
		}
		
		return null;
	}
	
}
