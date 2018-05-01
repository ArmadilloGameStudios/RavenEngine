package com.raven.breakingsands.scenes.battlescene.ai;

import com.raven.breakingsands.scenes.battlescene.BattleScene;
import com.raven.breakingsands.scenes.battlescene.map.Terrain;
import com.raven.engine2d.util.pathfinding.Path;
import com.raven.engine2d.util.pathfinding.PathFinder;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class AI implements Runnable {

    private static int comparePaths(Path<Terrain> a, Path<Terrain> b) {
        if (a == null && b == null) {
            return 0;
        } else if (a == null) {
            return -1;
        } else if (b == null) {
            return 1;
        }

        return a.size() - b.size();
    }

    private final BattleScene scene;
    private Path<Terrain> currentPath;
    private boolean moving = false;
    private Terrain attack = null;

    public AI(BattleScene scene) {
        this.scene = scene;
    }

    @Override
    public void run() {
        try {
            // create a clean slate, since this object is reused each time
            clean();

            // check if can attack
            List<Terrain> inRange = scene.selectRange(scene.getActivePawn().getWeapon().getRange(), scene.getActivePawn().getParent());
            HashMap<Terrain, Float> rangeMap = scene.filterRange(scene.getActivePawn().getParent(), inRange);

            if (inRange.size() > 0) {
                Optional<Terrain> optionalTerrain = inRange.stream().filter(Objects::nonNull).max((a, b) -> (int) (rangeMap.get(a) - rangeMap.get(b) * 100));

                if (optionalTerrain.isPresent()) {
                    attack = optionalTerrain.get();
                    return;
                }
            }

            // find the closest pawn
            PathFinder<Terrain> pf = new PathFinder<>();

            Optional<Path<Terrain>> oPath = scene.getPawns().stream().filter(p -> p.getTeam() == 0).map(p ->
                    pf.findTarget(scene.getActivePawn().getParent(), p.getParent()))
                    .filter(Objects::nonNull)
                    .min(AI::comparePaths);

            if (!oPath.isPresent()) {
                System.out.println("No Path");
                return;
            }

            // find the furthest it can go
            Path<Terrain> closestPath = oPath.get();
            HashMap<Terrain, Path<Terrain>> pathMap = pf.findDistance(scene.getActivePawn().getParent(), scene.getActivePawn().getRemainingMovement());

            if (pathMap.size() == 0) {
                System.out.println("Map Size 0");
                return;
            }

            System.out.println(closestPath.size());

//        for (Terrain step : closestPath) {
            for (int i = 0; i < closestPath.size(); i++) {
                Terrain step = closestPath.get(closestPath.size() - 1 - i).getNode();

                if (pathMap.keySet().contains(step)) {
                    System.out.println("Found " + i);

                    moving = true;
                    currentPath = pathMap.get(step);
                    break;
                }
            }

            System.out.println(moving);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void clean() {
        System.out.println("Clean");
        currentPath = null;
        moving = false;
        attack = null;
    }

    public void resolve() {
        if (attack != null) {
            scene.setTargetPawn(attack.getPawn());
            scene.setState(BattleScene.State.ATTACKING);
            return;
        }

        if (moving) {
            scene.setCurrentPath(currentPath);
            scene.setState(BattleScene.State.MOVING);
        } else {
            scene.selectNextPawn();
        }
    }
}
