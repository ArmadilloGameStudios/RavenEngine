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

    public Pawn getPawn() {
        return pawn;
    }

    public enum State {
        MOVEABLE,
        UNSELECTABLE,
        ATTACKABLE,
        MOVE, ATTACK
    }

    private State state;

    public float cover = 0f;
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

    public int getMapX() {
        return x;
    }

    public int getMapY() {
        return y;
    }

    @Override
    public void handleMouseClick() {
        switch (getScene().getState()) {
            case SELECT_MOVE:
                if (pawn == getScene().getActivePawn()) {
                    getScene().selectNextPawn();
                } else
                    switch (state) {
                        case MOVE:
                            getScene().clearAllPaths();
                            getScene().setState(BattleScene.State.MOVING);
                            break;
                        case ATTACK:
                            getScene().getActivePawn().attack(getPawn());
                            getScene().selectNextPawn();
                            break;
                    }
                break;
        }
    }

    @Override
    public void handleMouseEnter() {
        if (!getScene().isPaused()) {
            switch (getScene().getState()) {
                case SELECT_MOVE:
                    switch (state) {
                        case MOVEABLE:
                            getScene().selectPath(this);
                            break;
                        case ATTACKABLE:
                            setState(State.ATTACK);
                            break;
                    }
                    break;
            }

            getScene().setDetailText(details);
        }
    }

    @Override
    public void handleMouseLeave() {
        switch (getScene().getState()) {
            case SELECT_MOVE:
                getScene().clearPath();

                if (state == State.ATTACK) {
                    setState(State.ATTACKABLE);
                }
                break;
        }
    }

    @Override
    public void handleMouseHover(float delta) {

    }

    @Override
    public List<PathAdjacentNode<Terrain>> getAdjacentNodes() {
        switch (getScene().getState()) {
            case SELECT_MOVE:
            case SELECT_MOVE_AI:
                return getMovementNodes();
        }

        return new ArrayList<>();
    }

    private List<PathAdjacentNode<Terrain>> getMovementNodes() {
        List<PathAdjacentNode<Terrain>> neighbors = new ArrayList<>();

        Terrain[][] map = getScene().getTerrainMap();
        int size = getScene().getTerrainMapSize();

        if (x + 1 < size) {
            Terrain n = map[x + 1][y];

            if (n.passable && n.pawn == null) {
                neighbors.add(new PathAdjacentNode<>(n, 1));
            }
        }

        if (y + 1 < size) {
            Terrain n = map[x][y + 1];

            if (n.passable && n.pawn == null) {
                neighbors.add(new PathAdjacentNode<>(n, 1));
            }
        }

        if (y - 1 >= 0) {
            Terrain n = map[x][y - 1];

            if (n.passable && n.pawn == null) {
                neighbors.add(new PathAdjacentNode<>(n, 1));
            }
        }

        if (x - 1 >= 0) {
            Terrain n = map[x - 1][y];

            if (n.passable && n.pawn == null) {
                neighbors.add(new PathAdjacentNode<>(n, 1));
            }
        }

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
            case MOVEABLE:
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
        switch (getScene().getState()) {
            case MOVING:
                setHighlight(BattleScene.OFF);
                break;
            case SELECT_MOVE:
                switch (state) {
                    case UNSELECTABLE:
                        if (pawn == getScene().getActivePawn()) {
                            setHighlight(BattleScene.GREEN);
                        } else {
                            setHighlight(BattleScene.OFF);
                        }
                        break;
                    case MOVEABLE:
                        if (pawn != null) {
                            setHighlight(BattleScene.GREEN_CHANGING);
                        } else {
                            if (passable) {
                                setHighlight(BattleScene.BLUE_CHANGING);
                            } else
                                setHighlight(BattleScene.YELLOW_CHANGING);
                        }
                        break;
                    case MOVE:
                        if (pawn != null) {
                            setHighlight(BattleScene.GREEN);
                        } else {
                            if (passable) {
                                setHighlight(BattleScene.BLUE);
                            } else
                                setHighlight(BattleScene.YELLOW);
                        }
                        break;
                    case ATTACK:
                        setHighlight(BattleScene.RED);
                        break;
                    case ATTACKABLE:
                        setHighlight(BattleScene.RED_CHANGING);
                        break;
                }
                break;
        }
    }

    public void updateText() {
        String text = "" + x + ", " + y + "\n" + cover + "\n";

        if (decal != null) {
            text += "Terrain:\n" + decal.getDescription();
        } else {
            text += "Terrain:\nSand";
        }

        if (pawn != null) {
//            text += "\n" + pawn.getName();
//            text += "\n" + pawn.getHitPoints();
//            text += "\n" + pawn.getWeapon().getName();
            text += "\n" + pawn.getArmor().getName();
        }

        details.setText(text);
    }

    public boolean isPassable() {
        return passable;
    }
}
