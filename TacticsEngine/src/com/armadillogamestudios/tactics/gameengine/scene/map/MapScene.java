package com.armadillogamestudios.tactics.gameengine.scene.map;

import com.armadillogamestudios.engine2d.util.math.Vector3f;
import com.armadillogamestudios.tactics.gameengine.game.TacticsGame;
import com.armadillogamestudios.tactics.gameengine.scene.TacticsScene;


public abstract class MapScene<S extends MapScene<S, G, T, P>, G extends TacticsGame<G>, T extends Tile<P>, P extends Pawn> extends TacticsScene<G> {

    private Speed speed = Speed.Normal;
    private boolean pausedTick = true;
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
    public final void onUpdate(float deltaTime) {
        if (pausedTick) {
            timeSinceLastTick = 0f;
        } else {
            timeSinceLastTick += deltaTime;

            if (timeSinceLastTick >= speed.getValue()) {
                // TODO multithread tick
                // TODO tick beforehand but don't apply until tick
                tick();
                timeSinceLastTick = 0f;
            }
        }
    }

    public final boolean getPausedTick() {
        return pausedTick;
    }

    public final void setPausedTick(boolean pausedTick) {
        this.pausedTick = pausedTick;

        updatePlayPause();
    }

    protected Speed getSpeed() {
        return speed;
    }

    protected void setSpeed(Speed speed) {
        this.speed = speed;
        this.pausedTick = false;

        updatePlayPause();
    }

    public final void focus(T tile) {
        if (tile.getY() % 2 == 0) {
            getTileMap().setX(-24 * tile.getX() + 312);
        } else {
            getTileMap().setX(-24 * tile.getX() + 324);
        }

        getTileMap().setY(-13 * tile.getY() + 180);
    }

    protected abstract void updatePlayPause();

    protected abstract void updateUI();

    protected abstract void tick();

    protected abstract TileMap<S, T, P> getTileMap();

    public abstract void handleTileClick(T tile);

    public abstract void handlePawnClick(P pawn);

    public enum Speed {
        Slow(500f), Normal(250f), Fast(125f), Fastest(0f);

        private final float value;

        Speed(float value) {
            this.value = value;
        }

        private float getValue() {
            return value;
        }
    }
}