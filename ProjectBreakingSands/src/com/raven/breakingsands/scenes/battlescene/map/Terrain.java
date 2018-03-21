package com.raven.breakingsands.scenes.battlescene.map;

import com.raven.breakingsands.scenes.battlescene.BattleScene;
import com.raven.breakingsands.scenes.battlescene.decal.Decal;
import com.raven.breakingsands.scenes.battlescene.decal.DecalFactory;
import com.raven.breakingsands.scenes.battlescene.pawn.Pawn;
import com.raven.engine.GameEngine;
import com.raven.engine.database.GameData;
import com.raven.engine.database.GameDataList;
import com.raven.engine.database.GameDatabase;
import com.raven.engine.graphics3d.model.ModelData;
import com.raven.engine.util.pathfinding.PathAdjacentNode;
import com.raven.engine.util.pathfinding.PathNode;
import com.raven.engine.worldobject.MouseHandler;
import com.raven.engine.worldobject.TextObject;
import com.raven.engine.worldobject.WorldObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Terrain extends WorldObject<BattleScene, Structure, WorldObject>
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
        UNSELECTABLE,
        MOVEABLE,
        MOVE,
        ATTACKABLE,
        ATTACK
    }

    private State state;

    public float cover = 0f;
    private int x, y;

    private boolean passable = true;

    private Decal decal;
    private Pawn pawn;

    private TextObject details;

    public Terrain(BattleScene scene, Structure structure, GameData terrainData, GameData propData) {
        super(scene, terrainData.getString("model"));

        switch (structure.getMapRotation()) {
            default:
            case 0:
                this.x = propData.getInteger("x") + structure.getMapX();
                this.y = propData.getInteger("y") + structure.getMapY();
                break;
            case 1:
                this.x = (structure.getWidth() - propData.getInteger("y"))
                        + structure.getMapX() - 1;
                this.y = propData.getInteger("x") + structure.getMapY();
                break;
            case 2:
                this.x = (structure.getWidth() - propData.getInteger("x"))
                                + structure.getMapX() - 1;
                this.y = (structure.getHeight() - propData.getInteger("y"))
                                + structure.getMapY() - 1;
                break;
            case 3:
                this.y = (structure.getHeight() - propData.getInteger("x"))
                        + structure.getMapY() - 1;
                this.x = propData.getInteger("y") + structure.getMapX();
                break;
        }

        setRotation(-90 * structure.getMapRotation());
        setX((this.x - structure.getMapX()) * 2);
        setZ((this.y - structure.getMapY()) * 2);

        this.addMouseHandler(this);

        this.setState(State.UNSELECTABLE);

        details = new TextObject(250, 250);

        updateText();

        if (terrainData.has("passable")) {
            this.passable = terrainData.getBoolean("passable");
        }

        if (propData.has("rotation")) {
            float r = propData.getInteger("rotation");

            this.setRotation(r + getRotation());
        }

        if (propData.has("decal")) {
            GameData decalData = propData.getData("decal");

            DecalFactory f = new DecalFactory(scene);

            for (GameData restriction : decalData.getList("restriction")) {
                f.addTypeRestriction(restriction.asString());
            }

            Decal decal = f.getInstance();
            if (decalData.has("rotation")) {
                float r = decalData.getInteger("rotation");

                decal.setRotation(r);
            }

            setDecal(decal);
        }
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

        Map map = getScene().getTerrainMap();

        Optional<Terrain> o = map.get(x + 1, y);
        if (o.isPresent()) {
            Terrain n = o.get();

            if (n.passable && n.pawn == null) {
                neighbors.add(new PathAdjacentNode<>(n, 1));
            }
        }

        o = map.get(x, y + 1);
        if (o.isPresent()) {
            Terrain n = o.get();

            if (n.passable && n.pawn == null) {
                neighbors.add(new PathAdjacentNode<>(n, 1));
            }
        }

        o = map.get(x, y - 1);
        if (o.isPresent()) {
            Terrain n = o.get();

            if (n.passable && n.pawn == null) {
                neighbors.add(new PathAdjacentNode<>(n, 1));
            }
        }

        o = map.get(x - 1, y);
        if (o.isPresent()) {
            Terrain n = o.get();

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
            case SELECT_MOVE_AI:
                setHighlight(BattleScene.OFF);
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
            text += "\n" + pawn.getName();
            text += "\nHP: " + pawn.getHitPoints();
            text += "\nMovement: " + pawn.getTotalMovement();

            text += "\nWeapon:";
            text += "\nName: " + pawn.getWeapon().getName() +
                    "\nDamage: " + pawn.getWeapon().getDamage() +
                    ", Range: " + pawn.getWeapon().getRange() +
                    "\nPiercing: " + pawn.getWeapon().getPiercing() +
                    ", Accuracy: " + pawn.getWeapon().getAccuracy();

            text += "\nArmor:";
            text += "\n" + pawn.getArmor().getName() + ", " + pawn.getArmor().getResistance();
        }

        details.setText(text);
        details.updateTexture();
    }

    public boolean isPassable() {
        return passable;
    }
}
