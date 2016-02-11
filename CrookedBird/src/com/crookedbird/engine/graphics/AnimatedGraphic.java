package com.crookedbird.engine.graphics;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import com.crookedbird.engine.GameEngine;
import com.crookedbird.engine.database.GameDataRow;
import com.crookedbird.engine.scene.Layer;

public class AnimatedGraphic extends ImageReference {

	private Map<String, List<Frame>> states = new HashMap<String, List<Frame>>();
	private int width, height;
	
	public AnimatedGraphic(File file) {
		BufferedReader reader = null;

		try {
			reader = new BufferedReader(new InputStreamReader(
					new FileInputStream(file)));
		} catch (FileNotFoundException e) {
		}

		String line = "";
		width = 0; height = 0;
		int i = 0;
		BufferedImage img = null;
		List<Frame> frameList = null;

		try {
			while ((line = reader.readLine()) != null) {
				String[] values = line.split("=");

				if (values.length >= 2) {
					String prop = values[0].trim();
					String value = values[1].trim();

					if (prop.compareTo("width") == 0) {
						width = Integer.parseInt(value);
					} else if (prop.compareTo("height") == 0) {
						height = Integer.parseInt(value);
					} else if (prop.compareTo("img") == 0) {
						img = GameEngine.getEngine()
								.getImageReferenceAsset(value).getImage();
					} else {
						String[] times = value.split(",");
						frameList = new ArrayList<Frame>();

						for (String time : times) {
							BufferedImage bufferedImage = new BufferedImage(
									width, height, BufferedImage.TYPE_INT_ARGB);

							int w = ((width * i) % img.getWidth()) / width, h = (width * i)
									/ img.getWidth();

							bufferedImage.getGraphics().drawImage(img, 0, 0,
									width, height, width * w, height * h,
									width * (w + 1), height * (h + 1), null);

							frameList.add(new Frame(prop, frameList.size(),
									Integer.parseInt(time), bufferedImage));

							i++;
						}

						states.put(prop, frameList);

					}
				}
			}
		} catch (IOException e) {
		}
	}

	public AnimatedGraphic(List<Frame> frames) {
		addFrames(frames);
	}

	public AnimatedGraphic(BufferedImage genErrorImage) {
		// TODO Auto-generated constructor stub
	}

	public AnimatedGraphic(GameDataRow anim) {

		String line = "";
		width = Integer.parseInt(anim.get("Width").toString());
		height = Integer.parseInt(anim.get("Height").toString());
		BufferedImage img = GameEngine.getEngine()
				.getImageReferenceAsset(anim.get("Src").toString()).getImage();
		List<Frame> frameList = null;

		String value = anim.get("FrameCSV").toString();

		String[] strFrames = value.split("\\|");
		for (String strFrame : strFrames) {

			String[] strFrameData = strFrame.split(",");
			frameList = new ArrayList<Frame>();

			for (int i = 1; i < strFrameData.length; i += 2) {
				BufferedImage bufferedImage = new BufferedImage(width, height,
						BufferedImage.TYPE_INT_ARGB);

				int w = ((width * Integer.parseInt(strFrameData[i])) % img
						.getWidth()) / width;
				int h = (width * Integer.parseInt(strFrameData[i]))
						/ img.getWidth();

				bufferedImage.getGraphics().drawImage(img, 0, 0, width, height,
						width * w, height * h, width * (w + 1),
						height * (h + 1), null);

				frameList.add(new Frame(strFrameData[0], frameList.size(),
						Integer.parseInt(strFrameData[i + 1]), bufferedImage));
			}

			states.put(strFrameData[0], frameList);
		}
	}

	public void addFrames(List<Frame> frames) {
		for (Frame frame : frames) {
			addFrame(frame);
		}
	}

	public void addFrame(Frame frame) {
		String state = frame.getState();
		List<Frame> list;

		if (states.containsKey(state)) {
			list = states.get(state);
			list.add(frame);
		} else {
			list = new ArrayList<Frame>();
			states.put(state, list);
		}

		Collections.sort(list);
	}

	@Override
	public BufferedImage getImage() {
		List<Frame> frames = states.get("idle");

		if (frames != null) {
			Frame f = frames.get(0);

			if (f != null) {
				return f.getImage();
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	public BufferedImage getImage(String state, int time) {
		List<Frame> frames = states.get(state);

		if (frames == null) {
			frames = states.get("idle");
		}

		Frame current = frames.get(0);

		int timeSum = 0;
		for (Frame f : frames) {
			timeSum += f.getTimeLength();
		}

		if (timeSum == 0) {
			return current.getImage();
		}

		time = time % timeSum;
		int currentTime = 0;
		int i = 0;

		while (currentTime < time) {
			current = frames.get(i);
			currentTime += current.getTimeLength();
			i++;
		}

		return current.getImage();
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}

	public void draw(Layer v) {
		// TODO
	}
}