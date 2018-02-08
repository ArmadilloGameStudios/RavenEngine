package com.raven.breakingsands.scenes.battlescene;

import com.raven.breakingsands.BreakingSandsGame;
import com.raven.breakingsands.Character;
import com.raven.breakingsands.scenes.battlescene.decal.Decal;
import com.raven.breakingsands.scenes.battlescene.decal.DecalFactory;
import com.raven.breakingsands.scenes.battlescene.menu.Menu;
import com.raven.breakingsands.scenes.hud.HUDBottomContainer;
import com.raven.breakingsands.scenes.battlescene.pawn.Pawn;
import com.raven.breakingsands.scenes.battlescene.pawn.PawnFactory;
import com.raven.breakingsands.scenes.battlescene.terrain.Terrain;
import com.raven.engine.GameEngine;
import com.raven.engine.graphics3d.ModelData;
import com.raven.engine.scene.Camera;
import com.raven.engine.scene.Scene;
import com.raven.engine.scene.light.GlobalDirectionalLight;
import com.raven.engine.util.Range;
import com.raven.engine.util.Vector3f;
import com.raven.engine.util.pathfinding.Path;
import com.raven.engine.util.pathfinding.PathAdjacentNode;
import com.raven.engine.util.pathfinding.PathFinder;
import com.raven.engine.worldobject.Highlight;
import com.raven.engine.worldobject.TextObject;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

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

    public enum State {
        MOVING, ATTACKING, SELECT_MOVE_AI, SELECT_MOVE,
    }

    private Random random = new Random();

    private GlobalDirectionalLight sunLight;
    private int size = 32;
    private Terrain[][] terrain;

    private HashMap<Terrain, Path<Terrain>> pathMap;
    private HashMap<Terrain, Float> rangeMap;
    private Path<Terrain> currentPath;
    private int pathIndex = 0;
    private float pathSpeed = 100f;
    private float pathMoveTime = 0f;

    private List<Pawn> pawns = new ArrayList<>();
    private Pawn activePawn;

    private HUDDetailText hudDetailText;

    private State state = SELECT_MOVE;

    public BattleScene(BreakingSandsGame game) {
        super(game);
    }

    @Override
    public List<ModelData> getSceneModels() {
        List<ModelData> models = new ArrayList<>();

        // TODO
        models.addAll(Terrain.getModelData());
        models.addAll(Decal.getModelData());
        models.addAll(Pawn.getModelData());

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

    @Override
    public void onEnterScene() {
        setBackgroundColor(new Vector3f(0.6f, 0.7f, 1f));

        // Light
        sunLight = new GlobalDirectionalLight();
        sunLight.origin = new Vector3f(0, 2, 0);
        sunLight.color = new Vector3f(1, 1, 1);
        sunLight.intensity = 1f;
        sunLight.shadowTransparency = .2f;
        sunLight.size = 46f;
        sunLight.height = 4f;

        Vector3f dir = new Vector3f(1, 5, 2);
        sunLight.setDirection(dir.normalize());

        addLight(sunLight);

        // Terrain
        terrain = new Terrain[size][size];

        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                terrain[x][y] = new Terrain(this, "sand", x, y);

                terrain[x][y].setX(x * 2 - size);
                terrain[x][y].setZ(y * 2 - size);

                getLayerTerrain().addChild(terrain[x][y]);
            }
        }

        DecalFactory f = new DecalFactory(this);
        f.addTypeRestriction("tall");
        f.addTypeRestriction("wall");
        f.addTypeRestriction("straight");

        // front left
        Decal decal = f.getInstance();
        terrain[7][8].setDecal(decal);

        decal = f.getInstance();
        terrain[7][9].setDecal(decal);

        decal = f.getInstance();
        terrain[7][10].setDecal(decal);

        decal = f.getInstance();
        terrain[7][11].setDecal(decal);

        decal = f.getInstance();
        decal.setRotation(180);
        terrain[9][8].setDecal(decal);

        decal = f.getInstance();
        decal.setRotation(180);
        terrain[9][9].setDecal(decal);

        decal = f.getInstance();
        decal.setRotation(180);
        terrain[9][10].setDecal(decal);

        decal = f.getInstance();
        decal.setRotation(180);
        terrain[9][11].setDecal(decal);

        decal = f.getInstance();
        decal.setRotation(180);
        terrain[21][19].setDecal(decal);

        decal = f.getInstance();
        decal.setRotation(90);
        terrain[20][20].setDecal(decal);

        decal = f.getInstance();
        decal.setRotation(270);
        terrain[8][7].setDecal(decal);

        decal = f.getInstance();
        decal.setRotation(90);
        terrain[8][12].setDecal(decal);

        decal = f.getInstance();
        terrain[7][8 + 8].setDecal(decal);

        decal = f.getInstance();
        terrain[7][9 + 8].setDecal(decal);

        decal = f.getInstance();
        terrain[7][10 + 8].setDecal(decal);

        decal = f.getInstance();
        terrain[7][11 + 8].setDecal(decal);

        decal = f.getInstance();
        decal.setRotation(180);
        terrain[9][8 + 8].setDecal(decal);

        decal = f.getInstance();
        decal.setRotation(180);
        terrain[9][9 + 8].setDecal(decal);

        decal = f.getInstance();
        decal.setRotation(180);
        terrain[9][10 + 8].setDecal(decal);

        decal = f.getInstance();
        decal.setRotation(180);
        terrain[9][11 + 8].setDecal(decal);

        decal = f.getInstance();
        decal.setRotation(270);
        terrain[8][7 + 8].setDecal(decal);

        decal = f.getInstance();
        decal.setRotation(180);
        terrain[21][8].setDecal(decal);

        decal = f.getInstance();
        decal.setRotation(270);
        terrain[20][7].setDecal(decal);

        decal = f.getInstance();
        decal.setRotation(90);
        terrain[8][12 + 8].setDecal(decal);

        f.clear();

        // Tall Corners
        f.addTypeRestriction("tall");
        f.addTypeRestriction("wall");
        f.addTypeRestriction("corner");

        decal = f.getInstance();
        terrain[7][7].setDecal(decal);

        decal = f.getInstance();
        decal.setRotation(270);
        terrain[9][7].setDecal(decal);

        decal = f.getInstance();
        decal.setRotation(90);
        terrain[7][12].setDecal(decal);

        decal = f.getInstance();
        decal.setRotation(180);
        terrain[9][12].setDecal(decal);

        decal = f.getInstance();
        terrain[7][7 + 8].setDecal(decal);

        decal = f.getInstance();
        decal.setRotation(270);
        terrain[9][7 + 8].setDecal(decal);

        decal = f.getInstance();
        decal.setRotation(90);
        terrain[7][12 + 8].setDecal(decal);

        decal = f.getInstance();
        decal.setRotation(180);
        terrain[9][12 + 8].setDecal(decal);

        decal = f.getInstance();
        terrain[19][18].setDecal(decal);

        decal = f.getInstance();
        decal.setRotation(270);
        terrain[21][18].setDecal(decal);

        decal = f.getInstance();
        decal.setRotation(90);
        terrain[19][20].setDecal(decal);

        decal = f.getInstance();
        decal.setRotation(180);
        terrain[21][20].setDecal(decal);

        decal = f.getInstance();
        terrain[19][7].setDecal(decal);

        decal = f.getInstance();
        decal.setRotation(270);
        terrain[21][7].setDecal(decal);

        decal = f.getInstance();
        decal.setRotation(90);
        terrain[19][9].setDecal(decal);

        decal = f.getInstance();
        decal.setRotation(180);
        terrain[21][9].setDecal(decal);

        f.clear();

        // Tall Ceiling
        f.addTypeRestriction("tall");
        f.addTypeRestriction("ceiling");

        decal = f.getInstance();
        terrain[8][8].setDecal(decal);

        decal = f.getInstance();
        terrain[8][9].setDecal(decal);

        decal = f.getInstance();
        terrain[8][10].setDecal(decal);

        decal = f.getInstance();
        terrain[8][11].setDecal(decal);

        decal = f.getInstance();
        terrain[8][8 + 8].setDecal(decal);

        decal = f.getInstance();
        terrain[8][9 + 8].setDecal(decal);

        decal = f.getInstance();
        terrain[8][10 + 8].setDecal(decal);

        decal = f.getInstance();
        terrain[8][11 + 8].setDecal(decal);

        decal = f.getInstance();
        terrain[20][8].setDecal(decal);

        decal = f.getInstance();
        terrain[20][19].setDecal(decal);

        f.clear();

        // Obelisks
        f.addTypeRestriction("obelisk");

        decal = f.getInstance();
        terrain[5][12].setDecal(decal);

        decal = f.getInstance();
        terrain[5][15].setDecal(decal);

        decal = f.getInstance();
        terrain[3][12].setDecal(decal);

        decal = f.getInstance();
        terrain[3][15].setDecal(decal);

        decal = f.getInstance();
        terrain[1][12].setDecal(decal);

        decal = f.getInstance();
        terrain[1][15].setDecal(decal);

        f.clear();

        // short tall transition
        f.addTypeRestriction("short");
        f.addTypeRestriction("tall");
        f.addTypeRestriction("wall");
        f.addTypeRestriction("transition");

        decal = f.getInstance();
        decal.setRotation(180);
        terrain[9][8].setDecal(decal);

        decal = f.getInstance();
        terrain[19][8].setDecal(decal);

        decal = f.getInstance();
        decal.setRotation(90);
        terrain[20][9].setDecal(decal);

        decal = f.getInstance();
        decal.setRotation(180);
        terrain[9][19].setDecal(decal);

        decal = f.getInstance();
        terrain[19][19].setDecal(decal);

        decal = f.getInstance();
        decal.setRotation(270);
        terrain[20][18].setDecal(decal);

        f.clear();

        // Short wall
        f.addTypeRestriction("short");
        f.addTypeRestriction("wall");
        f.addTypeRestriction("straight");

        decal = f.getInstance();
        terrain[10][8].setDecal(decal);

        decal = f.getInstance();
        terrain[12][8].setDecal(decal);

        decal = f.getInstance();
        terrain[14][8].setDecal(decal);

        decal = f.getInstance();
        terrain[16][8].setDecal(decal);

        decal = f.getInstance();
        terrain[18][8].setDecal(decal);

        decal = f.getInstance();
        terrain[10][19].setDecal(decal);

        decal = f.getInstance();
        terrain[12][19].setDecal(decal);

        decal = f.getInstance();
        terrain[14][19].setDecal(decal);

        decal = f.getInstance();
        terrain[16][19].setDecal(decal);

        decal = f.getInstance();
        terrain[18][19].setDecal(decal);

        decal = f.getInstance();
        decal.setRotation(90);
        terrain[20][16].setDecal(decal);

        decal = f.getInstance();
        decal.setRotation(90);
        terrain[20][15].setDecal(decal);

        decal = f.getInstance();
        decal.setRotation(90);
        terrain[20][14].setDecal(decal);

        decal = f.getInstance();
        decal.setRotation(90);
        terrain[20][13].setDecal(decal);

        decal = f.getInstance();
        decal.setRotation(90);
        terrain[20][12].setDecal(decal);

        decal = f.getInstance();
        decal.setRotation(90);
        terrain[20][11].setDecal(decal);

        // Pawns
        addPawns();

        // Bottom HUD
        HUDBottomContainer bottomContainer = new HUDBottomContainer(this);
        getLayerHUD().addChild(bottomContainer);

        hudDetailText = new HUDDetailText(this);

        bottomContainer.addChild(hudDetailText);

        setDetailText(activePawn.getParent().getDetailText());

        // Menu
        menu = new Menu(this);
        getLayerHUD().addChild(menu);
        menu.setVisibility(false);
    }

    private void addPawns() {
        // characters
        int offset = 0;
        for (Character character : getGame().getCharacters()) {

            Pawn p = new Pawn(this, character);
            pawns.add(p);
            terrain[29][29 - offset].setPawn(p);

            offset++;
        }

        // enemy
        PawnFactory pf = new PawnFactory(this);
        pf.setName("Enemy");
        Pawn p = pf.getInstance();
        pawns.add(p);
        terrain[7][6].setPawn(p);

        p = pf.getInstance();
        pawns.add(p);
        terrain[7][5].setPawn(p);

        p = pf.getInstance();
        pawns.add(p);
        terrain[7][4].setPawn(p);

        p = pf.getInstance();
        pawns.add(p);
        terrain[6][6].setPawn(p);

        p = pf.getInstance();
        pawns.add(p);
        terrain[6][5].setPawn(p);

        p = pf.getInstance();
        pawns.add(p);
        terrain[6][4].setPawn(p);

        activePawn = pawns.get(pawns.size() - 1);

        setState(SELECT_MOVE);
    }

    @Override
    public void onExitScene() {

    }

    @Override
    public void onUpdate(float deltaTime) {
        float a = (float) (Math.cos(GameEngine.getEngine().getSystemTime() * .004) * .075 + .4);

        BLUE_CHANGING.a = RED_CHANGING.a = GREEN_CHANGING.a = YELLOW_CHANGING.a = a;

        switch (state) {
            case MOVING:
                movePawn(deltaTime);
                break;
        }
    }

    public float movePawn(float delta) {

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

            Vector3f movement = next.getNode().getPosition().subtract(current.getPosition());

            activePawn.move(movement.scale(delta / (pathSpeed * cost)));

            if (overflow > 0f) {
                pathIndex += 1;
                movePawn(overflow);
            }
        } else {
            pathIndex = 0;
            pathMoveTime = 0f;

            activePawn.setPosition(new Vector3f());
            current.setPawn(activePawn);

            if (activePawn.getTeam() == 0 &&
                    (activePawn.getRemainingMovement() > 0 ||
                            activePawn.getRemainingAttacks() > 0))
                setState(State.SELECT_MOVE);
            else
                selectNextPawn();
        }

        return delta;
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

    public Terrain[][] getTerrainMap() {
        return terrain;
    }

    public int getTerrainMapSize() {
        return size;
    }

    public void selectNextPawn() {
        setActivePawn(getNextPawn());
    }

    public void setActivePawn(Pawn pawn) {
        this.activePawn = pawn;

        activePawn.ready();

        Camera camera = getCamera();
        camera.setPosition(pawn.getParent().getX(), pawn.getParent().getZ());

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

    public void setState(State state) {
        this.state = state;
        System.out.println("State: " + state);

        switch (state) {
            case SELECT_MOVE:
                setStateSelectMove();
                break;
            case SELECT_MOVE_AI:
                currentPath = null;

                PathFinder<Terrain> pf = new PathFinder<>();

                pathMap = pf.findDistance(activePawn.getParent(), activePawn.getRemainingMovement());
                if (pathMap.size() > 0) {
                    int rand = random.nextInt(pathMap.size());

                    currentPath = pathMap.get(pathMap.keySet().toArray()[rand]);

                    setState(State.MOVING);
                } else {
                    selectNextPawn();
                }
                break;
            case MOVING:
                activePawn.move(currentPath.getCost());

                for (Terrain[] row : terrain) {
                    for (Terrain t : row) {
                        t.setState(Terrain.State.UNSELECTABLE);
                    }
                }
                break;
            case ATTACKING:
                break;
        }
    }

    private void setStateSelectMove() {

        // clear
        for (Terrain[] row : terrain) {
            for (Terrain t : row) {
                t.setState(Terrain.State.UNSELECTABLE);
            }
        }
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

    public List<Terrain> selectRange(int range, Terrain start) {
        int x = start.getMapX();
        int y = start.getMapY();

        List<Terrain> withinRange = new ArrayList<>();

        for (int i = Math.max(-range + x, 0) - x;
             i <= Math.min(range + x, size - 1) - x;
             i++) {

            int heightRange = range - Math.abs(i);

            for (int j = Math.max(-heightRange + y, 0) - y;
                 j <= Math.min(heightRange + y, size - 1) - y;
                 j++) {

                withinRange.add(terrain[x + i][y + j]);
            }
        }

        return withinRange;
    }

    private HashMap<Terrain, Float> filterRange(Terrain start, List<Terrain> inRange) {
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

                    if (!terrain[x][y].isPassable()) {

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
                map.put(terrain[endX][endY], coverage);
            }
        }

        return map;
    }

    private float linePointDist(float a, float b, float c, float x, float y) {
        return ((a * x + b * y + c) / (Math.abs(a) + Math.abs(b)));
    }

    public void setDetailText(TextObject textObject) {
        hudDetailText.setTexture(textObject);
    }
}
