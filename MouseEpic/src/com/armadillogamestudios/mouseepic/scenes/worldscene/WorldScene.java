package com.armadillogamestudios.mouseepic.scenes.worldscene;

import com.armadillogamestudios.mouseepic.MouseEpicGame;
import com.armadillogamestudios.mouseepic.scenes.worldscene.terrain.Coast;
import com.armadillogamestudios.mouseepic.scenes.worldscene.terrain.Grass;
import com.armadillogamestudios.mouseepic.scenes.worldscene.terrain.Terrain;
import com.raven.engine2d.GameEngine;
import com.raven.engine2d.GameProperties;
import com.raven.engine2d.database.GameData;
import com.raven.engine2d.graphics2d.DrawStyle;
import com.raven.engine2d.graphics2d.shader.ShaderTexture;
import com.raven.engine2d.scene.Scene;
import com.raven.engine2d.util.math.Vector3f;
import org.lwjgl.glfw.GLFW;

import java.util.List;
import java.util.Random;


public class WorldScene extends Scene<MouseEpicGame> {
    private Random random = new Random();
    private MouseEntity mouse;

    public WorldScene(MouseEpicGame game) {
        super(game);
        load();
    }

    private void load() {
        Terrain.loadData();
        MouseEntity.loadData();

        for (int i = 2; i < 18; i++) {
            for (int j = 2; j < 18; j++) {
                Grass grass = new Grass(this, i, j);
                addChild(grass);
            }

            Coast coast = new Coast(this, 1, i, Coast.Side.LEFT);
            addChild(coast);

            coast = new Coast(this, 18, i, Coast.Side.RIGHT);
            addChild(coast);

            coast = new Coast(this, i, 1, Coast.Side.BOTTOM);
            addChild(coast);

            coast = new Coast(this, i, 18, Coast.Side.TOP);
            addChild(coast);
        }

        mouse = new MouseEntity(this);

        addChild(mouse);

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
