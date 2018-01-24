package com.raven.breakingsands.scenes.terrain;

import com.raven.breakingsands.scenes.TestScene;
import com.raven.breakingsands.scenes.decal.Decal;
import com.raven.breakingsands.scenes.pawn.Pawn;
import com.raven.engine.GameEngine;
import com.raven.engine.database.GameData;
import com.raven.engine.database.GameDataList;
import com.raven.engine.database.GameDataQuery;
import com.raven.engine.database.GameDatabase;
import com.raven.engine.graphics3d.ModelData;
import com.raven.engine.util.pathfinding.PathAdjacentNode;
import com.raven.engine.util.pathfinding.PathNode;
import com.raven.engine.worldobject.MouseHandler;
import com.raven.engine.worldobject.WorldObject;

import java.util.ArrayList;
import java.util.List;

public class Terrain extends WorldObject<TestScene, WorldObject> implements MouseHandler, PathNode<Terrain> {
    private static GameDataList dataList = GameDatabase.all("terrain");

    public enum State {
        SELECTABLE,
        UNSELECTABLE,
        SELECTED,
    }

    private State state;

    private int x, y;

    private boolean passable = true;

    private Decal decal;
    private Pawn pawn;

    public Terrain(TestScene scene, String name, int x, int y) {
        super(scene, dataList.queryRandom(new GameDataQuery() {
            @Override
            public boolean matches(GameData row) {
                return row.getString("name").matches(name);
            }
        }).getString("model"));

        this.x = x;
        this.y = y;

        this.addMouseHandler(this);

        this.setState(State.UNSELECTABLE);
    }

    @Override
    public void handleMouseClick() {
        switch (state) {
            case SELECTED:
                getScene().clearAllPaths();
                getScene().setState(TestScene.State.MOVING);
                break;
        }
    }

    @Override
    public void handleMouseEnter() {
        if (getScene().getState() == TestScene.State.MOVE) {
            getScene().selectPath(this);
        }
    }

    @Override
    public void handleMouseLeave() {
        if (getScene().getState() == TestScene.State.MOVE) {
            getScene().clearPath();
        }
    }

    @Override
    public void handleMouseHover(float delta) {

    }

    @Override
    public List<PathAdjacentNode<Terrain>> getAdjacentNodes() {
        List<PathAdjacentNode<Terrain>> neighbors = new ArrayList<>();

        TestScene scene = this.getScene();
        Terrain[][] map = scene.getTerrainMap();
        int size = scene.getTerrainMapSize();

        Terrain a1 = null, a2 = null, b1 = null, b2 = null;

        if (x + 1 < size) {
            Terrain n = map[x + 1][y];

            if (n.passable && n.pawn == null) {
                a1 = n;
                neighbors.add(new PathAdjacentNode<>(n, 2));
            }
        }

        if (y + 1 < size) {
            Terrain n = map[x][y + 1];

            if (n.passable && n.pawn == null) {
                b1 = n;
                neighbors.add(new PathAdjacentNode<>(n, 2));
            }
        }

        if (y - 1 >= 0) {
            Terrain n = map[x][y - 1];

            if (n.passable && n.pawn == null) {
                b2 = n;
                neighbors.add(new PathAdjacentNode<>(n, 2));
            }
        }

        if (x - 1 >= 0) {
            Terrain n = map[x - 1][y];

            if (n.passable && n.pawn == null) {
                a2 = n;
                neighbors.add(new PathAdjacentNode<>(n, 2));
            }
        }

//        if (a1 != null && b1 != null) {
//            Terrain n = map[x + 1][y + 1];
//
//            if (n.passable && scene.getActivePawn() != n.pawn) {
//                neighbors.add(new PathAdjacentNode<>(n, 3));
//            }
//        }
//
//        if (a1 != null && b2 != null) {
//            Terrain n = map[x + 1][y - 1];
//
//            if (n.passable && scene.getActivePawn() != n.pawn) {
//                neighbors.add(new PathAdjacentNode<>(n, 3));
//            }
//        }
//
//        if (a2 != null && b1 != null) {
//            Terrain n = map[x - 1][y + 1];
//
//            if (n.passable && scene.getActivePawn() != n.pawn) {
//                neighbors.add(new PathAdjacentNode<>(n, 3));
//            }
//        }
//
//        if (a2 != null && b2 != null) {
//            Terrain n = map[x - 1][y - 1];
//
//            if (n.passable && scene.getActivePawn() != n.pawn) {
//                neighbors.add(new PathAdjacentNode<>(n, 3));
//            }
//        }

        return neighbors;
    }

    public static List<ModelData> getModelData() {
        List<ModelData> data = new ArrayList<>();

        for (GameData gameData : dataList) {
            data.add(GameEngine.getEngine().getModelData(gameData.getString("model")));
        }

        return data;
    }

    public void setDecal(Decal decal) {
        if (this.decal != null) {
            removeChild(this.decal);
        }

        this.decal = decal;

        this.passable = decal.isPassable();

        this.addChild(decal);
    }

    public void setPawn(Pawn pawn) {
        if (this.pawn != null) {
            removeChild(this.pawn);
        }

        if (pawn.getParent() != null)
            pawn.getParent().removePawn();

        this.pawn = pawn;

        this.addChild(pawn);
    }

    public void removePawn() {
        this.removeChild(this.pawn);
        this.pawn = null;
    }

    public void setState(State state) {
        this.state = state;

        switch (state) {
            case UNSELECTABLE:
                break;
            case SELECTABLE:
                if (this.pawn == getScene().getActivePawn()) {
                    this.state = State.UNSELECTABLE;
                }
                break;
        }

        selectHighlight();
    }

    private void selectHighlight() {
        switch (state) {
            case UNSELECTABLE:
                if (getScene().getState() == TestScene.State.MOVING) {
                    setHighlight(TestScene.OFF);
                } else {
                    if (pawn == getScene().getActivePawn()) {
                        setHighlight(TestScene.GREEN);
                    } else {
                        setHighlight(TestScene.OFF);
                    }
                }
                break;
            case SELECTABLE:
                if (pawn != null) {
                    setHighlight(TestScene.GREEN_CHANGING);
                } else {
                    if (passable) {
                        setHighlight(TestScene.BLUE_CHANGING);
                    } else
                        setHighlight(TestScene.YELLOW_CHANGING);
                }
                break;
            case SELECTED:
                if (pawn != null) {
                    setHighlight(TestScene.GREEN);
                } else {
                    if (passable) {
                        setHighlight(TestScene.BLUE);
                    } else
                        setHighlight(TestScene.YELLOW);
                }
                break;
        }
    }
}
