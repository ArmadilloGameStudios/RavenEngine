package com.armadillogamestudios.tactics.gameengine.scene.map;

import com.armadillogamestudios.engine2d.graphics2d.shader.LayerShader;
import com.armadillogamestudios.engine2d.input.MouseHandler;
import com.armadillogamestudios.engine2d.scene.Layer;
import com.armadillogamestudios.engine2d.util.math.Vector2f;
import com.armadillogamestudios.engine2d.worldobject.GameObject;
import com.armadillogamestudios.engine2d.worldobject.WorldObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TileMap<S extends MapScene<S, ?, T>, T extends Tile> extends WorldObject<S, WorldObject<S, ?>> {

    private final int xSpan = 29, ySpan = 22;
    private int xOffset;
    private int yOffset;
    private final float mapMoveSpeed = .1f;
    boolean moveRight = false;
    boolean moveLeft = false;
    boolean moveDown = false;
    boolean moveUp = false;
    private T[][] tiles;
    private final TileMap<S, T>.TileDrawObject[][] tilesDrawObjects = new TileMap.TileDrawObject[xSpan][];
    private Vector2f tilePos = new Vector2f();

    public TileMap(S scene, T[][] tiles) {
        super(scene);
        scene.addKeyboardHandler(new MapKeyboardHandler(this));

        this.tiles = tiles;

        for (int x = 0; x < tiles.length; x++) {
            for (int y = 0; y < tiles[x].length; y++) {
                tiles[x][y].load(scene);
            }
        }
        for (int x = 0; x < xSpan; x++) {
            tilesDrawObjects[x] = new TileMap.TileDrawObject[ySpan];

            for (int y = 0; y < ySpan; y++) {
                tilesDrawObjects[x][y] = new TileDrawObject(x, y);
            }
        }
    }

    @Override
    public void draw(LayerShader shader) {

        Vector2f worldPos = getPosition();
        xOffset = -((int) worldPos.x / 24);
        yOffset = -((int) worldPos.y / 18);

        for (int x = 0; x < xSpan; x++) {
            for (int y = 0; y < ySpan; y++) {
                tilesDrawObjects[x][y].draw(shader);
            }
        }
    }

    @Override
    public void onUpdate(float deltaTime) {
        super.onUpdate(deltaTime);

        if (moveUp) {
            moveY(-mapMoveSpeed * deltaTime);

            int min = getMinY();
            if (getY() < min) {
                setY(min);
            }
        } else if (moveDown) {
            moveY(mapMoveSpeed * deltaTime);

            int max = getMaxY();
            if (getY() > max) {
                setY(max);
            }
        }

        if (moveRight) {
            moveX(-mapMoveSpeed * deltaTime);

            int min = getMinX();
            if (getX() < min) {
                setX(min);
            }
        } else if (moveLeft) {
            moveX(mapMoveSpeed * deltaTime);

            int max = getMaxX();
            if (getX() > max) {
                setX(max);
            }
        }
    }

    @Override
    public Layer.Destination getDestination() {
        return Layer.Destination.Terrain;
    }

    @Override
    public float getZ() {
        return .1f;
    }

    public int getMaxY() {
        return 0;
    }

    private int getMaxX() {
        return 0;
    }

    public int getMinY() {
        return (tiles[0].length - ySpan) * -18 - 16;
    }

    private int getMinX() {
        return (tiles[0].length - xSpan) * -24 - 22;
    }

    class TileDrawObject extends GameObject<GameObject<?>> implements MouseHandler {

        private final int x, y;

        public TileDrawObject(int x, int y) {
            super();

            this.x = x;
            this.y = y;

            this.addMouseHandler(this);
        }

        @Override
        public Layer.Destination getDestination() {
            return Layer.Destination.Terrain;
        }

        @Override
        public float getZ() {
            return TileMap.this.getZ() - .001f * y;
        }

        @Override
        public void needsRedraw() {
            TileMap.this.needsRedraw();
        }

        @Override
        public void draw(LayerShader shader) {
            shader.draw(
                    tiles[x + xOffset][y + yOffset].getSpriteSheet(getScene()),
                    null,
                    getTilePosition(x + xOffset, y + yOffset, getPosition()),
                    getScene().getWorldOffset(), null, getID(),
                    getWorldZ(),
                    getFade(), getHighlight(), getScene().getDrawStyle());
        }

        private Vector2f getTilePosition(int x, int y, Vector2f worldPosition) {
            int xOffset = 0;

            if (y % 2 == 0) {
                xOffset = 12 + x * 24;
            } else {
                xOffset = x * 24;
            }

            int yOffset = y * 18;

            tilePos.x = worldPosition.x + xOffset;
            tilePos.y = worldPosition.y + yOffset;

            return tilePos;
        }

        @Override
        public List<GameObject<?>> getParentGameObjectList() {
            return Collections.emptyList();
        }

        @Override
        public GameObject<?> getParent() {
            return TileMap.this;
        }

        @Override
        public void setParent(GameObject<?> parent) {
            throw new IllegalArgumentException();
        }

        @Override
        public void update(float delta) {

        }

        @Override
        public void addChild(GameObject<?> obj) {

        }

        @Override
        public List<GameObject<?>> getChildren() {
            return Collections.emptyList();
        }

        @Override
        public void handleMouseClick() {
            getScene().handleTileClick(tiles[x + xOffset][y + yOffset]);
        }

        @Override
        public void handleMouseEnter() {

        }

        @Override
        public void handleMouseLeave() {

        }

        @Override
        public void handleMouseHover(float delta) {

        }
    }
}