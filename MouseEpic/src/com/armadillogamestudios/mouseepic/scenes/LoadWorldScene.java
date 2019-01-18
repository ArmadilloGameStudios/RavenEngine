package com.armadillogamestudios.mouseepic.scenes;

import com.armadillogamestudios.mouseepic.MouseEpicGame;
import com.armadillogamestudios.mouseepic.scenes.worldscene.WorldMapGenerator;
import com.armadillogamestudios.mouseepic.scenes.worldscene.WorldScene;
import com.raven.engine2d.database.GameData;
import com.raven.engine2d.graphics2d.DrawStyle;
import com.raven.engine2d.scene.Scene;
import com.raven.engine2d.ui.UIContainer;
import com.raven.engine2d.ui.UIImage;

import java.util.Random;

public class LoadWorldScene extends Scene<MouseEpicGame> {

    private GameData[] gameDataMap = null;
    private Random random = new Random();
    private int size = 16 * 4;

    public LoadWorldScene(MouseEpicGame game) {
        super(game);

        UIContainer<LoadWorldScene> container = new UIContainer<LoadWorldScene>(this) {
            @Override
            public int getStyle() {
                return UIContainer.CENTER;
            }
        };
        addChild(container);

        UIImage<LoadWorldScene> splash = new UIImage<>(this, 314, 64, "sprites/armadillo.png");
        container.addChild(splash);

        container.pack();
    }

    public DrawStyle getDrawStyle() {
        return DrawStyle.STANDARD;
    }

    @Override
    public void loadShaderTextures() {

    }

    @Override
    public void onEnterScene() {
        Runnable loadWorld = () -> gameDataMap = WorldMapGenerator.generateMap(size, random);

        Thread t = new Thread(loadWorld);
        t.start();
    }

    @Override
    public void onExitScene() {

    }

    @Override
    public void onUpdate(float deltaTime) {
        if (gameDataMap != null && this.getTime() > 2000f)
            getGame().prepTransitionScene(new WorldScene(getGame(), size, random, gameDataMap));
    }

    @Override
    public void inputKey(int key, int action, int mods) {

    }
}
