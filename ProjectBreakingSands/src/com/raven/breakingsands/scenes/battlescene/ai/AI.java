package com.raven.breakingsands.scenes.battlescene.ai;

import com.raven.breakingsands.scenes.battlescene.BattleScene;
import com.raven.breakingsands.scenes.battlescene.map.Terrain;
import com.raven.engine.util.pathfinding.Path;
import com.raven.engine.util.pathfinding.PathFinder;

import java.util.HashMap;

public class AI implements Runnable {

    private final BattleScene scene;
    private Path<Terrain> currentPath;
    private boolean moving = false;

    public AI(BattleScene scene) {
        this.scene = scene;
    }

    @Override
    public void run() {
        PathFinder<Terrain> pf = new PathFinder<>();

        HashMap<Terrain, Path<Terrain>> pathMap = pf.findDistance(scene.getActivePawn().getParent(), scene.getActivePawn().getRemainingMovement());
        if (pathMap.size() > 0) {
            moving = true;

            int rand = scene.getRandom().nextInt(pathMap.size());

            currentPath = pathMap.get(pathMap.keySet().toArray()[rand]);
        } else {
            moving = false;
        }
    }

    public void resolve() {
        if (moving) {
            scene.setCurrentPath(currentPath);
            scene.setState(BattleScene.State.MOVING);
        } else {
            scene.selectNextPawn();
        }
    }
}
