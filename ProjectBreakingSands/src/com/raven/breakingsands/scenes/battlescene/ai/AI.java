package com.raven.breakingsands.scenes.battlescene.ai;

import com.raven.breakingsands.scenes.battlescene.BattleScene;
import com.raven.breakingsands.scenes.battlescene.map.Terrain;
import com.raven.breakingsands.scenes.battlescene.pawn.Pawn;
import com.raven.engine2d.util.pathfinding.Path;
import com.raven.engine2d.util.pathfinding.PathFinder;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

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
    private Pawn select = null;
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

            if (scene.getActivePawn() == null || !scene.getActivePawn().isReady()) {
                selectPawn();
            } else {
                selectMove();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void selectPawn() {
        List<Pawn> pawns = scene.getPawns().stream()
                .filter(p -> p.getTeam() != 0 && p.isReady())
                .collect(Collectors.toList());

        if (pawns.size() > 0) {
            select = pawns.get(0);
        }
    }

    private void selectMove() {

        // check if taunted
        List<Pawn> taunters = scene.getActivePawn().getAbilityAffects().stream()
                .filter(a -> a.taunt)
                .map(a -> a.owner)
                .collect(Collectors.toList());

        // check if can attack
        List<Terrain> inRange = scene.getActivePawn().getParent().selectRange(
                scene.getActivePawn().getWeapon().getStyle(),
                scene.getActivePawn().getWeapon().getRange());
        inRange = inRange.stream()
                .filter(t -> t.getPawn() != null &&
                        t.getPawn().getTeam() != scene.getActiveTeam())
                .collect(Collectors.toList());

        if (taunters.size() > 0) {
            inRange = inRange.stream()
                    .filter(t -> taunters.contains(t.getPawn()))
                    .collect(Collectors.toList());
        }

        HashMap<Terrain, Float> rangeMap = scene.getActivePawn().getParent().filterCoverRange(inRange);

        if (rangeMap.size() > 0) {
            Optional<Terrain> optionalTerrain;

            if (rangeMap.size() > 1) {
                optionalTerrain = rangeMap.keySet().stream()
                        .filter(Objects::nonNull)
                        .max((a, b) -> (int) (rangeMap.get(a) - rangeMap.get(b)));

                if (optionalTerrain.isPresent()) {
                    attack = optionalTerrain.get();
                    return;
                }
            } else {
                attack = rangeMap.keySet().stream().findFirst().get();
                return;
            }
        }

        // find the closest pawn
        PathFinder<Terrain> pf = new PathFinder<>();
        Optional<Path<Terrain>> oPath;

        if (taunters.size() > 0) {
            oPath = taunters.stream().filter(p -> p.getTeam() == 0).map(p ->
                    pf.findTarget(scene.getActivePawn().getParent(), p.getParent()))
                    .filter(Objects::nonNull)
                    .min(AI::comparePaths);
        } else {
            oPath = scene.getPawns().stream().filter(p -> p.getTeam() == 0).map(p ->
                    pf.findTarget(scene.getActivePawn().getParent(), p.getParent()))
                    .filter(Objects::nonNull)
                    .min(AI::comparePaths);
        }

        if (!oPath.isPresent()) {
            System.out.println("No Path");
            return;
        }

        // find the furthest it can go
        // TODO change to it goes as close while remaining its range
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
    }

    private void clean() {
        System.out.println("Clean");
        currentPath = null;
        moving = false;
        attack = null;
        select = null;
    }

    public void resolve() {
        if (attack != null) {
            scene.setTargetPawn(attack.getPawn());
            scene.getActivePawn().setReady(false);
            scene.setState(BattleScene.State.ATTACKING);
            return;
        }

        if (moving) {
            scene.setCurrentPath(currentPath);
            scene.setState(BattleScene.State.MOVING);
            return;
        }

        if (select != null) {
            scene.setActivePawn(select);
            return;
        }

        if (scene.getActivePawn() == null) {
            scene.setActiveTeam(0);
        } else {
            scene.getActivePawn().setReady(false);
            scene.setActivePawn(null);
        }
    }
}
