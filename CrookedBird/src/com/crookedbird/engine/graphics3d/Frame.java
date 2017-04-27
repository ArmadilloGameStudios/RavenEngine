package com.crookedbird.engine.graphics3d;

public class Frame implements Comparable<Frame> {
	private String state;
	private int index; // index in this state
	private int time; // amount of time(ms) on this frame, 0 is forever
	private String src;
	private ModelReference model;

	public Frame(String s, int i, int t, ModelReference model) {
		state = s;
		index = i;
		time = t;

		this.model = model;
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
	
	public ModelReference getModel() {
		return model;
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
