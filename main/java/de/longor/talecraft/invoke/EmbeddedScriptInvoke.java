package de.longor.talecraft.invoke;

import net.minecraft.nbt.NBTTagCompound;

public class EmbeddedScriptInvoke implements IInvoke, IScriptInvoke {
	public String script;
	
	public EmbeddedScriptInvoke() {
		this.script = "out.println(\"Hello, World!\");";
	}
	
	public EmbeddedScriptInvoke(String script) {
		this.script = script;
	}
	
	@Override
	public String getType() {
		return "EmbeddedScriptInvoke";
	}
	
	@Override
	public void reloadScript() {
		// Don't do anything. The script is always in memory.
	}
	
	@Override
	public void writeToNBT(NBTTagCompound compound) {
		compound.setString("embeddedScript", script);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		script = compound.getString("embeddedScript");
	}

	@Override
	public String getScript() {
		return script;
	}

	@Override
	public String getScriptName() {
		return "<EMBEDDED>";
	}
	
}
