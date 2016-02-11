package com.crookedbird.engine.graphics;

import java.awt.image.BufferedImage;

public class Frame extends ImageReference implements Comparable<Frame> {
	private String state;
	private int index; // index in this state
	private int time; // amount of time(ms) on this frame, 0 is forever
	private String src;
	private BufferedImage img;

	public Frame(String s, int i, int t, BufferedImage img) {
		state = s;
		index = i;
		time = t;
		
		this.img = img;
	}

	public String getState() {
		return state;
	}

	public int getIndex() {
		return index;
	}

	public int getTimeLength() {
		return time;
	}

	@Override
	public BufferedImage getImage() {
		return img;
	}

	@Override
	public int compareTo(Frame frame) {
		int comp = this.state.compareTo(frame.state);
		if (comp != 0)
			return comp;
		if (this.index > frame.index)
			return 1;
		if (this.index < frame.index)
			return -1;
		return 0;
	}
}
