package com.raven.breakingsands.scenes.battlescene;

import com.raven.breakingsands.BreakingSandsGame;
import com.raven.breakingsands.character.Ability;
import com.raven.breakingsands.character.Effect;
import com.raven.breakingsands.character.Weapon;
import com.raven.breakingsands.scenes.battlescene.ai.AI;
import com.raven.breakingsands.scenes.battlescene.decal.Wall;
import com.raven.breakingsands.scenes.battlescene.map.Map;
import com.raven.breakingsands.scenes.battlescene.map.Terrain;
import com.raven.breakingsands.scenes.battlescene.menu.Menu;
import com.raven.breakingsands.scenes.battlescene.pawn.Hack;
import com.raven.breakingsands.scenes.battlescene.pawn.Pawn;
import com.raven.breakingsands.scenes.battlescene.pawn.PawnMessage;
import com.raven.breakingsands.scenes.battlescene.pawn.PawnFactory;
import com.raven.breakingsands.scenes.hud.UIBottomLeftContainer;
import com.raven.breakingsands.scenes.hud.UIBottomRightContainer;
import com.raven.breakingsands.scenes.hud.UICenterContainer;
import com.raven.engine2d.Game;
import com.raven.engine2d.GameEngine;
import com.raven.engine2d.GameProperties;
import com.raven.engine2d.database.GameData;
import com.raven.engine2d.database.GameDataList;
import com.raven.engine2d.database.GameDatabase;
import com.raven.engine2d.database.GameDatable;
import com.raven.engine2d.graphics2d.sprite.SpriteAnimationState;
import com.raven.engine2d.graphics2d.sprite.SpriteSheet;
import com.raven.engine2d.scene.Scene;
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

public class BattleScene extends Scene<BreakingSandsGame> implements GameDatable {
    public static Highlight
            OFF = new Highlight(),
            BLUE = new Highlight(.2f, .6f, 1f, .75f),
            BLUE_CHANGING = new Highlight(.2f, .6f, 1f, .5f),
            RED = new Highlight(1f, .1f, .05f, .75f),
            RED_CHANGING = new Highlight(1f, .3f, .2f, .5f),
            YELLOW = new Highlight(1f, .8f, .2f, .75f),
            YELLOW_CHANGING = new Highlight(1f, .8f, .2f, .5f),
            GREEN = new Highlight(.3f, 1f, .2f, .75f),
            GREEN_CHANGING = new Highlight(.3f, 1f, .2f, .5f),
            PURPLE = new Highlight(.5f, .1f, .8f, .75f),
            PURPLE_CHANGING = new Highlight(.5f, .1f, .8f, .75f);

    private GameData loadGameData = null;

    private Menu menu;
    private UIActionSelect actionSelect;
    private UILevelUp uiLevelUp;
    private UIDetailText uiActiveDetailText;
    private UIDetailText uiSelectedDetailText;

    public enum State {
        MOVING, ATTACKING, SELECT_DEFAULT, SELECT_MOVE, SELECT_ATTACK, SELECT_ABILITY
    }

    private Random random = new Random();

    private Map map;

    private HashMap<Terrain, Path<Terrain>> pathMap;
    private Path<Terrain> currentPath;
    private int pathIndex = 0;
    private float pathSpeed = 4f * 100f;
    private float pathMoveTime = 0f;

    private HashMap<Terrain, Float> rangeMap;

    private List<Pawn> pawns = new CopyOnWriteArrayList<>();
    private List<Pawn> playersPawns;
    private Pawn activePawn;
    private Pawn targetPawn;

    private int activeTeam = 0;
    private Ability activeAbility;

    private State state = SELECT_DEFAULT;

    private ExecutorService aiExecutorService = Executors.newSingleThreadExecutor();
    private AI ai = new AI(this);
    private Future aiFuture;

    public BattleScene(BreakingSandsGame game, List<Pawn> playersPawns) {
        super(game);

        this.playersPawns = playersPawns;
    }

    public BattleScene(BreakingSandsGame game, GameData savedData) {
        super(game);

        loadGameData = savedData;
    }

    @Override
    public GameData toGameData() {
        HashMap<String, GameData> map = new HashMap<>();

        map.put("pawns", new GameDataList(pawns).toGameData());
        map.put("map", this.map.toGameData());
        map.put("activeTeam", new GameData(activeTeam));
        map.put("activePawn", new GameData(pawns.indexOf(activePawn)));

        return new GameData(map);
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

        Vector2f wo = this.getWorldOffset();
        wo.x = GameProperties.getScreenWidth() / GameProperties.getScaling();
        wo.y = GameProperties.getScreenHeight() / GameProperties.getScaling();

        if (loadGameData != null) {
            // Pawns
            loadGameData.getList("pawns").forEach(p -> {
                pawns.add(new Pawn(this, p));
            });

            // Terrain
            map = new Map(this, loadGameData.getData("map"));
            getLayerTerrain().addChild(map);
            map.getTerrainList().forEach(Terrain::setPawnIndex);

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

            // Level UP
            UICenterContainer<BattleScene> centerContainer = new UICenterContainer<>(this);
            getLayerUI().addChild(centerContainer);
            uiLevelUp = new UILevelUp(this);
            centerContainer.addChild(uiLevelUp);
            centerContainer.pack();
            uiLevelUp.setVisibility(false);

            // Menu
            menu = new Menu(this);
            menu.pack();
            getLayerUI().addChild(menu);
            menu.setVisibility(false);

//          victory();

            activeTeam = loadGameData.getInteger("activeTeam");
            if (loadGameData.getInteger("activePawn") >= 0) {
                Pawn a = pawns.get(loadGameData.getInteger("activePawn"));
                setActivePawn(a);
            } else
                setActivePawn(null);
        } else {
            int difficulty = 1;
            if (playersPawns != null) {
                difficulty = (int) (Math.max(playersPawns.stream().mapToInt(Pawn::getLevel).sum() / 1.5 - 2, 1.0));
            }

            // Terrain
            map = new Map(this, difficulty);
            map.generate();
            getLayerTerrain().addChild(map);

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

            // Level UP
            UICenterContainer<BattleScene> centerContainer = new UICenterContainer<>(this);
            getLayerUI().addChild(centerContainer);
            uiLevelUp = new UILevelUp(this);
            centerContainer.addChild(uiLevelUp);
            centerContainer.pack();
            uiLevelUp.setVisibility(false);

            // Menu
            menu = new Menu(this);
            menu.pack();
            getLayerUI().addChild(menu);
            menu.setVisibility(false);

//          victory();

            addPawns();

            setActiveTeam(0);

        }

        // restore shield
        pawns.forEach(Pawn::restoreShield);
    }

    private void addPawns() {
        List<Terrain> terrainList = map.getTerrainList();

        if (playersPawns == null)
            for (int i = 0; i < 4; i++) {
                GameData gdPawn = GameDatabase.all("pawn").stream()
                        .filter(p -> p.getString("name").equals("amateur"))
                        .findFirst().get();

                Pawn p = new Pawn(this, gdPawn);
                pawns.add(p);

                Optional<Terrain> o = terrainList.stream()
                        .filter(t -> t.isPassable() && t.isStart()).findAny();

                o.ifPresent(t -> map.setPawn(t, p));
            }
        else {
            playersPawns.forEach(p -> {
                p.setScene(this);
                pawns.add(p);

                Optional<Terrain> o = terrainList.stream()
                        .filter(t -> t.isPassable() && t.isStart()).findAny();

                o.ifPresent(t -> map.setPawn(t, p));
            });
        }

        terrainList.forEach(t -> {
            if (t.isSpawn()) {
                spawnPawn(1, t);
            }
        });

        setActivePawn(null);
        setState(SELECT_DEFAULT);
    }

    public List<Pawn> getPawns() {
        return pawns;
    }

    public void spawnPawn(int team, Terrain terrain) {
        if (terrain.isPassable()) {
            PawnFactory pf = new PawnFactory(this);
            pf.setTeam(team);
            Pawn p = pf.getInstance();

            pawns.add(p);
            map.setPawn(terrain, p);
        }
    }

    @Override
    public void onExitScene() {
        getGame().saveGame();
    }

    private Vector2f tempVec2 = new Vector2f();

    @Override
    public void onUpdate(float deltaTime) {
        float a = (float) (Math.cos(GameEngine.getEngine().getSystemTime() * .005) * .15 + .4);

        PURPLE_CHANGING.a = BLUE_CHANGING.a = RED_CHANGING.a = GREEN_CHANGING.a = YELLOW_CHANGING.a = a;

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

            setActivePawn(activePawn);
        }
    }

    public void removePawn(Pawn pawn) {
        pawns.remove(pawn);
    }

    public UILevelUp getUILevelUp() {
        return uiLevelUp;
    }

    public Map getTerrainMap() {
        return map;
    }

    public void setActivePawn(Pawn pawn) {
        getGame().saveGame();

        if (checkVictory()) return;

        if (activePawn != null && activePawn != pawn) {
            activePawn.setReadyIfUnmoved();
        }

        this.activePawn = pawn;

        if (activePawn != null) {
            activePawn.getParent().updateText();

            if (pawn.getTeam() == 0)
                setActiveDetailText(activePawn.getParent().getDetails());
            else
                clearActiveDetailText();


            Vector2f pos = pawn.getWorldPosition();
            // TODO focus on pawn

//            System.out.println("Pawn -");
//            pawn.getAbilities().forEach(a -> System.out.println(a.name));
//            System.out.println("Terrain -");
//            pawn.getParent().getAbilities().forEach(a -> System.out.println(a.name));

        }

        if (pawn != null && pawn.getTeam() == 0)
            actionSelect.setPawn(pawn);
        else
            actionSelect.setPawn(null);

        setState(SELECT_DEFAULT);
    }

    public Pawn getActivePawn() {
        return activePawn;
    }

    public int getActiveTeam() {
        return activeTeam;
    }

    public void setActiveTeam(int team) {

        if (team != activeTeam) {
            pawns.forEach(pawn -> {
                Hack hack = pawn.getHack();
                if (hack != null && hack.getTeam() == activeTeam) {
                    hack.tick();

                    if (hack.getRemainingTurnsTurns() <= 0) {
                        if (hack.getSelfDestruct() > 0) {
                            pawn.damage(5);
                        }
                        pawn.hack(null);
                    }
                }
            });
        }

        this.activeTeam = team;

        pawns.stream()
                .filter(p -> p.getTeam() == activeTeam)
                .forEach(Pawn::ready);

        setActivePawn(null);
    }

    public Ability getActiveAbility() {
        return activeAbility;
    }

    public void setActiveAbility(Ability ability) {
        this.activeAbility = ability;
    }

    public void setTargetPawn(Pawn targetPawn) {
        this.targetPawn = targetPawn;
    }

    private boolean doSpawn() {
        int a = 10 - (int) pawns.stream().filter(p -> p.getTeam() != 0).count();
        int b = random.nextInt(10);

        return a > b || true;
    }

    public void setState(State state) {
        this.state = state;
//        System.out.println("State: " + state);

        switch (state) {
            case SELECT_DEFAULT:
                if (activeTeam == 0) {
                    // clean
                    map.setState(Terrain.State.UNSELECTABLE);
                    currentPath = null;

                    if (activePawn != null) {
                        setStateSelectDefault();
                    } else {
                        setSelectablePawn();
                    }
                } else {
                    if (activePawn != null) {
                        activePawn.getAnimationState().setActionIdle();
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
            case SELECT_ABILITY:
                if (activeTeam == 0) {

                    // clean
                    map.setState(Terrain.State.UNSELECTABLE);
                    currentPath = null;

                    if (activePawn != null) {
                        setStateSelectAbility();
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
            setActivePawn(activePawn);
        });
    }

    private void setStateSelectDefault() {
        activePawn.getAnimationState().setActionIdle(false);

        Terrain parentTerrain = activePawn.getParent();

        if (activePawn.canMove()) {
            // find movement
            PathFinder<Terrain> pf = new PathFinder<>();

            pathMap = pf.findDistance(parentTerrain, activePawn.getRemainingMovement());

            if (pathMap.size() > 0) {
                for (Terrain t : pathMap.keySet()) {
                    t.setState(Terrain.State.MOVEABLE);
                }
            }
        }

        if (activePawn.canAttack()) {
            // find attack
            List<Terrain> inRange = parentTerrain.selectRange(activePawn.getWeapon().getStyle(), activePawn.getWeapon().getRangeMin(), activePawn.getWeapon().getRange());
            rangeMap = parentTerrain.filterCoverRange(inRange);

            if (rangeMap.size() > 0) {
                for (Terrain n : rangeMap.keySet()) {
                    if (n.getPawn() != null && n.getPawn().getTeam() != activePawn.getTeam()) {

//                    n.cover = rangeMap.get(n);
                        n.setState(Terrain.State.ATTACKABLE);
                        n.updateText();
                    }
                }
            }
        }

        setSelectablePawn();
    }

    private void setStateSelectMove() {
        activePawn.getAnimationState().setActionIdle(false);

        if (activePawn.canMove()) {  // TODO add ability
            // find movement
            Terrain parentTerrain = activePawn.getParent();

            PathFinder<Terrain> pf = new PathFinder<>();

            pathMap = pf.findDistance(parentTerrain, activePawn.getRemainingMovement());

            if (pathMap.size() > 0) {
                for (Terrain t : pathMap.keySet()) {
                    t.setState(Terrain.State.MOVEABLE);
                }
            }
        }

        setSelectablePawn();
    }

    private void setStateSelectAttack() {
        activePawn.getAnimationState().setActionIdle(false);

        if (activePawn.canAttack()) {
            // find attack
            Terrain parentTerrain = activePawn.getParent();

            List<Terrain> inRange = parentTerrain.selectRange(activePawn.getWeapon().getStyle(), activePawn.getWeapon().getRangeMin(), activePawn.getWeapon().getRange());
            rangeMap = parentTerrain.filterCoverRange(inRange);

            if (rangeMap.size() > 0) {
                for (Terrain n : rangeMap.keySet()) {
                    n.setState(Terrain.State.ATTACKABLE);
                    n.updateText();
                }
            }
        }

        setSelectablePawn();
    }

    private void setStateSelectAbility() {
        activePawn.getAnimationState().setActionIdle(false);

        if (activePawn.canAbility(activeAbility)) {
            // find target
            Terrain parentTerrain = activePawn.getParent();

            List<Terrain> inRange = parentTerrain.selectRange(activeAbility.style, activeAbility.size);
            rangeMap = parentTerrain.filterCoverRange(inRange);

            if (rangeMap.size() > 0) {
                for (Terrain n : rangeMap.keySet()) {
                    if (n.getPawn() == null || n.getPawn().getTeam() != activePawn.getTeam()) {
                        n.setState(Terrain.State.ABILITYABLE);
                        n.updateText();
                    }
                }
            }
        }

        setSelectablePawn();
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
        if (pathMap != null) {
            for (Terrain t : pathMap.keySet()) {
                t.setState(Terrain.State.UNSELECTABLE);
            }

            pathMap.clear();
        }
    }

    public void setCurrentPath(Path<Terrain> currentPath) {
        this.currentPath = currentPath;
    }

    public boolean checkVictory() {
        for (Pawn pawn : pawns) {
            if (pawn.getTeam() != 0)
                return false;
        }

        victory();
        return true;
    }

    public void victory() {
        getGame().prepTransitionScene(new BattleScene(getGame(), pawns.stream().filter(p -> p.getTeam() == 0 && p.getHack() == null).collect(Collectors.toList())));
    }

    private void clearActiveDetailText() {
        if (uiActiveDetailText != null)
            uiActiveDetailText.setDetails(new SelectionDetails());
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

    public void pawnEnd() {
        pawns.stream()
                .filter(p -> p.getTeam() == 0)
                .forEach(p -> p.setReady(false));
        setActiveTeam(1);
    }

    public void pawnDeselect() {
        setActivePawn(null);
    }

    public void pawnMove() {
        setState(BattleScene.State.MOVING);
    }

    public void pawnAttack(Pawn pawn) {
        setTargetPawn(pawn);
        setState(BattleScene.State.ATTACKING);
    }

    public void pawnAbility(Pawn pawn) {
        if (activeAbility.uses != null) {
            activeAbility.remainingUses--;
        }

        if (activeAbility.hook_pull) {
            Terrain base = activePawn.getParent();
            Terrain from = pawn.getParent();
            Optional<Terrain> to = Optional.empty();

            if (base.getMapX() > from.getMapX()) {
                to = map.get(base.getMapX() - 1, base.getMapY());
            }
            if (base.getMapX() < from.getMapX()) {
                to = map.get(base.getMapX() + 1, base.getMapY());
            }
            if (base.getMapY() > from.getMapY()) {
                to = map.get(base.getMapX(), base.getMapY() - 1);
            }
            if (base.getMapY() < from.getMapY()) {
                to = map.get(base.getMapX(), base.getMapY() + 1);
            }

            to.ifPresent(t -> t.setPawn(pawn));

            setActivePawn(activePawn);
        } else if (activeAbility.hack) {
            pawn.hack(new Hack(0, activeAbility.turns, activeAbility.damage != null ? activeAbility.damage : 0));
            pawn.ready();
            activePawn.setUnmoved(activeAbility.remain);
            if (activePawn.isReady())
                setActivePawn(activePawn);
            else
                setActivePawn(null);
        }
    }

    public void pawnLevel() {
        this.setPaused(true);
        uiLevelUp.setVisibility(true);
        uiLevelUp.setPawn(getActivePawn());
    }

    public void pawnPushBlast(Ability blast) { // TODO change to make more generic on button click
        if (activeAbility.uses != null) {
            activeAbility.remainingUses--;
        }

        List<Pawn> pawns = this.pawns.stream()
                .filter(p ->
                        p.getAbilityAffects().stream()
                                .anyMatch(a -> a == blast))
                .collect(Collectors.toList());

        // push
        pawns.forEach(p -> {
            int x = Integer.signum(p.getParent().getMapX() - activePawn.getParent().getMapX());
            int y = Integer.signum(p.getParent().getMapY() - activePawn.getParent().getMapY());

            map.get(p.getParent().getMapX() + x, p.getParent().getMapY() + y).ifPresent(t -> {
                if (t.isPassable()) {
                    t.setPawn(p);
                }
            });

            activePawn.attack(p, activeAbility.damage, 0, 1);
        });

        activePawn.reduceAttacks();
        setActivePawn(activePawn);
    }
}
