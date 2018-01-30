package com.raven.breakingsands.scenes.battlescene.terrain;

import com.raven.breakingsands.scenes.battlescene.BattleScene;
import com.raven.breakingsands.scenes.battlescene.decal.Decal;
import com.raven.breakingsands.scenes.battlescene.pawn.Pawn;
import com.raven.engine.GameEngine;
import com.raven.engine.database.GameData;
import com.raven.engine.database.GameDataList;
import com.raven.engine.database.GameDataQuery;
import com.raven.engine.database.GameDatabase;
import com.raven.engine.graphics3d.ModelData;
import com.raven.engine.scene.Layer;
import com.raven.engine.util.pathfinding.PathAdjacentNode;
import com.raven.engine.util.pathfinding.PathNode;
import com.raven.engine.worldobject.MouseHandler;
import com.raven.engine.worldobject.TextObject;
import com.raven.engine.worldobject.WorldObject;

import java.util.ArrayList;
import java.util.List;

public class Terrain extends WorldObject<BattleScene, Layer<WorldObject>, WorldObject>
        implements MouseHandler, PathNode<Terrain> {

    private static GameDataList dataList = GameDatabase.all("terrain");

    public static List<ModelData> getModelData() {
        List<ModelData> data = new ArrayList<>();

        for (GameData gameData : dataList) {
            data.add(GameEngine.getEngine().getModelData(gameData.getString("model")));
        }

        return data;
    }

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

    private TextObject details;

    public Terrain(BattleScene scene, String name, int x, int y) {
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

        details = new TextObject(85, 85);

        updateText();
    }

    @Override
    public void handleMouseClick() {
        switch (state) {
            case SELECTED:
                getScene().clearAllPaths();
                getScene().setState(BattleScene.State.MOVING);
                break;
        }
    }

    @Override
    public void handleMouseEnter() {
        if (!getScene().isPaused()) {
            if (getScene().getState() == BattleScene.State.MOVE) {
                getScene().selectPath(this);
            }

            getScene().setDetailText(details);
        }
    }

    @Override
    public void handleMouseLeave() {
        if (getScene().getState() == BattleScene.State.MOVE) {
            getScene().clearPath();
        }
    }

    @Override
    public void handleMouseHover(float delta) {

    }

    @Override
    public List<PathAdjacentNode<Terrain>> getAdjacentNodes() {
        List<PathAdjacentNode<Terrain>> neighbors = new ArrayList<>();

        BattleScene scene = this.getScene();
        Terrain[][] map = scene.getTerrainMap();
        int size = scene.getTerrainMapSize();

        Terrain a1 = null, a2 = null, b1 = null, b2 = null;

        if (x + 1 < size) {
            Terrain n = map[x + 1][y];

            if (n.passable && n.pawn == null) {
                a1 = n;
                neighbors.add(new PathAdjacentNode<>(n, 1));
            }
        }

        if (y + 1 < size) {
            Terrain n = map[x][y + 1];

            if (n.passable && n.pawn == null) {
                b1 = n;
                neighbors.add(new PathAdjacentNode<>(n, 1));
            }
        }

        if (y - 1 >= 0) {
            Terrain n = map[x][y - 1];

            if (n.passable && n.pawn == null) {
                b2 = n;
                neighbors.add(new PathAdjacentNode<>(n, 1));
            }
        }

        if (x - 1 >= 0) {
            Terrain n = map[x - 1][y];

            if (n.passable && n.pawn == null) {
                a2 = n;
                neighbors.add(new PathAdjacentNode<>(n, 1));
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

    public void setDecal(Decal decal) {
        if (this.decal != null) {
            removeChild(this.decal);
        }

        this.decal = decal;

        this.passable = decal.isPassable();

        this.addChild(decal);

        updateText();
    }

    public void setPawn(Pawn pawn) {
        if (this.pawn != null) {
            removeChild(this.pawn);
        }

        if (pawn.getParent() != null)
            pawn.getParent().removePawn();

        this.pawn = pawn;

        this.addChild(pawn);

        updateText();
    }

    public void removePawn() {
        this.removeChild(this.pawn);
        this.pawn = null;

        updateText();
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

    public TextObject getDetailText() {
        return details;
    }

    private void selectHighlight() {
        switch (state) {
            case UNSELECTABLE:
                if (getScene().getState() == BattleScene.State.MOVING) {
                    setHighlight(BattleScene.OFF);
                } else {
                    if (pawn == getScene().getActivePawn()) {
                        setHighlight(BattleScene.GREEN);
                    } else {
                        setHighlight(BattleScene.OFF);
                    }
                }
                break;
            case SELECTABLE:
                if (pawn != null) {
                    setHighlight(BattleScene.GREEN_CHANGING);
                } else {
                    if (passable) {
                        setHighlight(BattleScene.BLUE_CHANGING);
                    } else
                        setHighlight(BattleScene.YELLOW_CHANGING);
                }
                break;
            case SELECTED:
                if (pawn != null) {
                    setHighlight(BattleScene.GREEN);
                } else {
                    if (passable) {
                        setHighlight(BattleScene.BLUE);
                    } else
                        setHighlight(BattleScene.YELLOW);
                }
                break;
        }
    }

    private void updateText() {
        String text = "";

        if (decal != null) {
            text += "Terrain:\n" + decal.getDescription();
        } else {
            text += "Terrain:\nSand";
        }

        if (pawn != null) {
            text += "\n" + pawn.getName();
            text += "\n" + pawn.getWeapon().getName();
        }

        details.setText(text);
    }
}
