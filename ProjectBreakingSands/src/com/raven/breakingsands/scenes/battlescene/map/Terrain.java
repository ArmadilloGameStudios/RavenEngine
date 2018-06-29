package com.raven.breakingsands.scenes.battlescene.map;

import com.raven.breakingsands.ZLayer;
import com.raven.breakingsands.character.Ability;
import com.raven.breakingsands.character.RangeStyle;
import com.raven.breakingsands.character.Weapon;
import com.raven.breakingsands.scenes.battlescene.BattleScene;
import com.raven.breakingsands.scenes.battlescene.SelectionDetails;
import com.raven.breakingsands.scenes.battlescene.decal.Wall;
import com.raven.breakingsands.scenes.battlescene.decal.WallFactory;
import com.raven.breakingsands.scenes.battlescene.pawn.Pawn;
import com.raven.engine2d.Game;
import com.raven.engine2d.GameEngine;
import com.raven.engine2d.database.GameData;
import com.raven.engine2d.database.GameDataList;
import com.raven.engine2d.database.GameDatabase;
import com.raven.engine2d.database.GameDatable;
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
        implements MouseHandler, PathNode<Terrain>, GameDatable {

    private static GameDataList dataList = GameDatabase.all("terrain");

    public static List<SpriteSheet> getSpriteSheets() {
        List<SpriteSheet> data = new ArrayList<>();

        for (GameData gameData : dataList) {
            data.add(GameEngine.getEngine().getSpriteSheet(gameData.getString("sprite")));
        }

        return data;
    }

    public void setPawnIndex() {
        if (pawnIndex != null)
            setPawn(getScene().getPawns().get(pawnIndex));
    }

    public enum State {
        SELECTABLE,
        UNSELECTABLE,
        MOVEABLE,
        MOVE,
        ATTACKABLE,
        ATTACK,
        ABILITYABLE,
        ABILITY,
    }

    private State state;

    private int x, y;

    private boolean passable = true;
    private SelectionDetails details = new SelectionDetails();
    private Wall wall;
    private Integer pawnIndex;
    private Pawn pawn;
    private boolean spawn = false;
    private boolean start = false;

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

        propData.ifHas("tags", p -> p.asList().forEach(t -> {
            switch (t.asString()) {
                case "spawn":
                    spawn = true;
                    break;
                case "start":
                    start = true;
                    break;
            }
        }));
    }

    public Terrain(BattleScene scene, Structure structure, GameData gdTerrain) {
        super(scene, gdTerrain);

        gdTerrain.ifHas("passable", p -> passable = p.asBoolean());
        gdTerrain.ifHas("spawn", s -> spawn = s.asBoolean());
        gdTerrain.ifHas("start", s -> start = s.asBoolean());
        gdTerrain.ifHas("x", s -> x = s.asInteger());
        gdTerrain.ifHas("y", s -> y = s.asInteger());
        setX(x - structure.getMapX());
        setY(y - structure.getMapY());

        gdTerrain.ifHas("wall", w -> setWall(new Wall(scene, w)));

        gdTerrain.ifHas("pawn", p -> pawnIndex = p.asInteger());

        this.addMouseHandler(this);
    }

    @Override
    public GameData toGameData() {
        HashMap<String, GameData> map = new HashMap<>();

        map.put("passable", new GameData(passable));
        if (wall != null)
            map.put("wall", wall.toGameData());
        if (pawn != null)
            map.put("pawn", new GameData(getScene().getPawns().indexOf(pawn)));
        map.put("spawn", new GameData(spawn));
        map.put("start", new GameData(start));
        map.put("x", new GameData(x));
        map.put("y", new GameData(y));

        GameData woData = getWorldObjectData();
        for (String key : woData.asMap().keySet()) {
            map.put(key, woData.getData(key));
        }

        return new GameData(map);
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
                case SELECT_ABILITY:
                case SELECT_DEFAULT:
                    switch (state) {
                        case SELECTABLE:
                            if (pawn != null) {
                                if (pawn == getScene().getActivePawn()) {

                                } else if (pawn.getTeam() == getScene().getActiveTeam()) {
//                                    if (getScene().getActivePawn() != null)
//                                        getScene().getActivePawn().setReadyIsMoved(false);
                                    getScene().setActivePawn(pawn);
                                }
                            }
                            break;
                        case MOVE:
                            getScene().pawnMove();
                            break;
                        case ATTACK:
                            getScene().pawnAttack(getPawn());
                            break;
                        case ABILITY:
                            getScene().pawnAbility(getPawn());
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
                case SELECT_ABILITY:
                case SELECT_DEFAULT:
                    switch (state) {
                        case MOVEABLE:
                            getScene().selectPath(this);
                            break;
                        case ATTACKABLE:
                            if (pawn != null && pawn.getTeam() != getScene().getActivePawn().getTeam())
                                setState(State.ATTACK);
                            break;
                        case ABILITYABLE:
                            if (pawn != null && pawn.getTeam() != getScene().getActivePawn().getTeam())
                                setState(State.ABILITY);
                            break;
                    }
                    break;
            }

            selectHighlight();


            getScene().setSelectedDetailText(getDetails());
        }
    }

    @Override
    public void handleMouseLeave() {
        if (!getScene().isPaused()) {
            switch (getScene().getState()) {
                case SELECT_MOVE:
                case SELECT_ATTACK:
                case SELECT_ABILITY:
                case SELECT_DEFAULT:
                    getScene().clearPath();

                    if (state == State.ATTACK) {
                        setState(State.ATTACKABLE);
                    }
                    if (state == State.ABILITY) {
                        setState(State.ABILITYABLE);
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
            case SELECT_ABILITY:
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
        this.wall.setHighlight(BattleScene.OFF);

        this.addChild(wall);

        updateText();
    }

    public void setPawn(Pawn pawn) {
        if (this.pawn != null) {
            removeChild(this.pawn);
        }

        if (pawn.getParent() != null && pawn.getParent().getPawn() == pawn)
            pawn.getParent().removePawn();

        this.pawn = pawn;

        this.addChild(pawn);

        // check if it has an ability
        pawn.getAbilities().stream()
                .filter(a -> a.type == Ability.Type.AURORA)
                .forEach(a -> {
                    List<Terrain> inRange = selectRange(a.style, a.size);
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
        List<Terrain> inRange = selectRange(a.style, a.size);
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
            case ABILITYABLE:
                if (this.isMouseHovering()) {
                    setState(State.ABILITY);
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
            case SELECT_ABILITY:
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
                        case ABILITYABLE:
                            setHighlight(BattleScene.PURPLE);
                            break;
                        case ABILITY:
                            setHighlight(BattleScene.PURPLE_CHANGING);
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

    public boolean isStart() {
        return start;
    }

    public boolean isSpawn() {
        return spawn;
    }

    public void updateText() {

        if (pawn != null) {
            details.name = pawn.getName();
            if (pawn.getTeam(false) == 0)
                details.level = Integer.toString(pawn.getLevel()) + "," + pawn.getXp() + "/" + pawn.getNextLevelXp();
            else
                details.level = "";
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
        return passable && getPawn() == null;
    }

    public List<Terrain> selectRange(RangeStyle style, int rangeMax) {
        return selectRange(style, 1, rangeMax);
    }

    public List<Terrain> selectRange(RangeStyle style, int rangeMin, int rangeMax) {
        switch (style) {
            case SQUARE:
                return selectRangeSquare(rangeMin, rangeMax);
            case STRAIGHT:
                return selectRangeStraight(rangeMin, rangeMax);
            case DIAMOND:
            default:
                return selectRangeDiamond(rangeMin, rangeMax);
        }
    }

    private List<Terrain> selectRangeDiamond(int rangeMin, int rangeMax) {
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
                    o.ifPresent(withinRange::add);
                }
            }
        }

        return withinRange;
    }

    private List<Terrain> selectRangeSquare(int rangeMin, int rangeMax) {
        int x = this.getMapX();
        int y = this.getMapY();

        List<Terrain> withinRange = new ArrayList<>();

        for (int i = -rangeMax;
             i <= rangeMax;
             i++) {
            for (int j = -rangeMax;
                 j <= rangeMax;
                 j++) {

                if (Math.abs(i) + Math.abs(j) > rangeMin - 1) {
                    Optional<Terrain> o = getScene().getTerrainMap().get(x + i, y + j);
                    o.ifPresent(withinRange::add);
                }
            }
        }

        return withinRange;
    }

    private List<Terrain> selectRangeStraight(int rangeMin, int rangeMax) {
        int x = this.getMapX();
        int y = this.getMapY();

        List<Terrain> withinRange = new ArrayList<>();

        for (int i = -rangeMax;
             i <= rangeMax;
             i++) {
            if (Math.abs(i) + Math.abs(0) > rangeMin - 1) {
                Optional<Terrain> o = getScene().getTerrainMap().get(x + i, y);
                o.ifPresent(withinRange::add);
            }
        }

        for (int j = -rangeMax;
             j <= rangeMax;
             j++) {

            if (Math.abs(0) + Math.abs(j) > rangeMin - 1) {
                Optional<Terrain> o = getScene().getTerrainMap().get(x, y + j);
                o.ifPresent(withinRange::add);
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
                    if (!((x == startX && y == startY) || (x == endX && y == endY))) {

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
