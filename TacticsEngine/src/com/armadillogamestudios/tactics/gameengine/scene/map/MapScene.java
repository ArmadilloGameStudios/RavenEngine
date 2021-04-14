package com.armadillogamestudios.tactics.gameengine.scene.map;

import com.armadillogamestudios.engine2d.util.math.Vector3f;
import com.armadillogamestudios.tactics.gameengine.game.TacticsGame;
import com.armadillogamestudios.tactics.gameengine.scene.TacticsScene;


public abstract class MapScene<S extends MapScene<S, G, T>, G extends TacticsGame<G>, T extends Tile> extends TacticsScene<G> {

    private final float speed = Speed.Fast;
    private final boolean paused = false;
    private float timeSinceLastTick = 0;

    public MapScene(final G game) {
        super(game);

        this.getWorldOffset().x = -16;
        this.getWorldOffset().y = -12;
    }

    @Override
    protected void loadUI() {
        // Background
        setBackgroundColor(new Vector3f(0, 0, 0));

        // Create world objects
        addChild(getTileMap());
    }

    @Override
    public void updateUI(float deltaTime) {
        if (paused) {
            timeSinceLastTick = 0f;
        } else {
            timeSinceLastTick += deltaTime;

            if (timeSinceLastTick >= speed) {
                // TODO tick beforehand but don't apply until tick
                // TODO multithread tick
                tick();
                timeSinceLastTick = 0f;
            }
        }
    }

    protected abstract void tick();

    protected abstract TileMap<S, T> getTileMap();

    public abstract void handleTileClick(T tile);

    private static class Speed {
        public static final float Slow = 500f;
        public static final float Normal = 250f;
        public static final float Fast = 125f;
        public static final float Fastest = 0f;
    }
}
