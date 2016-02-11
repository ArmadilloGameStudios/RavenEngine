package com.crookedbird.engine.graphics;

import java.awt.image.BufferedImage;
import java.io.File;

public class ImageGraphic extends ImageReference {
	private BufferedImage img;
	
	public ImageGraphic (BufferedImage img) {
		this.img = img;
	}
	
	public ImageGraphic(String src) {
		this.img = ImageReference.loadImage(src);
	}
	
	public ImageGraphic(File f) {
		this.img = ImageReference.loadImage(f);
	}

	@Override
	public BufferedImage getImage() {
		return img;
	}

}
