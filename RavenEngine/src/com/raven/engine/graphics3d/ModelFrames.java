package com.raven.engine.graphics3d;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModelFrames {
	private Map<String, List<Frame>> map;
	
	public ModelFrames() {
		map = new HashMap<String, List<Frame>>();
	}

	public void addFrame(String frameState, int frameIndex, int frameTime,
			ModelReference model) {
		List<Frame> frameAnim = map.get(frameState);
		if (frameAnim == null) {
			map.put(frameState, frameAnim = new ArrayList<Frame>());
		}

		frameAnim.add(frameIndex, new Frame(frameState, frameIndex, frameTime,
				model));
	}
	
	public int getFrameTime(String animationstate) {
		int length = 0;

		for (Frame f : map.get(animationstate)) {
			length += f.getTimeLength();
		}

		return length;
	}
		
	public ModelReference getModel() {
		List<Frame> frames = map.get("idle");

		if (frames != null) {
			Frame f = frames.get(0);

			if (f != null) {
				return f.getModel();
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	public ModelReference getModel(String state, long l) {
		List<Frame> frames = map.get(state);

		if (frames == null) {
			frames = map.get("idle");
		}

		Frame current = frames.get(0);

		int timeSum = 0;
		for (Frame f : frames) {
			timeSum += f.getTimeLength();
		}

		if (timeSum == 0) {
			return current.getModel();
		}

		l = l % timeSum;
		int currentTime = 0;
		int i = 0;

		while (currentTime < l) {
			current = frames.get(i);
			currentTime += current.getTimeLength();
			i++;
		}

		return current.getModel();
	}
}
