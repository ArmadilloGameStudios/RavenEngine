package com.crookedbird.engine.graphics;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public abstract class ImageReference {
	public abstract BufferedImage getImage();

	public static BufferedImage safeImage(BufferedImage image) {
		// obtain the current system graphical settings
		GraphicsConfiguration gfx_config = GraphicsEnvironment
				.getLocalGraphicsEnvironment().getDefaultScreenDevice()
				.getDefaultConfiguration();

		/*
		 * if image is already compatible and optimized for current system
		 * settings, simply return it
		 */
		if (image.getColorModel().equals(gfx_config.getColorModel()))
			return image;

		// image is not optimized, so create a new image that is
		BufferedImage new_image = gfx_config.createCompatibleImage(
				image.getWidth(), image.getHeight(), image.getTransparency());

		// get the graphics context of the new image to draw the old image on
		Graphics2D g2d = (Graphics2D) new_image.getGraphics();

		// actually draw the image and dispose of context no longer needed
		g2d.drawImage(image, 0, 0, null);
		g2d.dispose();

		// return the new optimized image
		return new_image;
	}
	
	private static void printImage(BufferedImage img) {
		for (int i = 0; i < img.getWidth(); i++) {
			for (int j = 0; j < img.getHeight(); j++) {
				System.out.print(img.getRGB(i, j) + " ");
			}
			System.out.println();
		}
	}
	
	public static BufferedImage loadImage(File f) {
		BufferedImage image;
		try {
			image = ImageIO.read(f);
		} catch (IOException e) {
			image = genErrorImage();
			System.out.print("Error loading " + f.getPath());
		}

		// printImage(image);

		return safeImage(image);
	}

	public static BufferedImage loadImage(String src) {
		return loadImage(new File(src));
	}

	public static BufferedImage genErrorImage() {
		BufferedImage img = new BufferedImage(16, 16,
				BufferedImage.TYPE_INT_RGB);

		for (int i = 0; i < img.getWidth(); i++) {
				img.setRGB(i, i, Color.RED.getRGB());
				img.setRGB(i, img.getHeight() - i - 1, Color.RED.getRGB());
			
		}
		
		// printImage(img);

		return safeImage(img);
	}
}
