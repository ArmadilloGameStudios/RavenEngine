package com.armadillogamestudios.mouseepic.scenes.worldscene;

import com.armadillogamestudios.mouseepic.MouseEpicGame;
import com.armadillogamestudios.mouseepic.scenes.worldscene.terrain.Terrain;
import com.armadillogamestudios.mouseepic.scenes.worldscene.terrain.TerrainFactory;
import com.raven.engine2d.GameEngine;
import com.raven.engine2d.GameProperties;
import com.raven.engine2d.database.GameData;
import com.raven.engine2d.database.GameDatabase;
import com.raven.engine2d.graphics2d.DrawStyle;
import com.raven.engine2d.graphics2d.shader.ShaderTexture;
import com.raven.engine2d.scene.Scene;
import com.raven.engine2d.util.math.Vector2i;
import com.raven.engine2d.util.math.Vector3f;
import org.lwjgl.glfw.GLFW;

import java.util.*;


public class WorldScene extends Scene<MouseEpicGame> {
    private Random random = new Random();
    private MouseEntity mouse;
    private TerrainFactory terrainFactory;

    private int size = 20;

    public WorldScene(MouseEpicGame game) {
        super(game);
        load();
    }

    int tries = 0;

    private void load() {
        MouseEntity.loadData();

        terrainFactory = new TerrainFactory(this, GameDatabase.all("terrain"));

        Terrain[][] map = new Terrain[size][];

        for (int x = 0; x < size; x++) {
            map[x] = new Terrain[size];

            for (int j = 0; j < size; j++) {

                int y = j;
                if (x % 2 == 0) {
                    y = size - j - 1;
                }

                Terrain left = null;
                Terrain bottom = null;
                Terrain top = null;

                if (x != 0) {
                    left = map[x - 1][y];
                }
                if (y != 0) {
                    bottom = map[x][y - 1];
                }
                if (y != size - 1) {
                    top = map[x][y + 1];
                }

                terrainFactory.clear();
                terrainFactory.setLeft(left);
                terrainFactory.setBottom(bottom);
                terrainFactory.setTop(top);
                terrainFactory.setX(x);
                terrainFactory.setY(y);

                Terrain kid = terrainFactory.getInstance();

                if (kid != null) {
                    map[x][y] = kid;
                } else {
                    tries++;
                    if (tries < 12) {
                        for (; j >= 0; j--) {
                            y = j;
                            if (x % 2 == 0) {
                                y = size - j - 1;
                            }

                            mapRemove(map, x, y);

                            System.out.println("x: " + x + ", y: " + y);
                        }
                    } else {
                        tries = 0;
                        for (int k = 0; k < size; k++) {
                            mapRemove(map, x, k);
                        }
                        x--;
                        for (int k = 0; k < size; k++) {
                            mapRemove(map, x, k);
                        }
                        j = -1;
                    }
                }
            }
        }

//        mapCleanup(map);

        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                if (map[x][y] != null) {
                    addChild(map[x][y]);
                }
            }
        }

        mouse = new MouseEntity(this);

        addChild(mouse);

    }

    private void mapCleanup(Terrain[][] map) {
        if (Arrays.stream(map).anyMatch(t -> Arrays.stream(t).anyMatch(Objects::isNull))) {
            List<Vector2i> nulls = new ArrayList<>();

            for (int x = 0; x < size; x++) {
                for (int y = 0; y < size; y++) {
                    if (map[x][y] == null) {
                        nulls.add(new Vector2i(x, y));
                    }
                }
            }

            for (Vector2i pos : nulls) {
                if (pos.x - 1 > 0) {
                    mapRemove(map, pos.x - 1, pos.y);
                }
                if (pos.x + 1 < size) {
                    mapRemove(map, pos.x + 1, pos.y);
                }
                if (pos.y - 1 > 0) {
                    mapRemove(map, pos.x, pos.y - 1);
                }
                if (pos.y + 1 < size) {
                    mapRemove(map, pos.x, pos.y + 1);
                }
            }


            for (int x = 0; x < size; x++) {
                for (int y = 0; y < size; y++) {
                    if (map[x][y] == null) {

                        Terrain left = null;
                        Terrain right = null;
                        Terrain bottom = null;
                        Terrain top = null;

                        if (x != 0) {
                            left = map[x - 1][y];
                        }
                        if (x != size - 1) {
                            right = map[x + 1][y];
                        }
                        if (y != 0) {
                            bottom = map[x][y - 1];
                        }
                        if (y != size - 1) {
                            top = map[x][y + 1];
                        }

                        terrainFactory.clear();
                        terrainFactory.setX(x);
                        terrainFactory.setY(y);
                        terrainFactory.setLeft(left);
                        terrainFactory.setRight(right);
                        terrainFactory.setBottom(bottom);
                        terrainFactory.setTop(top);


                        Terrain kid = terrainFactory.getInstance();

                        if (kid != null) {
                            map[x][y] = kid;
                        }
                    }
                }
            }

            mapCleanup(map);
        }
    }

    private void mapRemove(Terrain[][] map, int x, int y) {
        if (map[x][y] != null) {
            removeGameObject(map[x][y]);
            map[x][y] = null;
        }
    }

    @Override
    public void loadShaderTextures() {
        List<ShaderTexture> textures = getShaderTextures();

        textures.addAll(Terrain.getSpriteSheets(this));
        textures.addAll(MouseEntity.getSpriteSheets(this));
    }

    @Override
    public void onEnterScene() {
        setBackgroundColor(new Vector3f(0, 0x57 / 256f, 0x84 / 256f));
    }

    @Override
    public void onExitScene() {

    }

    @Override
    public void onUpdate(float deltaTime) {

    }

    @Override
    public void inputKey(int key, int action, int mods) {
        switch (key) {
            case GLFW.GLFW_KEY_W:
                if (action == GLFW.GLFW_PRESS) {
                    mouse.setMovingUp(true);
                } else if (action == GLFW.GLFW_RELEASE) {
                    mouse.setMovingUp(false);
                }
                break;
            case GLFW.GLFW_KEY_S:
                if (action == GLFW.GLFW_PRESS) {
                    mouse.setMovingDown(true);
                } else if (action == GLFW.GLFW_RELEASE) {
                    mouse.setMovingDown(false);
                }
                break;
            case GLFW.GLFW_KEY_D:
                if (action == GLFW.GLFW_PRESS) {
                    mouse.setMovingRight(true);
                } else if (action == GLFW.GLFW_RELEASE) {
                    mouse.setMovingRight(false);
                }
                break;
            case GLFW.GLFW_KEY_A:
                if (action == GLFW.GLFW_PRESS) {
                    mouse.setMovingLeft(true);
                } else if (action == GLFW.GLFW_RELEASE) {
                    mouse.setMovingLeft(false);
                }
                break;
        }
    }

    @Override
    public DrawStyle getDrawStyle() {
        return DrawStyle.STANDARD;
    }

    public Random getRandom() {
        return random;
    }
}
