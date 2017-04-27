package com.crookedbird.tactician.generation;

import java.util.ArrayList;
import java.util.List;

import com.crookedbird.tactician.battle.BattleScene;
import com.crookedbird.tactician.battle.Level;
import com.crookedbird.tactician.battle.Terrain;

public class Generation {

	public enum TerrainType {
		Ground, Wall, Obstacle
	}

	public static Level LevelGeneration(LevelGenerationProperties lgp,
			BattleScene bs) {
		Level level = new Level(bs);

		// Set Theme
		String theme = "Sand";

		// Create TerrainType
		TerrainType[][] terrainType = new TerrainType[lgp.getWidth()][];

		for (int i = 0; i < lgp.getWidth(); i++) {
			terrainType[i] = new TerrainType[lgp.getHeight()];

			for (int j = 0; j < lgp.getHeight(); j++) {
				terrainType[i][j] = TerrainType.Ground;
			}
		}

		// Add Obstacles
		int obstacleCount = lgp.getRandom().nextInt(5) + 15;
		for (int i = 0; i < obstacleCount; i++) {
			int x = lgp.getRandom().nextInt(lgp.getWidth());
			int y = lgp.getRandom().nextInt(lgp.getHeight());

			terrainType[x][y] = TerrainType.Obstacle;
		}

		// Add Walls
		int wallCount = lgp.getRandom().nextInt(10) + 7;
		for (int i = 0; i < wallCount; i++) {
			int o = lgp.getRandom().nextInt(4);
			int l = lgp.getRandom().nextInt(6) + 2;
			int x1 = lgp.getRandom().nextInt(lgp.getWidth());
			// int x2 = lgp.getWidth() - 1 - x1;
			int y1 = lgp.getRandom().nextInt(lgp.getHeight());
			int y2 = lgp.getWidth() - 1 - y1;

			for (; l >= 0; l--) {
				if (x1 >= 0 && x1 < lgp.getWidth() && y1 >= 0
						&& y1 < lgp.getHeight()) {

					terrainType[x1][y1] = TerrainType.Wall;
				}

//				if (x1 >= 0 && x1 < lgp.getWidth() && y2 >= 0
//						&& y2 < lgp.getHeight()) {
//
//					terrainType[x1][y2] = TerrainType.Wall;
//				}

				switch (o) {
				case 0:
					x1++;
					break;
				case 1:
					x1--;
					break;
				case 2:
					y1++;
					y2--;
					break;
				case 3:
					y1--;
					y2++;
					break;
				}
			}

		}

		// Add Outer Walls
		int x1 = 0;
		int x2 = lgp.getWidth() - 1;
		int y1 = 0;
		int y2 = lgp.getHeight() - 1;

		for (int x = 0; x < lgp.getWidth(); x++) {
			terrainType[x][y1] = TerrainType.Wall;
			terrainType[x][y2] = TerrainType.Wall;
		}

		for (int y = 0; y < lgp.getHeight(); y++) {
			terrainType[x1][y] = TerrainType.Wall;
			terrainType[x2][y] = TerrainType.Wall;
		}

		// Add Enterances
		int midx = lgp.getWidth() / 2;
		// int midy = lgp.getWidth() / 2;

		// -bottom
		terrainType[midx][0] = TerrainType.Ground;
		terrainType[midx + 1][0] = TerrainType.Ground;
		terrainType[midx - 1][0] = TerrainType.Ground;
		terrainType[midx][1] = TerrainType.Ground;
		terrainType[midx + 1][1] = TerrainType.Ground;
		terrainType[midx - 1][1] = TerrainType.Ground;

		// -top
		terrainType[midx][lgp.getHeight() - 1] = TerrainType.Ground;
		terrainType[midx + 1][lgp.getHeight() - 1] = TerrainType.Ground;
		terrainType[midx - 1][lgp.getHeight() - 1] = TerrainType.Ground;
		terrainType[midx][lgp.getHeight() - 2] = TerrainType.Ground;
		terrainType[midx + 1][lgp.getHeight() - 2] = TerrainType.Ground;
		terrainType[midx - 1][lgp.getHeight() - 2] = TerrainType.Ground;

		// Create Terrain
		Terrain[][] terrain = new Terrain[lgp.getWidth()][];

		for (int i = 0; i < lgp.getWidth(); i++) {
			terrain[i] = new Terrain[lgp.getHeight()];

			for (int j = 0; j < lgp.getHeight(); j++) {
				List<String> type = new ArrayList<String>();

				switch (terrainType[i][j]) {
				case Ground:
					type.add("Ground");
					break;
				case Wall:
					type.add("Wall");

					if (j + 1 < lgp.getHeight()
							&& terrainType[i][j + 1] == TerrainType.Wall) {
						type.add("Bottom");
					}
					if (i - 1 >= 0 && terrainType[i - 1][j] == TerrainType.Wall) {
						type.add("Left");
					}
					if (i + 1 < lgp.getWidth()
							&& terrainType[i + 1][j] == TerrainType.Wall) {
						type.add("Right");
					}
					if (j - 1 >= 0 && terrainType[i][j - 1] == TerrainType.Wall) {
						type.add("Top");
					}
					break;
				case Obstacle:
					type.add("Obstacle");
					break;
				}

				terrain[i][j] = new Terrain(level, theme, type, i * 16, j * 16);
			}
		}

		level.setTerrain(terrain);

		return level;
	}
}
