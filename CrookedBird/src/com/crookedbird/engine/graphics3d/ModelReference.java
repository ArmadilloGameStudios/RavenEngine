package com.crookedbird.engine.graphics3d;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.*;
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
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;

import org.lwjgl.BufferUtils;

public class ModelReference {

	private final static int vertex_size = 3;

	private static ArrayList<Float> vertex_list = new ArrayList<Float>();
	private static ArrayList<Float> normal_list = new ArrayList<Float>();
	private static ArrayList<Float> colors_list = new ArrayList<Float>();
	private static ArrayList<Float> glow_list = new ArrayList<Float>();

	private static int vbo_vertex_handle, vbo_normal_handle, vbo_color_handle, vbo_glow_handle;

	public static ModelReference load(File f) {
		ModelReference model = new ModelReference();

		int vertex_start = vertex_list.size();

		int left = -8, right = 8, lower = -8, upper = 8, back = -8, front = 8;

		Float[][][][] mapColor = new Float[right - left][upper - lower][front
				- back][];
		Float[][][] mapGlow = new Float[right - left][upper - lower][front
				- back];

		// Gen Test Data
		// Random rnd = new Random();
		// for (int i = 0; i < 40; i++) {
		// int a = rnd.nextInt(right - left);
		// int b = rnd.nextInt(upper - lower);
		// int c = rnd.nextInt(front - back);
		//
		// map[a][b][c] = new Float[] { rnd.nextFloat(), rnd.nextFloat(),
		// rnd.nextFloat() };
		// }

		// TODO find this values
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
					switch (sdata[0].trim()) {
					case "color":
						String[] cdata = sdata[1].split(",");

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

		// process lists into buffers

		for (int i = 0; i < right - left; i++) {
			for (int j = 0; j < upper - lower; j++) {
				for (int k = 0; k < front - back; k++) {

					if (mapColor[i][j][k] != null) {

						// left
						if (i == 0 || mapColor[i - 1][j][k] == null) {
							vertex_list.addAll(Arrays.asList(new Float[] {
									(float) (i + left), (float) (j + lower),
									(float) (k + back), (float) (i + left),
									(float) (j + lower + 1),
									(float) (k + back), (float) (i + left),
									(float) (j + lower + 1),
									(float) (k + back + 1), (float) (i + left),
									(float) (j + lower),
									(float) (k + back + 1), }));

							normal_list.addAll(Arrays.asList(new Float[] { -1f,
									0f, 0f, -1f, 0f, 0f, -1f, 0f, 0f, -1f, 0f,
									0f, }));

							for (int l = 0; l < 4; l++) {
								colors_list.addAll(Arrays
										.asList(mapColor[i][j][k]));
								glow_list.add(mapGlow[i][j][k]);
							}
						}

						// right
						if (i == right - left - 1
								|| mapColor[i + 1][j][k] == null) {
							vertex_list.addAll(Arrays.asList(new Float[] {
									(float) (i + left + 1),
									(float) (j + lower), (float) (k + back),
									(float) (i + left + 1),
									(float) (j + lower + 1),
									(float) (k + back), (float) (i + left + 1),
									(float) (j + lower + 1),
									(float) (k + back + 1),
									(float) (i + left + 1),
									(float) (j + lower),
									(float) (k + back + 1), }));

							normal_list.addAll(Arrays
									.asList(new Float[] { 1f, 0f, 0f, 1f, 0f,
											0f, 1f, 0f, 0f, 1f, 0f, 0f, }));

							for (int l = 0; l < 4; l++) {
								colors_list.addAll(Arrays
										.asList(mapColor[i][j][k]));
								glow_list.add(mapGlow[i][j][k]);
							}
						}

						// lower
						if (j == 0 || mapColor[i][j - 1][k] == null) {
							vertex_list.addAll(Arrays.asList(new Float[] {
									(float) (i + left), (float) (j + lower),
									(float) (k + back), (float) (i + left + 1),
									(float) (j + lower), (float) (k + back),
									(float) (i + left + 1),
									(float) (j + lower),
									(float) (k + back + 1), (float) (i + left),
									(float) (j + lower),
									(float) (k + back + 1), }));

							normal_list.addAll(Arrays.asList(new Float[] { 0f,
									-1f, 0f, 0f, -1f, 0f, 0f, -1f, 0f, 0f, -1f,
									0f, }));

							for (int l = 0; l < 4; l++) {
								colors_list.addAll(Arrays
										.asList(mapColor[i][j][k]));
								glow_list.add(mapGlow[i][j][k]);
							}
						}

						// upper
						if (j == upper - lower - 1
								|| mapColor[i][j + 1][k] == null) {
							vertex_list.addAll(Arrays.asList(new Float[] {
									(float) (i + left),
									(float) (j + lower + 1),
									(float) (k + back), (float) (i + left + 1),
									(float) (j + lower + 1),
									(float) (k + back), (float) (i + left + 1),
									(float) (j + lower + 1),
									(float) (k + back + 1), (float) (i + left),
									(float) (j + lower + 1),
									(float) (k + back + 1), }));

							normal_list.addAll(Arrays
									.asList(new Float[] { 0f, 1f, 0f, 0f, 1f,
											0f, 0f, 1f, 0f, 0f, 1f, 0f, }));

							for (int l = 0; l < 4; l++) {
								colors_list.addAll(Arrays
										.asList(mapColor[i][j][k]));
								glow_list.add(mapGlow[i][j][k]);
							}
						}

						// back
						if (k == 0 || mapColor[i][j][k - 1] == null) {
							vertex_list.addAll(Arrays.asList(new Float[] {
									(float) (i + left), (float) (j + lower),
									(float) (k + back), (float) (i + left + 1),
									(float) (j + lower), (float) (k + back),
									(float) (i + left + 1),
									(float) (j + lower + 1),
									(float) (k + back), (float) (i + left),
									(float) (j + lower + 1),
									(float) (k + back), }));

							normal_list.addAll(Arrays.asList(new Float[] { 0f,
									0f, -1f, 0f, 0f, -1f, 0f, 0f, -1f, 0f, 0f,
									-1f, }));

							for (int l = 0; l < 4; l++) {
								colors_list.addAll(Arrays
										.asList(mapColor[i][j][k]));
								glow_list.add(mapGlow[i][j][k]);
							}
						}

						// front
						if (k == front - back - 1
								|| mapColor[i][j][k + 1] == null) {
							vertex_list.addAll(Arrays.asList(new Float[] {
									(float) (i + left), (float) (j + lower),
									(float) (k + back + 1),
									(float) (i + left + 1),
									(float) (j + lower),
									(float) (k + back + 1),
									(float) (i + left + 1),
									(float) (j + lower + 1),
									(float) (k + back + 1), (float) (i + left),
									(float) (j + lower + 1),
									(float) (k + back + 1), }));

							normal_list.addAll(Arrays
									.asList(new Float[] { 0f, 0f, 1f, 0f, 0f,
											1f, 0f, 0f, 1f, 0f, 0f, 1f, }));

							for (int l = 0; l < 4; l++) {
								colors_list.addAll(Arrays
										.asList(mapColor[i][j][k]));
								glow_list.add(mapGlow[i][j][k]);
							}
						}
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

	public static void compileBuffer() {
		int vertices = vertex_list.size();

		// put into buffers
		FloatBuffer vertex_list_buffer = BufferUtils
				.createFloatBuffer(vertices);
		for (Float vertex : vertex_list) {
			vertex_list_buffer.put(vertex.floatValue());
		}
		vertex_list_buffer.flip();

		FloatBuffer normal_list_buffer = BufferUtils
				.createFloatBuffer(vertices);
		for (Float normal : normal_list) {
			normal_list_buffer.put(normal.floatValue());
		}
		normal_list_buffer.flip();

		FloatBuffer colors_list_buffer = BufferUtils
				.createFloatBuffer(vertices);
		for (Float colors : colors_list) {
			colors_list_buffer.put(colors.floatValue());
		}
		colors_list_buffer.flip();

		FloatBuffer glow_list_buffer = BufferUtils.createFloatBuffer(vertices);
		for (Float mode : glow_list) {
			glow_list_buffer.put(mode.floatValue());
		}
		glow_list_buffer.flip();

		// create vbo
		vbo_vertex_handle = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vbo_vertex_handle);
		glBufferData(GL_ARRAY_BUFFER, vertex_list_buffer, GL_STATIC_DRAW);
		glVertexPointer(vertex_size, GL_FLOAT, 0, 0l);

		vbo_normal_handle = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vbo_normal_handle);
		glBufferData(GL_ARRAY_BUFFER, normal_list_buffer, GL_STATIC_DRAW);
		glNormalPointer(GL_FLOAT, 0, 0l);

		vbo_color_handle = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vbo_color_handle);
		glBufferData(GL_ARRAY_BUFFER, colors_list_buffer, GL_STATIC_DRAW);
		glColorPointer(vertex_size, GL_FLOAT, 0, 0l);

		vbo_glow_handle = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vbo_glow_handle);
		glBufferData(GL_ARRAY_BUFFER, glow_list_buffer, GL_STATIC_DRAW);
		glVertexAttribPointer(6, 1, GL_FLOAT, false, 0, 0l);

		glBindBuffer(GL_ARRAY_BUFFER, 0);
	}

	public static ModelReference getErrorModel() {
		// TODO Auto-generated method stub
		return null;
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
