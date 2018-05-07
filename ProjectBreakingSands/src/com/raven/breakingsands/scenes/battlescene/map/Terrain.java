package com.raven.breakingsands.scenes.battlescene.map;

import com.raven.breakingsands.ZLayer;
import com.raven.breakingsands.scenes.battlescene.BattleScene;
import com.raven.breakingsands.scenes.battlescene.decal.Wall;
import com.raven.breakingsands.scenes.battlescene.decal.WallFactory;
import com.raven.breakingsands.scenes.battlescene.pawn.Pawn;
import com.raven.engine2d.GameEngine;
import com.raven.engine2d.database.GameData;
import com.raven.engine2d.database.GameDataList;
import com.raven.engine2d.database.GameDatabase;
import com.raven.engine2d.graphics2d.sprite.SpriteSheet;
import com.raven.engine2d.util.pathfinding.PathAdjacentNode;
import com.raven.engine2d.util.pathfinding.PathNode;
import com.raven.engine2d.worldobject.MouseHandler;
import com.raven.engine2d.worldobject.WorldObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Terrain extends WorldObject<BattleScene, Structure, WorldObject>
        implements MouseHandler, PathNode<Terrain> {

    private static GameDataList dataList = GameDatabase.all("terrain");

    public static List<SpriteSheet> getSpriteSheets() {
        List<SpriteSheet> data = new ArrayList<>();

        for (GameData gameData : dataList) {
            data.add(GameEngine.getEngine().getSpriteSheet(gameData.getString("sprite")));
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

    private Wall wall;
    private Pawn pawn;

    public Terrain(BattleScene scene, Structure structure, GameData terrainData, GameData propData) {
        super(scene, terrainData);

        x = propData.getInteger("x") + structure.getMapX();
        y = propData.getInteger("y") + structure.getMapY();


        setX(propData.getInteger("x"));
        setY(propData.getInteger("y"));

        this.addMouseHandler(this);

        this.setState(State.UNSELECTABLE);

        updateText();

        if (terrainData.has("passable")) {
            this.passable = terrainData.getBoolean("passable");
        }

        if (propData.has("wall")) {
            GameData wallData = propData.getData("wall");

            WallFactory f = new WallFactory(scene);

            for (GameData tag : wallData.asList()) {
                f.addTypeRestriction(tag.asString());
            }

            Wall wall = f.getInstance();
            setWall(wall);
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
                            getScene().setTargetPawn(getPawn());
                            getScene().setState(BattleScene.State.ATTACKING);
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

            // TODO
//            getScene().setDetailText(details);
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

    public void setWall(Wall wall) {
        if (this.wall != null) {
            removeChild(this.wall);
        }

        this.wall = wall;

        this.passable = wall.isPassable();

        this.addChild(wall);

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
                } else if (this.isMouseHovering()) {
                    getScene().selectPath(this);
                }
                break;
            case ATTACKABLE:
                if (this.isMouseHovering()) {
                    setState(State.ATTACK);
                }
                break;
        }

        selectHighlight();
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
            case ATTACKING:
                setHighlight(BattleScene.OFF);
                break;
        }
    }

    public void updateText() {
        String text = "" + x + ", " + y + "\n" + cover + "\n";

        if (wall != null) {
            text += "Terrain:\n" + wall.getName();
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

        // TODO
//        details.setText(text);
//        details.updateTexture();
    }

    public boolean isPassable() {
        return passable;
    }

    @Override
    public float getZ() {
        return ZLayer.TERRAIN.getValue();
    }
}
