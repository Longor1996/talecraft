package de.longor.talecraft.client.gui.qad.model;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils.Text;

import de.longor.talecraft.client.gui.qad.QADLabel.LabelModel;

public class DefaultLabelModel implements LabelModel {
	private final String text;
	
	public DefaultLabelModel(String text) {
		this.text = text;
	}
	
	@Override
	public String getText() {
		return text;
	}
	
	@Override
	public int getTextLength() {
		return text.length();
	}
}
