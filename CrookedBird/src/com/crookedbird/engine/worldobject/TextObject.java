package com.crookedbird.engine.worldobject;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;

public class TextObject {
	private String text;
	
	public TextObject(String text) {
		this.text = text;
	}
	
	public void draw(Graphics g, int x, int y) {
		FontMetrics m = g.getFontMetrics();
		
		g.setColor(Color.GREEN);
		
		int h = m.getHeight();
		int w = m.stringWidth(text);
		
		g.drawString(text, x, y + h);
	}

}
