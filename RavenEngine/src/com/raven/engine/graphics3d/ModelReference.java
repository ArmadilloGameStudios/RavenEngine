package com.raven.engine.graphics3d;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_SHORT;
import static org.lwjgl.opengl.GL11.glColorPointer;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL11.glNormalPointer;
import static org.lwjgl.opengl.GL11.glVertexPointer;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;

import java.io.File;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;

import org.lwjgl.BufferUtils;

public class ModelReference {

	private static ModelReference blank;

	private final static int vertex_size = 3;

	private static ArrayList<Float> vertex_list = new ArrayList<Float>();
	private static ArrayList<Float> normal_list = new ArrayList<Float>();
	private static ArrayList<Float> colors_list = new ArrayList<Float>();
	private static ArrayList<Float> glow_list = new ArrayList<Float>();

	private static int vbo_vertex_handle, vbo_normal_handle, vbo_color_handle,
			vbo_glow_handle;

	private static int left = -8, right = 8, lower = -8, upper = 8, back = -8,
			front = 8;
	private static Float[][][][] mapColor;
	private static Float[][][] mapGlow;

	public static ModelFrames load(File f) {
		ModelFrames frameAnimation = new ModelFrames();
		String frameState = null;
		int frameIndex = 0, frameTime = 0;

		left = -8;
		right = 8;
		lower = -8;
		upper = 8;
		back = -8;
		front = 8;

		mapColor = new Float[right - left][upper - lower][front - back][];
		mapGlow = new Float[right - left][upper - lower][front - back];

		try {
			int x = 0;
			int y = 0;
			int z = 0;

			float r = 0f;
			float g = 0f;
			float b = 0f;

			float glow = 0f;

			Float[] color = new Float[] { 0.0f, 0.0f, 0.0f };

			for (String data : Files.readAllLines(f.toPath())) {
				String[] sdata = data.split(":");

				if (sdata.length == 1) {
					String[] cdata = sdata[0].split(",");

					if (cdata.length == 3) {
						x = Integer.parseInt(cdata[0].trim());
						y = Integer.parseInt(cdata[1].trim());
						z = Integer.parseInt(cdata[2].trim());

						mapColor[x][y][z] = color;

						mapGlow[x][y][z] = glow;
					}
				} else if (sdata.length == 2) {
					String[] cdata;

					switch (sdata[0].trim()) {
					case "size":
						cdata = sdata[1].split(",");

						if (cdata.length == 3) {
							int width = Integer.parseInt(cdata[0].trim());
							int height = Integer.parseInt(cdata[1].trim());
							int length = Integer.parseInt(cdata[2].trim());

							left = -width / 2;
							right = width / 2;
							lower = -height / 2;
							upper = height / 2;
							back = -length / 2;
							front = length / 2;

							mapColor = new Float[right - left][upper - lower][front
									- back][];
							mapGlow = new Float[right - left][upper - lower][front
									- back];
						}
						break;
					case "frame":
						if (frameState != null) {
							frameAnimation.addFrame(frameState, frameIndex,
									frameTime, processFrameData());
						}

						String newFrame = sdata[1].trim();
						frameState = newFrame;

						cdata = sdata[1].split(",");

						if (cdata.length == 3) {
							frameState = cdata[0].trim();
							frameIndex = Integer.parseInt(cdata[1].trim());
							frameTime = Integer.parseInt(cdata[2].trim());

						}

						mapColor = new Float[right - left][upper - lower][front
								- back][];
						mapGlow = new Float[right - left][upper - lower][front
								- back];
						break;
					case "color":
						cdata = sdata[1].split(",");

						if (cdata.length == 3) {
							r = Float.parseFloat(cdata[0].trim());
							g = Float.parseFloat(cdata[1].trim());
							b = Float.parseFloat(cdata[2].trim());

							color = new Float[] { r, g, b };
						}
						break;
					case "glow":
						glow = Float.parseFloat(sdata[1].trim());
						break;
					}
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(0);
		}

		frameAnimation.addFrame(frameState, frameIndex, frameTime,
				processFrameData());

		mapColor = null;
		mapGlow = null;

		// process lists into buffers
		return frameAnimation;
	}

	private static ModelReference processFrameData() {

		int vertex_start = vertex_list.size();

		ModelReference model = new ModelReference();

		for (int i = 0; i < right - left; i++) {
			for (int j = 0; j < upper - lower; j++) {
				for (int k = 0; k < front - back; k++) {

					if (mapColor[i][j][k] != null) {

						anyFace(i, j, k, 0); // left
						anyFace(i, j, k, 1); // right
						anyFace(i, j, k, 2); // lower
						anyFace(i, j, k, 3); // upper
						anyFace(i, j, k, 4); // back
						anyFace(i, j, k, 5); // front
					}

				}
			}
		}

		int vertex_count = vertex_list.size();
		model.vertices = vertex_list.size() - vertex_start;
		// model.quads = vertex_list.size() / 12;

		ShortBuffer index_list_buffer = BufferUtils
				.createShortBuffer(model.vertices / vertex_size);
		for (short i = (short) (vertex_start / vertex_size); i < vertex_count
				/ vertex_size; i++) {
			index_list_buffer.put(i);
		}
		index_list_buffer.flip();

		model.vbo_index_handle = glGenBuffers();
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, model.vbo_index_handle);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, index_list_buffer, GL_STATIC_DRAW);

		glBindBuffer(GL_ARRAY_BUFFER, 0);

		return model;
	}

	private static void anyFace(int i, int j, int k, int type) {
		int[] g = null;
		int[] v = null;
		Float[] n = null;

		switch (type) {
		case 0: // left
			if (!(i == 0 || mapColor[i - 1][j][k] == null))
				return;

			v = new int[] { 0, 1, 1, 0, 1, 0, 0, 0, 0, 0, 0, 1 };

			n = new Float[] { -1f, 0f, 0f };
			break;
		case 1: // right
			if (!(i == right - left - 1 || mapColor[i + 1][j][k] == null))
				return;

			v = new int[] { 1, 0, 0, 1, 1, 0, 1, 1, 1, 1, 0, 1 };

			n = new Float[] { 1f, 0f, 0f };
			break;
		case 2: // lower
			if (!(j == 0 || mapColor[i][j - 1][k] == null))
				return;

			v = new int[] { 0, 0, 0, 1, 0, 0, 1, 0, 1, 0, 0, 1 };

			n = new Float[] { 0f, -1f, 0f };
			break;
		case 3: // upper
			if (!(j == upper - lower - 1 || mapColor[i][j + 1][k] == null))
				return;

			v = new int[] { 1, 1, 1, 1, 1, 0, 0, 1, 0, 0, 1, 1 };

			n = new Float[] { 0f, 1f, 0f };
			break;
		case 4: // back
			if (!(k == 0 || mapColor[i][j][k - 1] == null))
				return;

			v = new int[] { 1, 1, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0 };

			n = new Float[] { 0f, 0f, -1f };
			break;
		case 5: // front
			if (!(k == front - back - 1 || mapColor[i][j][k + 1] == null))
				return;

			v = new int[] { 0, 0, 1, 1, 0, 1, 1, 1, 1, 0, 1, 1 };

			n = new Float[] { 0f, 0f, 1f };
			break;
		}

		g = new int[12];
		for (int x = 0; x < 12; x++) {
			if (v[x] == 0)
				g[x] = -1;
			else
				g[x] = 1;
		}

		vertex_list.addAll(Arrays.asList(new Float[] {
				(float) (i + left + v[0]), (float) (j + lower + v[1]),
				(float) (k + back + v[2]),

				(float) (i + left + v[3]), (float) (j + lower + v[4]),
				(float) (k + back + v[5]),

				(float) (i + left + v[6]), (float) (j + lower + v[7]),
				(float) (k + back + v[8]),

				(float) (i + left + v[9]), (float) (j + lower + v[10]),
				(float) (k + back + v[11]), }));

		for (int l = 0; l < 4; l++) {
			colors_list.addAll(Arrays.asList(mapColor[i][j][k]));
			normal_list.addAll(Arrays.asList(n));
		}

		// glow corners
		Float[] glowCorners = new Float[12];
		for (int h = 0; h < 12; h += 3) {
			glowCorners[h] = mapColor[i][j][k][0] * mapGlow[i][j][k];
			glowCorners[h + 1] = mapColor[i][j][k][1] * mapGlow[i][j][k];
			glowCorners[h + 2] = mapColor[i][j][k][2] * mapGlow[i][j][k];
		}

		setVerticesGlow(glowCorners, 0, mapGlow[i][j][k], i, j, k, g[0], g[1],
				g[2]);
		setVerticesGlow(glowCorners, 3, mapGlow[i][j][k], i, j, k, g[3], g[4],
				g[5]);
		setVerticesGlow(glowCorners, 6, mapGlow[i][j][k], i, j, k, g[6], g[7],
				g[8]);
		setVerticesGlow(glowCorners, 9, mapGlow[i][j][k], i, j, k, g[9], g[10],
				g[11]);

		glow_list.addAll(Arrays.asList(glowCorners));
	}

	private static void setVerticesGlow(Float[] glowCorners, int s, float g,
			int i, int j, int k, int a, int b, int c) {

		if (i + a >= 0 && i + a < right - left &&
				mapColor[i + a][j][k] != null) {
			glowCorners[s] += getGlowFade(0, i + a, j, k) * (1f - g);
			glowCorners[s + 1] += getGlowFade(1, i + a, j, k) * (1f - g);
			glowCorners[s + 2] += getGlowFade(2, i + a, j, k) * (1f - g);
		}

		if (k + c >= 0 && k + c < front - back &&
				mapColor[i][j][k + c] != null) {
			glowCorners[s] += getGlowFade(0, i, j, k + c) * (1f - g);
			glowCorners[s + 1] += getGlowFade(1, i, j, k + c) * (1f - g);
			glowCorners[s + 2] += getGlowFade(2, i, j, k + c) * (1f - g);
		}

		if (j + b >= 0 && j + b < upper - lower &&
				mapColor[i][j + b][k] != null) {
			glowCorners[s] += getGlowFade(0, i, j + b, k) * (1f - g);
			glowCorners[s + 1] += getGlowFade(1, i, j + b, k) * (1f - g);
			glowCorners[s + 2] += getGlowFade(2, i, j + b, k) * (1f - g);
		}

		if (j + b >= 0 && j + b < upper - lower &&
				k + c >= 0 && k + c < front - back && 
				mapColor[i][j + b][k + c] != null) {
			glowCorners[s] += getGlowFade(0, i, j + b, k + c) * (1f - g);
			glowCorners[s + 1] += getGlowFade(1, i, j + b, k + c) * (1f - g);
			glowCorners[s + 2] += getGlowFade(2, i, j + b, k + c) * (1f - g);
		}

		if (i + a >= 0 && i + a < right - left && 
				k + c >= 0 && k + c < front - back && 
				mapColor[i + a][j][k + c] != null) {
			glowCorners[s] += getGlowFade(0, i + a, j, k + c) * (1f - g);
			glowCorners[s + 1] += getGlowFade(1, i + a, j, k + c) * (1f - g);
			glowCorners[s + 2] += getGlowFade(2, i + a, j, k + c) * (1f - g);
		}

		if (i + a >= 0 && i + a < right - left && 
				j + b >= 0 && j + b < upper - lower && 
				mapColor[i + a][j + b][k] != null) {
			glowCorners[s] += getGlowFade(0, i + a, j + b, k) * (1f - g);
			glowCorners[s + 1] += getGlowFade(1, i + a, j + b, k) * (1f - g);
			glowCorners[s + 2] += getGlowFade(2, i + a, j + b, k) * (1f - g);
		}

		if (i + a >= 0 && i + a < right - left &&
				j + b >= 0 && j + b < upper - lower && 
				k + c >= 0 && k + c < front - back && 
				mapColor[i + a][j + b][k + c] != null) {
			glowCorners[s] += getGlowFade(0, i + a, j + b, k + c) * (1f - g);
			glowCorners[s + 1] += getGlowFade(1, i + a, j + b, k + c)
					* (1f - g);
			glowCorners[s + 2] += getGlowFade(2, i + a, j + b, k + c)
					* (1f - g);
		}
	}

	private static float getGlowFade(int c, int i, int j, int k) {
		return mapColor[i][j][k][c] * mapGlow[i][j][k] / 3.0f;
	}

	public static void compileBuffer() {
		int vertices = vertex_list.size();

		// put into buffers
		FloatBuffer vertex_list_buffer = BufferUtils
				.createFloatBuffer(vertices);
		for (Float vertex : vertex_list) {
			vertex_list_buffer.put(vertex);
		}
		vertex_list_buffer.flip();

		FloatBuffer normal_list_buffer = BufferUtils
				.createFloatBuffer(vertices);
		for (Float normal : normal_list) {
			normal_list_buffer.put(normal);
		}
		normal_list_buffer.flip();

		FloatBuffer colors_list_buffer = BufferUtils
				.createFloatBuffer(vertices);
		for (Float colors : colors_list) {
			colors_list_buffer.put(colors);
		}
		colors_list_buffer.flip();

		FloatBuffer glow_list_buffer = BufferUtils.createFloatBuffer(vertices);
		for (Float mode : glow_list) {
			glow_list_buffer.put(mode);
		}
		glow_list_buffer.flip();

		// create vbo
		vbo_vertex_handle = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vbo_vertex_handle);
		glBufferData(GL_ARRAY_BUFFER, vertex_list_buffer, GL_STATIC_DRAW);
		glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0l);

		vbo_normal_handle = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vbo_normal_handle);
		glBufferData(GL_ARRAY_BUFFER, normal_list_buffer, GL_STATIC_DRAW);
		glVertexAttribPointer(1, 3, GL_FLOAT, false, 0, 0l);

		vbo_color_handle = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vbo_color_handle);
		glBufferData(GL_ARRAY_BUFFER, colors_list_buffer, GL_STATIC_DRAW);
		glVertexAttribPointer(2, 3, GL_FLOAT, false, 0, 0l);

		vbo_glow_handle = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vbo_glow_handle);
		glBufferData(GL_ARRAY_BUFFER, glow_list_buffer, GL_STATIC_DRAW);
		glVertexAttribPointer(3, 3, GL_FLOAT, false, 0, 0l);

		glBindBuffer(GL_ARRAY_BUFFER, 0);
	}

	public static void setBlankModel() {
		int vertex_start = vertex_list.size();

		blank = new ModelReference();

		vertex_list.addAll(Arrays.asList(new Float[] {
				-1.0f, -1.0f, 0.0f,
				1.0f, -1.0f, 0.0f,
				1.0f, 1.0f, 0.0f,
				-1.0f, 1.0f, 0.0f,
		}));

		normal_list.addAll(Arrays.asList(new Float[] {
				0.0f, 0.0f, 0.0f,
				0.0f, 0.0f, 0.0f,
				0.0f, 0.0f, 0.0f,
				0.0f, 0.0f, 0.0f
		}));
		colors_list.addAll(Arrays.asList(new Float[] {
				0.0f, 0.0f, 0.0f,
				0.0f, 0.0f, 0.0f,
				0.0f, 0.0f, 0.0f,
				0.0f, 0.0f, 0.0f
		}));
		glow_list.addAll(Arrays.asList(new Float[] {
				0.0f, 0.0f, 0.0f,
				0.0f, 0.0f, 0.0f,
				0.0f, 0.0f, 0.0f,
				0.0f, 0.0f, 0.0f
		}));


		int vertex_count = vertex_list.size();
		blank.vertices = vertex_list.size() - vertex_start;

		ShortBuffer index_list_buffer = BufferUtils
				.createShortBuffer(blank.vertices / vertex_size);
		for (short i = (short) (vertex_start / vertex_size); i < vertex_count
				/ vertex_size; i++) {
			index_list_buffer.put(i);
		}
		index_list_buffer.flip();

		blank.vbo_index_handle = glGenBuffers();
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, blank.vbo_index_handle);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, index_list_buffer, GL_STATIC_DRAW);

		glBindBuffer(GL_ARRAY_BUFFER, 0);
	}

	public static ModelReference getBlankModel() {
		return blank;
	}

	private int vertices;
	private int vbo_index_handle;

	private ModelReference() {
	}

	public void draw() {
		// glPolygonMode(GL_BACK, GL_FILL);

		// glFrontFace(GL_CW);

		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vbo_index_handle);

		glDrawElements(GL_QUADS, vertices, GL_UNSIGNED_SHORT, 0);
	}
}
