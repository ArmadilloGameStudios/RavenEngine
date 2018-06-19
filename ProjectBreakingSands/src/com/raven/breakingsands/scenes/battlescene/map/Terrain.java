package com.raven.breakingsands.scenes.battlescene.map;

import com.raven.breakingsands.ZLayer;
import com.raven.breakingsands.character.Ability;
import com.raven.breakingsands.scenes.battlescene.BattleScene;
import com.raven.breakingsands.scenes.battlescene.SelectionDetails;
import com.raven.breakingsands.scenes.battlescene.decal.Wall;
import com.raven.breakingsands.scenes.battlescene.decal.WallFactory;
import com.raven.breakingsands.scenes.battlescene.pawn.Pawn;
import com.raven.engine2d.GameEngine;
import com.raven.engine2d.database.GameData;
import com.raven.engine2d.database.GameDataList;
import com.raven.engine2d.database.GameDatabase;
import com.raven.engine2d.graphics2d.sprite.SpriteSheet;
import com.raven.engine2d.util.Range;
import com.raven.engine2d.util.pathfinding.PathAdjacentNode;
import com.raven.engine2d.util.pathfinding.PathNode;
import com.raven.engine2d.worldobject.MouseHandler;
import com.raven.engine2d.worldobject.WorldObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.DoubleStream;

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

    public enum State {
        SELECTABLE,
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
    private SelectionDetails details = new SelectionDetails();
    private Wall wall;
    private Pawn pawn;

    private List<Ability> abilities = new ArrayList<>();

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
        if (!getScene().isPaused())
            switch (getScene().getState()) {
                case SELECT_MOVE:
                case SELECT_ATTACK:
                case SELECT_DEFAULT:
                    switch (state) {
                        case SELECTABLE:
                            if (pawn != null) {
                                if (pawn == getScene().getActivePawn()) {

                                } else if (pawn.getTeam() == getScene().getActiveTeam()) {
                                    if (getScene().getActivePawn() != null)
                                        getScene().getActivePawn().setReadyIsMoved(false);
                                    getScene().setActivePawn(pawn);
                                }
                            }
                            break;
                        case MOVE:
                            getScene().pawnMove();
                            break;
                        case ATTACK:
                            getScene().setTargetPawn(getPawn());
                            getScene().setState(BattleScene.State.ATTACKING);
                            break;
                        default:
                            getScene().setSelectedDetailText(getDetails());
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
                case SELECT_ATTACK:
                case SELECT_DEFAULT:
                    switch (state) {
                        case MOVEABLE:
                            getScene().selectPath(this);
                            break;
                        case ATTACKABLE:
                            if (pawn != null && pawn.getTeam() != getScene().getActivePawn().getTeam())
                                setState(State.ATTACK);
                            break;
                    }
                    break;
            }

            selectHighlight();
        }
    }

    @Override
    public void handleMouseLeave() {
        if (!getScene().isPaused()) {
            switch (getScene().getState()) {
                case SELECT_MOVE:
                case SELECT_ATTACK:
                case SELECT_DEFAULT:
                    getScene().clearPath();

                    if (state == State.ATTACK) {
                        setState(State.ATTACKABLE);
                    }
                    break;
            }


            selectHighlight();
        }
    }

    @Override
    public void handleMouseHover(float delta) {

    }

    @Override
    public List<PathAdjacentNode<Terrain>> getAdjacentNodes() {
        switch (getScene().getState()) {
            case SELECT_DEFAULT:
            case SELECT_ATTACK:
            case SELECT_MOVE:
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

        // check if it has an ability
        pawn.getAbilities().stream()
                .filter(a -> a.type == Ability.Type.AURORA)
                .forEach(a -> {
                    List<Terrain> inRange = selectRange(a.size);
                    HashMap<Terrain, Float> rangeMap = filterCoverRange(inRange);

                    for (Terrain n : rangeMap.keySet()) {
                        n.addAbility(a);
                    }
                });

        abilities.forEach(pawn::addAbilityAffect);

        updateText();
    }


    public Pawn getPawn() {
        return pawn;
    }

    public void removePawn() {
        if (pawn != null) {
            pawn.getAbilities().stream()
                    .filter(a -> a.type == Ability.Type.AURORA)
                    .forEach(this::removePawnAbility);

            abilities.forEach(a -> pawn.removeAbilityAffect(a));
        }

        this.removeChild(this.pawn);
        this.pawn = null;

        updateText();
    }

    public void removePawnAbility(Ability a) {
        List<Terrain> inRange = selectRange(a.size);
        HashMap<Terrain, Float> rangeMap = filterCoverRange(inRange);

        for (Terrain n : rangeMap.keySet()) {
            n.removeAbility(a);
        }
    }

    private void addAbility(Ability a) {
        abilities.add(a);

        if (pawn != null) {
            pawn.addAbilityAffect(a);
        }
    }

    private void removeAbility(Ability a) {
        if (pawn != null) {
            pawn.removeAbilityAffect(a);
        }

        abilities.remove(a);
    }

    public List<Ability> getAbilities() {
        return abilities;
    }


    public void setState(State state) {
        this.state = state;

        switch (state) {
            case UNSELECTABLE:
                break;
            case SELECTABLE:
                break;
            case MOVEABLE:
                if (this.pawn == getScene().getActivePawn()) {
                    this.state = State.SELECTABLE;
                } else if (this.isMouseHovering()) {
                    getScene().selectPath(this);
                }
                break;
            case MOVE:
                if (this.pawn == getScene().getActivePawn()) {
                    this.state = State.SELECTABLE;
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
            case SELECT_ATTACK:
            case SELECT_DEFAULT:
                if (getScene().getActiveTeam() == 0)
                    switch (state) {
                        case SELECTABLE:
                            if (pawn != null && pawn == getScene().getActivePawn()) {

                                if (isMouseHovering())
                                    setHighlight(BattleScene.YELLOW_CHANGING);
                                else
                                    setHighlight(BattleScene.YELLOW);
                            } else {
                                if (isMouseHovering())
                                    setHighlight(BattleScene.GREEN_CHANGING);
                                else
                                    setHighlight(BattleScene.GREEN);
                            }
                            break;
                        case UNSELECTABLE:
                            setHighlight(BattleScene.OFF);
                            break;
                        case MOVEABLE:
                            if (pawn != null) {
                                setHighlight(BattleScene.GREEN);
                            } else {
                                if (passable) {
                                    setHighlight(BattleScene.BLUE);
                                } else
                                    setHighlight(BattleScene.YELLOW);
                            }
                            break;
                        case MOVE:
                            if (pawn != null) {
                                setHighlight(BattleScene.GREEN);
                            } else {
                                if (passable) {
                                    setHighlight(BattleScene.BLUE_CHANGING);
                                } else
                                    setHighlight(BattleScene.YELLOW_CHANGING);
                            }
                            break;
                        case ATTACKABLE:
                            setHighlight(BattleScene.RED);
                            break;
                        case ATTACK:
                            setHighlight(BattleScene.RED_CHANGING);
                            break;
                    }
                else {
                    setHighlight(BattleScene.OFF);
                }
                break;
            case ATTACKING:
                setHighlight(BattleScene.OFF);
                break;
        }
    }

    public void updateText() {

        if (pawn != null) {
            details.name = pawn.getName();
            details.hp = pawn.getRemainingHitPoints() + "/" + pawn.getHitPoints();
            if (pawn.getBonusHp() > 0) {
                details.hp += "+" + pawn.getBonusHp();
            }

            details.shield = pawn.getRemainingShield() + "/" + pawn.getTotalShield();
            if (pawn.getBonusShield() > 0) {
                details.shield += "+" + pawn.getBonusShield();
            }

            if (pawn == getScene().getActivePawn())
                details.movement = pawn.getRemainingMovement() + "/" + pawn.getTotalMovement();
            else
                details.movement = Integer.toString(pawn.getTotalMovement());

            details.resistance = Integer.toString(pawn.getResistance());

            details.weapon = pawn.getWeapon().getName();
            details.damage = Integer.toString(pawn.getWeapon().getDamage());
            details.piercing = Integer.toString(pawn.getWeapon().getPiercing());
            if (pawn.getWeapon().getRange() != pawn.getWeapon().getRangeMin()) {
                details.range =
                        Integer.toString(pawn.getWeapon().getRangeMin()) +
                                "-" +
                                Integer.toString(pawn.getWeapon().getRange());
            } else {
                details.range = Integer.toString(pawn.getWeapon().getRange());
            }
            details.shots = Integer.toString(pawn.getWeapon().getShots());
        } else {
            details.clear();
            details.name = "floor";
        }
    }

    public SelectionDetails getDetails() {
        return details;
    }

    public boolean isPassable() {
        return passable;
    }

    public List<Terrain> selectRange(int rangeMax) {
        return selectRange(1, rangeMax);
    }

    public List<Terrain> selectRange(int rangeMin, int rangeMax) {
        int x = this.getMapX();
        int y = this.getMapY();

        List<Terrain> withinRange = new ArrayList<>();

        for (int i = -rangeMax;
             i <= rangeMax;
             i++) {

            int heightRange = rangeMax - Math.abs(i);

            for (int j = -heightRange;
                 j <= heightRange;
                 j++) {

                if (Math.abs(i) + Math.abs(j) > rangeMin - 1) {
                    Optional<Terrain> o = getScene().getTerrainMap().get(x + i, y + j);
//                if (o.isPresent() &&
//                        o.get().getPawn() != null &&
//                        o.get().getPawn().getTeam() != activePawn.getTeam()) {
                    o.ifPresent(withinRange::add);
                }
            }
        }

        return withinRange;
    }

    public HashMap<Terrain, Float> filterCoverRange(List<Terrain> inRange) {
        return filterCoverRange(inRange, .75f);
    }

    public HashMap<Terrain, Float> filterCoverRange(List<Terrain> inRange, float threshold) {
        HashMap<Terrain, Float> map = new HashMap<>();

        int startX = this.getMapX();
        int startY = this.getMapY();

        for (Terrain end : inRange) {
            int endX = end.getMapX();
            int endY = end.getMapY();

            float a = -(endY - startY);
            float b = endX - startX;
            float c = -(a * startX + b * startY);

            float leftCoverage = 0f, rightCoverage = 0f;

            for (int x : new Range(endX, startX)) {
                for (int y : new Range(endY, startY)) {

                    Optional<Terrain> o = getScene().getTerrainMap().get(x, y);
                    if (!o.isPresent() || !o.get().isPassable()) {

                        float cover = linePointDist(a, b, c, x, y);

                        if (cover >= 0f) {
                            cover = Math.max(1f - cover, 0f);
                            leftCoverage = Math.max(leftCoverage, Math.min(cover, 1f));
                        } else if (cover < 0f) {
                            cover = -cover;
                            cover = Math.max(1f - cover, 0f);
                            rightCoverage = Math.max(rightCoverage, Math.min(cover, 1f));
                        }
                    }
                }
            }

            float coverage = Math.min(leftCoverage + rightCoverage, 1f);

            if (coverage <= threshold) {
                getScene().getTerrainMap().get(endX, endY).ifPresent(terrain -> map.put(terrain, coverage));
            }
        }

        return map;
    }

    private float linePointDist(float a, float b, float c, float x, float y) {
        return ((a * x + b * y + c) / (Math.abs(a) + Math.abs(b)));
    }

    @Override
    public float getZ() {
        return ZLayer.TERRAIN.getValue();
    }
}
