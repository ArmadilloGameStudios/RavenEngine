package com.armadillogamestudios.tactics.gameengine.scene.map;

import com.armadillogamestudios.engine2d.graphics2d.shader.LayerShader;
import com.armadillogamestudios.engine2d.input.MouseClickHandler;
import com.armadillogamestudios.engine2d.input.MouseHandler;
import com.armadillogamestudios.engine2d.scene.Layer;
import com.armadillogamestudios.engine2d.util.math.Vector2f;
import com.armadillogamestudios.engine2d.worldobject.GameObject;
import com.armadillogamestudios.engine2d.worldobject.WorldObject;

import java.util.*;

public class TileMap<S extends MapScene<S, ?, T, P>, T extends Tile<P>, P extends Pawn> extends WorldObject<S, WorldObject<S, ?>> {

    private final int tileWidth = 24, tileHeight = 20;

    private final int xSpan = 29, ySpan = 30;
    private final float mapMoveSpeed = .1f;
    private final TileMap<S, T, P>.TileDrawObject[][] tilesDrawObjects = new TileMap.TileDrawObject[xSpan][];
    private final List<TileMap<S, T, P>.PawnDrawObject> pawnDrawObjects = new ArrayList<>();
    boolean moveRight = false;
    boolean moveLeft = false;
    boolean moveDown = false;
    boolean moveUp = false;
    private int xOffset;
    private int yOffset;
    private final T[][] tiles;
    private final Vector2f tilePos = new Vector2f();
    private int pawnDrawIndex;

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
        xOffset = -((int) worldPos.x / tileWidth);
        yOffset = -((int) worldPos.y / (tileHeight - 7));

        pawnDrawIndex = 0;

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
        } else if (moveDown) {
            moveY(mapMoveSpeed * deltaTime);
        }

        if (moveRight) {
            moveX(-mapMoveSpeed * deltaTime);
        } else if (moveLeft) {
            moveX(mapMoveSpeed * deltaTime);
        }

        padPosition();
    }

    @Override
    public void setX(float x) {
        super.setX(x);
        padPosition();
    }

    @Override
    public void setY(float y) {
        super.setY(y);
        padPosition();
    }

    private void padPosition() {

        int max = getMaxX();
        if (getX() > max) {
            setX(max);
        }

        int min = getMinX();
        if (getX() < min) {
            setX(min);
        }

        max = getMaxY();
        if (getY() > max) {
            setY(max);
        }

        min = getMinY();
        if (getY() < min) {
            setY(min);
        }

    }

    @Override
    public Layer.Destination getDestination() {
        return Layer.Destination.Terrain;
    }

    @Override
    public float getZ() {
        return .2f;
    }

    public int getMaxY() {
        return 0;
    }

    private int getMaxX() {
        return 0;
    }

    public int getMinY() {
        return (tiles[0].length - ySpan) * -(tileHeight - 7) - (tileHeight - 8);
    }

    private int getMinX() {
        return (tiles[0].length - xSpan) * -tileWidth - (tileWidth - 2);
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


        private Vector2f unitPosition = new Vector2f();
        @Override
        public void draw(LayerShader shader) {

            T tile = tiles[x + xOffset][y + yOffset];

            if (!tile.isInCognito()) {
                shader.draw(
                        tiles[x + xOffset][y + yOffset].getSpriteSheet(getScene()),
                        null,
                        getTilePosition(x + xOffset, y + yOffset, getPosition()),
                        getScene().getWorldOffset(), null, getID(),
                        getZ(),
                        getFade(), getHighlight(), getScene().getDrawStyle());


                tiles[x + xOffset][y + yOffset].getPawns().forEach(pawn -> {

                    PawnDrawObject pawnDrawObject;

                    if (pawnDrawObjects.size() == pawnDrawIndex) {
                        pawnDrawObject = new PawnDrawObject();
                        pawnDrawObjects.add(pawnDrawObject);
                    } else {
                        pawnDrawObject = pawnDrawObjects.get(pawnDrawIndex);
                    }

                    pawnDrawObject.setPawn(pawn);

                    unitPosition.x = getPosition().x + 5;
                    unitPosition.y = getPosition().y + 1;

                    shader.draw(
                            pawn.getSpriteSheet(getScene()),
                            null,
                            getTilePosition(x + xOffset, y + yOffset, unitPosition),
                            getScene().getWorldOffset(), null, pawnDrawObject.getID(),
                            pawnDrawObject.getZ(),
                            getFade(), getHighlight(), getScene().getDrawStyle());

                    pawnDrawIndex++;
                });
            }
        }

        private Vector2f getTilePosition(int x, int y, Vector2f worldPosition) {
            int xOffset = 0;

            if (y % 2 == 0) {
                xOffset = tileWidth / 2 + x * tileWidth;
            } else {
                xOffset = x * tileWidth;
            }

            int yOffset = y * (tileHeight - 7);

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

    class PawnDrawObject extends GameObject<GameObject<?>> implements MouseClickHandler {

        private P pawn;

        public PawnDrawObject() {
            super();

            this.addMouseHandler(this);
        }

        public void setPawn(P pawn) {
            this.pawn = pawn;
        }

        @Override
        public void handleMouseClick() {
            getScene().handlePawnClick(pawn);
        }

        @Override
        public Layer.Destination getDestination() {
            return Layer.Destination.Terrain;
        }

        @Override
        public float getZ() {
            return TileMap.this.getZ() + .01f;
        }

        @Override
        public void needsRedraw() {

        }

        @Override
        public void draw(LayerShader shader) {

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
    }
}