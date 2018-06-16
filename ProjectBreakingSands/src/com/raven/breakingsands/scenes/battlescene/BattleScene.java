package com.raven.breakingsands.scenes.battlescene;

import com.raven.breakingsands.BreakingSandsGame;
import com.raven.breakingsands.character.Character;
import com.raven.breakingsands.character.Effect;
import com.raven.breakingsands.character.Weapon;
import com.raven.breakingsands.scenes.battlescene.ai.AI;
import com.raven.breakingsands.scenes.battlescene.decal.Wall;
import com.raven.breakingsands.scenes.battlescene.map.Map;
import com.raven.breakingsands.scenes.battlescene.map.Terrain;
import com.raven.breakingsands.scenes.battlescene.menu.Menu;
import com.raven.breakingsands.scenes.battlescene.pawn.Pawn;
import com.raven.breakingsands.scenes.battlescene.pawn.PawnDamage;
import com.raven.breakingsands.scenes.battlescene.pawn.PawnFactory;
import com.raven.breakingsands.scenes.hud.UIBottomLeftContainer;
import com.raven.breakingsands.scenes.hud.UIBottomRightContainer;
import com.raven.engine2d.GameEngine;
import com.raven.engine2d.GameProperties;
import com.raven.engine2d.database.GameData;
import com.raven.engine2d.database.GameDatabase;
import com.raven.engine2d.graphics2d.sprite.SpriteAnimationState;
import com.raven.engine2d.graphics2d.sprite.SpriteSheet;
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

import static com.raven.breakingsands.scenes.battlescene.BattleScene.State.SELECT_DEFAULT;

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
    private UIActionSelect actionSelect;
    private UIDetailText uiActiveDetailText;
    private UIDetailText uiSelectedDetailText;

    public enum State {
        MOVING, ATTACKING, SELECT_DEFAULT, SELECT_MOVE, SELECT_ATTACK
    }

    private Random random = new Random();

    private Map map;

    private HashMap<Terrain, Path<Terrain>> pathMap;
    private Path<Terrain> currentPath;
    private int pathIndex = 0;
    private float pathSpeed = 4f * 100f;
    private float pathMoveTime = 0f;

    private HashMap<Terrain, Float> rangeMap;

    private List<Pawn> pawns = new ArrayList<>();
    private Pawn activePawn;
    private Pawn targetPawn;

    private PawnDamage textDamage;
    private float damageShowTime;

    private List<Character> canLevelUp = new ArrayList<>();

    private State state = SELECT_DEFAULT;

    private ExecutorService aiExecutorService = Executors.newSingleThreadExecutor();
    private AI ai = new AI(this);
    private Future aiFuture;

    private int activeTeam = 0;

    public BattleScene(BreakingSandsGame game) {
        super(game);
    }

    @Override
    public List<SpriteSheet> getSpriteSheets() {
        List<SpriteSheet> models = new ArrayList<>();

        // TODO
        models.addAll(Terrain.getSpriteSheets());
        models.addAll(Wall.getSpriteSheets());
        models.addAll(Pawn.getSpriteSheets());
        models.addAll(Effect.getSpriteSheets());
        models.addAll(Weapon.getSpriteSheets());

        return models;
    }

    private boolean isDownKey = false;
    private boolean isUpKey = false;
    private boolean isRightKey = false;
    private boolean isLeftKey = false;

    @Override
    public void inputKey(int key, int action, int mods) {
        switch (key) {
            case GLFW.GLFW_KEY_ESCAPE:
                if (action == GLFW.GLFW_PRESS) {
                    if (isPaused()) {
                        menu.setVisibility(false);
                        setPaused(false);
                    } else {
                        menu.setVisibility(true);
                        setPaused(true);
                    }
                }
                break;
            case GLFW.GLFW_KEY_DOWN:
                if (action == GLFW.GLFW_PRESS) {
                    isDownKey = true;
                } else if (action == GLFW.GLFW_RELEASE) {
                    isDownKey = false;
                }
                break;
            case GLFW.GLFW_KEY_UP:
                if (action == GLFW.GLFW_PRESS) {
                    isUpKey = true;
                } else if (action == GLFW.GLFW_RELEASE) {
                    isUpKey = false;
                }
                break;
            case GLFW.GLFW_KEY_RIGHT:
                if (action == GLFW.GLFW_PRESS) {
                    isRightKey = true;
                } else if (action == GLFW.GLFW_RELEASE) {
                    isRightKey = false;
                }
                break;
            case GLFW.GLFW_KEY_LEFT:
                if (action == GLFW.GLFW_PRESS) {
                    isLeftKey = true;
                } else if (action == GLFW.GLFW_RELEASE) {
                    isLeftKey = false;
                }
                break;
        }

    }

    private Vector2f tempVec = new Vector2f();

    @Override
    public void onEnterScene() {
        setBackgroundColor(new Vector3f(0f, 0f, 0f));

        // Terrain
        map = new Map(this);
        getLayerTerrain().addChild(map);

        textDamage = new PawnDamage(this);
        getLayerDetails().addChild(textDamage);

        Vector2f wo = this.getWorldOffset();
        wo.x = GameProperties.getScreenWidth() / 2f;
        wo.y = GameProperties.getScreenHeight() / 2f;

        // Bottom UI
        UIBottomRightContainer<BattleScene> bottomRightContainer = new UIBottomRightContainer<>(this);
        getLayerUI().addChild(bottomRightContainer);
        uiActiveDetailText = new UIDetailText(this, bottomRightContainer.getStyle());
        bottomRightContainer.addChild(uiActiveDetailText);
        bottomRightContainer.pack();


        UIBottomLeftContainer<BattleScene> bottomLeftContainer = new UIBottomLeftContainer<>(this);
        getLayerUI().addChild(bottomLeftContainer);
        uiSelectedDetailText = new UIDetailText(this, bottomLeftContainer.getStyle());
        bottomLeftContainer.addChild(uiSelectedDetailText);
        bottomLeftContainer.pack();

        // Action Select
        actionSelect = new UIActionSelect(this);
        getLayerUI().addChild(actionSelect);

        // Menu
        menu = new Menu(this);
        menu.pack();
        getLayerUI().addChild(menu);
        menu.setVisibility(false);

//        victory();

        addPawns();

        setActiveTeam(0);
    }

    public void showDamage(Pawn pawn, String damage) {
        textDamage.setText(damage);
        Vector2f pos = pawn.getWorldPosition();
        pos.x -= .9;
        pos.y += 1.3;
        textDamage.setPosition(pos);

        damageShowTime = 0;
    }

    private void addPawns() {
        List<Terrain> terrainList = map.getTerrainList();

        // characters
//        for (Character character : getGame().getCharacters()) {
//
//            Pawn p = new Pawn(this, character);
//            pawns.add(p);
//
//            Optional<Terrain> o = terrainList.stream().filter(t -> t.getPawn() == null && t.isPassable()).findAny();
//
//            map.setPawn(o.get(), p);
//        }

        for (int i = 0; i < 4; i++) {
            GameData gdPawn = GameDatabase.all("pawn").stream()
                    .filter(p -> p.getString("name").equals("Recruit"))
                    .findFirst().get();

            Pawn p = new Pawn(this, gdPawn);
            pawns.add(p);

            Optional<Terrain> o = terrainList.stream().filter(t -> t.getPawn() == null && t.isPassable()).findAny();

            map.setPawn(o.get(), p);
        }

        setActivePawn(null);
        setState(SELECT_DEFAULT);
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

//        System.out.println(terrainList);
//        System.out.println(validTerrainList);

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
        float a = (float) (Math.cos(GameEngine.getEngine().getSystemTime() * .005) * .15 + .4);

        BLUE_CHANGING.a = RED_CHANGING.a = GREEN_CHANGING.a = YELLOW_CHANGING.a = a;

        switch (state) {
            case MOVING:
                movePawn(deltaTime);
                break;
            case SELECT_DEFAULT:
                if (activeTeam != 0)
                    if (aiFuture.isDone()) {
                        ai.resolve();
                    }
                break;
        }

        damageShowTime += deltaTime;
        if (damageShowTime < 750) {
            textDamage.setVisibility(true);
        } else {
            textDamage.setVisibility(false);
        }

        float smoothing = .5f;
        Vector2f worldOffset = getWorldOffset();

        if (isDownKey) {
            worldOffset.y += smoothing * deltaTime;
        }

        if (isUpKey) {
            worldOffset.y -= smoothing * deltaTime;
        }

        if (isRightKey) {
            worldOffset.x -= smoothing * deltaTime;
        }

        if (isLeftKey) {
            worldOffset.x += smoothing * deltaTime;
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

            activePawn.setFlip(movement.y > 0f || movement.x > 0f);

            SpriteAnimationState animationState = activePawn.getAnimationState();
            if (movement.y > 0f || movement.x < 0f) {
                animationState.setAction("walking up", false);
            } else {
                animationState.setAction("walking down", false);
            }

            if (overflow > 0f) {
                pathIndex += 1;
                movePawn(overflow);
            }
        } else {
            pathIndex = 0;
            pathMoveTime = 0f;

            activePawn.setPosition(0, 0);
            current.setPawn(activePawn);

            setState(State.SELECT_DEFAULT);
        }
    }

    public void removePawn(Pawn pawn) {
        pawns.remove(pawn);
    }

    public Map getTerrainMap() {
        return map;
    }

    public void setActivePawn(Pawn pawn) {
        this.activePawn = pawn;

//        activePawn.ready();

        if (activePawn != null) {
            activePawn.getParent().updateText();
            setActiveDetailText(activePawn.getParent().getDetails());

            Vector2f pos = pawn.getWorldPosition();
            // TODO focus on pawn
        }

        setState(SELECT_DEFAULT);
    }

    public Pawn getActivePawn() {
        return activePawn;
    }

    public int getActiveTeam() {
        return activeTeam;
    }

    public void setActiveTeam(int team) {
        this.activeTeam = team;

        pawns.stream()
                .filter(p -> p.getTeam() == activeTeam)
                .forEach(Pawn::ready);

        setState(SELECT_DEFAULT);
    }

    public void setTargetPawn(Pawn targetPawn) {
        this.targetPawn = targetPawn;
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
            case SELECT_DEFAULT:
                if (activeTeam == 0) {
                    // TODO change spawn
                    if (doSpawn()) {
                        spawnPawn("Service Drone");
                    }

                    // clean
                    map.setState(Terrain.State.UNSELECTABLE);
                    currentPath = null;

                    if (activePawn != null) {
                        actionSelect.setPawn(activePawn);
                        setStateSelectDefault();
                    } else {
                        actionSelect.setPawn(null);
                        setSelectablePawn();
                    }
                } else {
                    if (activePawn != null) {
                        activePawn.getAnimationState().setAction("idle");
                    }

                    currentPath = null;
                    map.setState(Terrain.State.UNSELECTABLE);

                    aiFuture = aiExecutorService.submit(ai);
                }
                break;
            case SELECT_MOVE:
                if (activeTeam == 0) {

                    // clean
                    map.setState(Terrain.State.UNSELECTABLE);
                    currentPath = null;

                    if (activePawn != null) {
                        setStateSelectMove();
                    }
                }
                break;
            case SELECT_ATTACK:
                if (activeTeam == 0) {

                    // clean
                    map.setState(Terrain.State.UNSELECTABLE);
                    currentPath = null;

                    if (activePawn != null) {
                        setStateSelectAttack();
                    }
                }
                break;
            case MOVING:
                actionSelect.setPawn(null);

                clearAllPaths();
                activePawn.getAnimationState().setAction("walking up");
                activePawn.move(currentPath.getCost());

                map.setState(Terrain.State.UNSELECTABLE);
                break;

            case ATTACKING:
                actionSelect.setPawn(null);
                setStateAttacking();
                break;
        }
    }

    private void setSelectablePawn() {

        if (pawns.stream().noneMatch(p -> p.getTeam() == 0 && p.isReady())) {
            if (activeTeam == 0) {
                setActiveTeam(1);
            } else {
                setActiveTeam(0);
            }
        } else {
            // selectable pawns
            for (Pawn pawn : pawns) {
                if (pawn.getTeam() == 0 && pawn.isReady()) {
                    pawn.getParent().setState(Terrain.State.SELECTABLE);
                }
            }
        }

    }

    public void setStateAttacking() {
        map.setState(Terrain.State.UNSELECTABLE);

        activePawn.runAttackAnimation(targetPawn, a -> {
            setState(State.SELECT_DEFAULT);
        });
    }

    private void setStateSelectDefault() {
        activePawn.getAnimationState().setAction("idle", false);

        // find movement
        Terrain parentTerrain = activePawn.getParent();

        PathFinder<Terrain> pf = new PathFinder<>();

        pathMap = pf.findDistance(parentTerrain, activePawn.getRemainingMovement());

        if (pathMap.size() > 0) {
            for (Terrain t : pathMap.keySet()) {
                t.setState(Terrain.State.MOVEABLE);
            }
        }

        // find attack
        List<Terrain> inRange = selectRange(activePawn.getWeapon().getRange(), parentTerrain);

        rangeMap = filterRange(parentTerrain, inRange);

        boolean noOptions = true;

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

        if (!activePawn.checkReady(noOptions)) {
            setActivePawn(null);
        } else {
            setSelectablePawn();
        }
    }

    private void setStateSelectMove() {
        activePawn.getAnimationState().setAction("idle", false);

        // find movement
        Terrain parentTerrain = activePawn.getParent();

        PathFinder<Terrain> pf = new PathFinder<>();

        pathMap = pf.findDistance(parentTerrain, activePawn.getRemainingMovement());

        if (pathMap.size() > 0) {
            for (Terrain t : pathMap.keySet()) {
                t.setState(Terrain.State.MOVEABLE);
            }
        }

        activePawn.setReady(true);
        setSelectablePawn();
    }

    private void setStateSelectAttack() {
        activePawn.getAnimationState().setAction("idle", false);

        // find attack
        Terrain parentTerrain = activePawn.getParent();

        List<Terrain> inRange = selectRange(activePawn.getWeapon().getRange(), parentTerrain);

        rangeMap = filterRange(parentTerrain, inRange);

        boolean noOptions = true;

        if (rangeMap.size() > 0) {
            for (Terrain n : rangeMap.keySet()) {
//                if (n.getPawn() == null || n.getPawn().getTeam() != activePawn.getTeam()) {

                    n.cover = rangeMap.get(n);
                    n.setState(Terrain.State.ATTACKABLE);
                    n.updateText();

                    noOptions = false;
//                }
            }
        }

        if (!activePawn.checkReady(noOptions)) {
            setActivePawn(null);
        } else {
            setSelectablePawn();
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
//                if (o.isPresent() &&
//                        o.get().getPawn() != null &&
//                        o.get().getPawn().getTeam() != activePawn.getTeam()) {
                o.ifPresent(withinRange::add);
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

    public void setActiveDetailText(SelectionDetails details) {
        if (uiActiveDetailText != null)
            uiActiveDetailText.setDetails(details);
    }

    public void setSelectedDetailText(SelectionDetails details) {
        if (uiSelectedDetailText != null)
            uiSelectedDetailText.setDetails(details);
    }

    // actions - should be the prime way of interacting with the battle scene state
    public void pawnWait() {
        activePawn.setReady(false);
        setActivePawn(null);
    }

    public void pawnDeselect() {
        setActivePawn(null);
    }

    public void pawnMove() {
        setState(BattleScene.State.MOVING);
    }
}
