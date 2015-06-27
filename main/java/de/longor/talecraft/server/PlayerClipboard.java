package de.longor.talecraft.server;

import net.minecraft.block.state.IBlockState;

public class PlayerClipboard {
	private IBlockState[] data;
	private int width;
	private int height;
	private int length;
	
	public PlayerClipboard(IBlockState[] region, int w, int h, int l) {
		this.data = region;
		this.width = w;
		this.height = h;
		this.length = l;
	}
	
	public IBlockState get(int x,int y,int z) {
		return data[index(x, y, z)];
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public int getLength() {
		return length;
	}
	
	private int index(int x,int y,int z) {
		return x + (z*width) + (y*width*length);
	}
	
}
