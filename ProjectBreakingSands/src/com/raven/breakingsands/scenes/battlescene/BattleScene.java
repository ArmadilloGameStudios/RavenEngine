package com.raven.breakingsands.scenes.battlescene;

import com.raven.breakingsands.BreakingSandsGame;
import com.raven.breakingsands.character.Character;
import com.raven.breakingsands.scenes.battlescene.ai.AI;
import com.raven.breakingsands.scenes.battlescene.decal.Decal;
import com.raven.breakingsands.scenes.battlescene.map.Map;
import com.raven.breakingsands.scenes.battlescene.map.Terrain;
import com.raven.breakingsands.scenes.battlescene.menu.Menu;
import com.raven.breakingsands.scenes.hud.UIBottomContainer;
import com.raven.breakingsands.scenes.battlescene.pawn.Pawn;
import com.raven.breakingsands.scenes.battlescene.pawn.PawnFactory;
import com.raven.engine2d.GameEngine;
import com.raven.engine2d.graphics2d.sprite.SpriteSheet;
import com.raven.engine2d.scene.Camera;
import com.raven.engine2d.scene.Scene;
import com.raven.engine2d.util.Range;
import com.raven.engine2d.util.math.Vector2f;
import com.raven.engine2d.util.math.Vector3f;
import com.raven.engine2d.util.pathfinding.Path;
import com.raven.engine2d.util.pathfinding.PathAdjacentNode;
import com.raven.engine2d.util.pathfinding.PathFinder;
import com.raven.engine2d.worldobject.Highlight;
import org.lwjgl.glfw.GLFW;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

import static com.raven.breakingsands.scenes.battlescene.BattleScene.State.SELECT_MOVE;

public class BattleScene extends Scene<BreakingSandsGame> {
    public static Highlight
            OFF = new Highlight(),
            BLUE = new Highlight(.2f, .6f, 1f, .75f),
            BLUE_CHANGING = new Highlight(.2f, .6f, 1f, .5f),
            RED = new Highlight(1f, .1f, .05f, .75f),
            RED_CHANGING = new Highlight(1f, .3f, .2f, .5f),
            YELLOW = new Highlight(1f, .8f, .2f, .75f),
            YELLOW_CHANGING = new Highlight(1f, .8f, .2f, .5f),
            GREEN = new Highlight(.3f, 1f, .2f, .75f),
            GREEN_CHANGING = new Highlight(.3f, 1f, .2f, .5f);

    private Menu menu;
    private UIDetailText hudDetailText;

    public enum State {
        MOVING, ATTACKING, SELECT_MOVE_AI, SELECT_MOVE,
    }

    private Random random = new Random();

    private Map map;

    private HashMap<Terrain, Path<Terrain>> pathMap;
    private Path<Terrain> currentPath;
    private int pathIndex = 0;
    private float pathSpeed = 100f;
    private float pathMoveTime = 0f;

    private HashMap<Terrain, Float> rangeMap;

    private List<Pawn> pawns = new ArrayList<>();
    private Pawn activePawn;

    private List<Character> canLevelUp = new ArrayList<>();

    private State state = SELECT_MOVE;

    private ExecutorService aiExecutorService = Executors.newSingleThreadExecutor();
    private AI ai = new AI(this);
    private Future aiFuture;

    public BattleScene(BreakingSandsGame game) {
        super(game);
    }

    @Override
    public List<SpriteSheet> getSpriteSheets() {
        List<SpriteSheet> models = new ArrayList<>();

        // TODO
        models.addAll(Terrain.getSpriteSheets());
        models.addAll(Decal.getSpriteSheets());
        models.addAll(Pawn.getSpriteSheets());

        return models;
    }

    @Override
    public void inputKey(int key, int action, int mods) {
        if (GLFW.GLFW_KEY_ESCAPE == key && GLFW.GLFW_PRESS == action) {
            if (isPaused()) {
                menu.setVisibility(false);
                setPaused(false);
            } else {
                menu.setVisibility(true);
                setPaused(true);
            }
        }
    }

    private Vector2f tempVec = new Vector2f();

    @Override
    public void onEnterScene() {
        setBackgroundColor(new Vector3f(0f, 0f, 0f));

        // Terrain
        map = new Map(this);
        getLayerTerrain().addChild(map);

        addPawns();

        // Bottom UI
        UIBottomContainer<BattleScene> bottomContainer = new UIBottomContainer<>(this);
        getLayerUI().addChild(bottomContainer);

        hudDetailText = new UIDetailText(this);

        bottomContainer.addChild(hudDetailText);
        bottomContainer.pack();

        // TODO
//        setDetailText(activePawn.getParent().getDetailText());

        // Menu
        menu = new Menu(this);
//        menu.pack();
        getLayerUI().addChild(menu);
        menu.setVisibility(false);

//        victory();
    }

    private void addPawns() {
        List<Terrain> terrainList = map.getTerrainList();

        // characters
        for (Character character : getGame().getCharacters()) {

            Pawn p = new Pawn(this, character);
            pawns.add(p);

            Optional<Terrain> o = terrainList.stream().filter(t -> t.getPawn() == null && t.isPassable()).findAny();

            map.setPawn(o.get(), p);
        }

        activePawn = pawns.get(0);

        setState(SELECT_MOVE);
    }

    public List<Pawn> getPawns() {
        return pawns;
    }

    public void spawnPawn(String name) {
        PawnFactory pf = new PawnFactory(this);
        pf.setName(name);
        Pawn p = pf.getInstance();
        pawns.add(p);

        List<Terrain> terrainList = map.getTerrainList();
        List<Terrain> validTerrainList = terrainList.stream().filter(t -> t.getPawn() == null && t.isPassable()).collect(Collectors.toList());

        int r = random.nextInt(validTerrainList.size());

        map.setPawn(validTerrainList.get(r), p);
    }

    @Override
    public void onExitScene() {
        getGame().saveGame();
    }

    private Vector2f tempVec2 = new Vector2f();

    @Override
    public void onUpdate(float deltaTime) {
        float a = (float) (Math.cos(GameEngine.getEngine().getSystemTime() * .004) * .075 + .4);

        BLUE_CHANGING.a = RED_CHANGING.a = GREEN_CHANGING.a = YELLOW_CHANGING.a = a;

        switch (state) {
            case MOVING:
                movePawn(deltaTime);
                break;
            case SELECT_MOVE_AI:
                if (aiFuture.isDone()) {
                    ai.resolve();
                }
                break;
        }
    }

    private void movePawn(float delta) {

        Terrain current = currentPath.get(pathIndex).getNode();

        if (pathIndex + 1 < currentPath.size()) {
            PathAdjacentNode<Terrain> next = currentPath.get(pathIndex + 1);
            float cost = next.getCost();

            pathMoveTime += delta;

            float overflow = 0f;

            if (pathMoveTime > pathSpeed * cost) {
                overflow = pathMoveTime - pathSpeed * cost;
                delta -= overflow;

                pathMoveTime = 0f;
            }


            Vector2f movement = next.getNode().getWorldPosition().subtract(current.getWorldPosition(), tempVec);

            activePawn.move(movement.scale(delta / (pathSpeed * cost), tempVec2));

            if (overflow > 0f) {
                pathIndex += 1;
                movePawn(overflow);
            }
        } else {
            pathIndex = 0;
            pathMoveTime = 0f;

            activePawn.setPosition(0, 0);
            current.setPawn(activePawn);

            if (activePawn.getRemainingMovement() > 0 ||
                    activePawn.getRemainingAttacks() > 0) {
                if (activePawn.getTeam() == 0)
                    setState(State.SELECT_MOVE);
                else
                    setState(State.SELECT_MOVE_AI);
            } else
                selectNextPawn();
        }
    }

    public void removePawn(Pawn pawn) {
        pawns.remove(pawn);
    }

    public Pawn getNextPawn() {
        int i = pawns.indexOf(activePawn) + 1;

        if (i == pawns.size()) {
            i = 0;
        }

        return pawns.get(i);
    }

    public Map getTerrainMap() {
        return map;
    }

    public void selectNextPawn() {
        setActivePawn(getNextPawn());
    }

    public void setActivePawn(Pawn pawn) {
        this.activePawn = pawn;

        activePawn.ready();

        Vector2f pos = pawn.getWorldPosition();
        // TODO focus on pawn

        switch (state) {
            case MOVING:
                if (activePawn.getTeam() == 0) {
                    setState(State.SELECT_MOVE);
                } else {
                    setState(State.SELECT_MOVE_AI);
                }
                break;
            case SELECT_MOVE:
                if (activePawn.getTeam() == 0) {
                    setState(SELECT_MOVE);
                } else {
                    setState(State.SELECT_MOVE_AI);
                }
                break;
            case SELECT_MOVE_AI:
                if (activePawn.getTeam() == 0) {
                    setState(SELECT_MOVE);
                } else {
                    setState(State.SELECT_MOVE_AI);
                }
                break;
            case ATTACKING:
                if (activePawn.getTeam() == 0) {
                    setState(SELECT_MOVE);
                } else {
                    setState(State.SELECT_MOVE_AI);
                }
                break;
        }
    }

    public Pawn getActivePawn() {
        return activePawn;
    }

    private boolean doSpawn() {
        int a = 10 - (int) pawns.stream().filter(p -> p.getTeam() != 0).count();
        int b = random.nextInt(10);

        return a > b;
    }

    public void setState(State state) {
        this.state = state;
        System.out.println("State: " + state);

        switch (state) {
            case SELECT_MOVE:
                if (doSpawn()) {
                    spawnPawn("Service Drone");
                }

                setStateSelectMove();
                break;
            case SELECT_MOVE_AI:
                currentPath = null;
                map.setState(Terrain.State.UNSELECTABLE);

                aiFuture = aiExecutorService.submit(ai);
                break;
            case MOVING:
                activePawn.move(currentPath.getCost());

                map.setState(Terrain.State.UNSELECTABLE);
                break;
            case ATTACKING:
                break;
        }
    }

    private void setStateSelectMove() {

        // clear

        map.setState(Terrain.State.UNSELECTABLE);
        currentPath = null;
        boolean noOptions = false;

        // find movement
        Terrain parentTerrain = activePawn.getParent();

        PathFinder<Terrain> pf = new PathFinder<>();

        pathMap = pf.findDistance(parentTerrain, activePawn.getRemainingMovement());

        if (pathMap.size() > 0) {
            for (Terrain t : pathMap.keySet()) {
                t.setState(Terrain.State.MOVEABLE);
            }
        } else {
            noOptions = true;
//            selectNextPawn();
        }

        // find attack
        List<Terrain> inRange = selectRange(activePawn.getWeapon().getRange(), parentTerrain);

        rangeMap = filterRange(parentTerrain, inRange);

        if (rangeMap.size() > 0) {
            for (Terrain n : rangeMap.keySet()) {
                if (n.getPawn() != null && n.getPawn().getTeam() != activePawn.getTeam()) {

                    n.cover = rangeMap.get(n);
                    n.setState(Terrain.State.ATTACKABLE);
                    n.updateText();

                    noOptions = false;
                }
            }
        }

        if (noOptions) {
            selectNextPawn();
        }
    }

    public State getState() {
        return state;
    }

    public Random getRandom() {
        return random;
    }

    public void selectPath(Terrain t) {
        currentPath = pathMap.get(t);

        if (currentPath != null) {
            for (PathAdjacentNode<Terrain> p : currentPath) {
                Terrain node = p.getNode();

                node.setState(Terrain.State.MOVE);
            }
        }
    }

    public void clearPath() {
        if (currentPath != null) {
            for (PathAdjacentNode<Terrain> p : currentPath) {
                Terrain node = p.getNode();

                node.setState(Terrain.State.MOVEABLE);
            }
        }
    }

    public void clearAllPaths() {
        for (Terrain t : pathMap.keySet()) {
            t.setState(Terrain.State.UNSELECTABLE);
        }

        pathMap.clear();
    }

    public void setCurrentPath(Path<Terrain> currentPath) {
        this.currentPath = currentPath;
    }

    public List<Terrain> selectRange(int range, Terrain start) {
        int x = start.getMapX();
        int y = start.getMapY();

        List<Terrain> withinRange = new ArrayList<>();

        for (int i = -range;
             i <= range;
             i++) {

            int heightRange = range - Math.abs(i);

            for (int j = -heightRange;
                 j <= heightRange;
                 j++) {

                Optional<Terrain> o = map.get(x + i, y + j);
                if (o.isPresent() &&
                        o.get().getPawn() != null &&
                        o.get().getPawn().getTeam() != activePawn.getTeam()) {
                    withinRange.add(o.get());
                }
            }
        }

        return withinRange;
    }

    public HashMap<Terrain, Float> filterRange(Terrain start, List<Terrain> inRange) {
        HashMap<Terrain, Float> map = new HashMap<>();

        int startX = start.getMapX();
        int startY = start.getMapY();

        for (Terrain end : inRange) {
            int endX = end.getMapX();
            int endY = end.getMapY();

            float a = -(endY - startY);
            float b = endX - startX;
            float c = -(a * startX + b * startY);

            float leftCoverage = 0f, rightCoverage = 0f;

            for (int x : new Range(endX, startX)) {
                for (int y : new Range(endY, startY)) {

                    Optional<Terrain> o = this.map.get(x, y);
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

            if (coverage <= .75f) {
                this.map.get(endX, endY).ifPresent(terrain -> map.put(terrain, coverage));
            }
        }

        return map;
    }

    private float linePointDist(float a, float b, float c, float x, float y) {
        return ((a * x + b * y + c) / (Math.abs(a) + Math.abs(b)));
    }

    public void checkVictory() {
        for (Pawn pawn : pawns) {
            if (pawn.getTeam() != 0)
                return;
        }

        victory();
    }

    public void victory() {
        BreakingSandsGame game = getGame();
        canLevelUp.clear();

        for (Character c : game.getCharacters()) {
            c.addExperience(1);

            if (c.canLevelUp()) {
                c.increaseLevel();
                canLevelUp.add(c);
            }
        }

        setPaused(true);
    }

    public List<Character> getCharacterToLevel() {
        return canLevelUp;
    }

    public void setDetailText() {
        // TODO
    }
}
