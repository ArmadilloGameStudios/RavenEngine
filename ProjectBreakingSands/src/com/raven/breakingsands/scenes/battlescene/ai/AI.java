package com.raven.breakingsands.scenes.battlescene.ai;

import com.raven.breakingsands.character.Weapon;
import com.raven.breakingsands.scenes.battlescene.BattleScene;
import com.raven.breakingsands.scenes.battlescene.map.Terrain;
import com.raven.breakingsands.scenes.battlescene.pawn.Pawn;
import com.raven.engine2d.util.pathfinding.Path;
import com.raven.engine2d.util.pathfinding.PathAdjacentNode;
import com.raven.engine2d.util.pathfinding.PathFinder;
import org.lwjgl.system.CallbackI;
import org.omg.Messaging.SYNC_WITH_TRANSPORT;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AI implements Runnable {

    private final BattleScene scene;
    private Path<Terrain> currentPath;
    private Pawn select = null;
    private boolean moving = false;
    private Terrain attack = null;
    private Map<Pawn, Map<Terrain, Path<Terrain>>> attackableTerrain = new HashMap<>();
    private Map<Pawn, Map<Terrain, Path<Terrain>>> attackableUnblockedTerrain = new HashMap<>();
    private Map<Pawn, Collection<Terrain>> enemyAttackTerrain = new HashMap<>();
    private PathFinder<Terrain, Terrain.PathFlag> terrainPathFinder = new PathFinder<>();

    public AI(BattleScene scene) {
        this.scene = scene;
    }

    @Override
    public void run() {
        System.out.println();

        try {
            clean();

            createAttackMap();

            if (scene.getActivePawn() == null) {
                selectPawn();
            } else {
                selectMove();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createAttackMap() {
        getAllCanMoveAttackPawns().forEach(p -> {
            // get all it can attack from
            List<Terrain> canAttackFrom = new ArrayList<>();
            getAllEnemyPawns().forEach(e ->
                    canAttackFrom.addAll(
                            e.getParent().selectRange(
                                    p.getWeapon().getStyle(),
                                    p.getWeapon().getRangeMin(),
                                    p.getWeapon().getRange(),
                                    false,
                                    false)));

            // create path to each
            Map<Terrain, Path<Terrain>> costMapA = new HashMap<>();
            attackableTerrain.put(p, costMapA);
            canAttackFrom.forEach(t -> {
                Path<Terrain> pt = terrainPathFinder.findTarget(p.getParent(), t, Terrain.PathFlag.PASS_PAWN);
//                if (pt != null)
                costMapA.put(t, pt);
            });

            Map<Terrain, Path<Terrain>> costMapB = new HashMap<>();
            attackableUnblockedTerrain.put(p, costMapB);
            canAttackFrom.forEach(t -> {
                Path<Terrain> pt = terrainPathFinder.findTarget(p.getParent(), t);
//                if (pt != null)
                costMapB.put(t, pt);
            });
        });

        getAllEnemyPawns().forEach(e -> {
            Weapon w = e.getWeapon();

            Collection<Terrain> cat = e.getParent().selectRange(w.getStyle(), w.getRangeMin(), w.getRange(), false, false);

            enemyAttackTerrain.put(e, cat);
        });
    }

    private Stream<Pawn> getAllEnemyPawns() {
        return scene.getPawns().stream().filter(p -> p.getTeam(true) == 0);
    }

    private Stream<Pawn> getAllCanMoveAttackPawns() {
        return scene.getPawns().stream()
                .filter(p -> p.getTeam(true) == 1 &&
                        (p.isReady() && (p.canAttack() || p.canMove())));
    }

    private void selectPawn() {
        List<Pawn> pawns = getAllCanMoveAttackPawns()
                .collect(Collectors.toList());

        if (pawns.size() == 0) {
            System.out.println("None to Select");
            return;
        } else {
            System.out.println(Integer.toString(pawns.size()) + " to Select");
        }

        AtomicInteger cost = new AtomicInteger(Integer.MAX_VALUE);
        select = null;

        System.out.println("Finding Unblocked Path");
        pawns.forEach(pawn -> attackableUnblockedTerrain.get(pawn).forEach((aTerrain, path) -> {
            if (pawn.getParent() != aTerrain) {
                // if cant attack, check path cost
                if (path != null) {
                    int c = path.getCost();
                    if (c < cost.get()) {
                        cost.set(c);
                        select = pawn;
                    }
                }
            } else {
                // if can attack right away
                cost.set(0);
                select = pawn;
            }
        }));

        // if no unblocked paths, find a blocked path to improve
        if (select == null) {
            System.out.println("Finding Blocked Path");
            pawns.forEach(pawn -> attackableTerrain.get(pawn).forEach((aTerrain, path) -> {
                System.out.println(path);
                if (path != null) {
                    int c = 0;

                    for (PathAdjacentNode<Terrain> t : path) {
                        if (t.getNode().getPawn() == null || t.getNode().getPawn() == pawn)
                            c += t.getCost();
                        else break;
                    }

                    if (c < cost.get()) {
                        cost.set(c);
                        select = pawn;
                    }
                }
            }));
        }

        if (select == null) {
            System.out.println("No Pawns?");
        }
    }

    private void selectMove() {
        System.out.println("Selecting Move");

        // check if taunted
        List<Pawn> taunters = scene.getActivePawn().getAbilityAffects().stream()
                .filter(a -> a.taunt)
                .map(a -> a.owner)
                .collect(Collectors.toList());

        // check if can attack
        System.out.println("Attack? " + scene.getActivePawn().canAttack());
        if (scene.getActivePawn().canAttack()) {
            Collection<Terrain> inRange = scene.getActivePawn().getParent().selectRange(
                    scene.getActivePawn().getWeapon().getStyle(),
                    scene.getActivePawn().getWeapon().getRange(),
                    false, false);
            inRange = inRange.stream()
                    .filter(t -> t.getPawn() != null &&
                            t.getPawn().getTeam(true) != scene.getActiveTeam())
                    .collect(Collectors.toList());

            if (taunters.size() > 0) {
                inRange = inRange.stream()
                        .filter(t -> taunters.contains(t.getPawn()))
                        .collect(Collectors.toList());
            }

            if (inRange.size() > 0) {
                inRange.stream().findFirst().ifPresent(t -> attack = t);
                return;
            }
        }

        // end if no movement
        System.out.println("Move? " + scene.getActivePawn().canMove());
        if (!scene.getActivePawn().canMove()) {
            return;
        }

        // find the closest attack space
        Optional<Path<Terrain>> oPath;

        if (taunters.size() > 0) {
            oPath = taunters.stream().filter(p -> p.getTeam(true) == 0).map(p ->
                    terrainPathFinder.findTarget(scene.getActivePawn().getParent(), p.getParent()))
                    .filter(Objects::nonNull)
                    .min((Comparator.comparingInt(Path::getCost)));
        } else {
            if (scene.getActivePawn().canAttack() && scene.getActivePawn().isReady()) {
//              if it can attack still, get closer
                System.out.println("Move Toward");
                oPath = attackableUnblockedTerrain
                        .get(scene
                                .getActivePawn())
                        .values()
                        .stream()
                        .filter(Objects::nonNull)
                        .min((Comparator.comparingInt(Path::getCost)));
                System.out.println("Blocked? " + !oPath.isPresent());

                if (!oPath.isPresent()) {
                    oPath = attackableTerrain
                            .get(scene
                                    .getActivePawn())
                            .values()
                            .stream()
                            .filter(Objects::nonNull)
                            .min((Comparator.comparingInt(Path::getCost)));
                    System.out.println("Blocked 2? " + !oPath.isPresent());

                }
            } else if (scene.getActivePawn().canMove() && scene.getActivePawn().isReady()) {
                // move further away (to least blocking space)
                System.out.println("Move Away");

                HashMap<Terrain, Path<Terrain>> terrainOptions = terrainPathFinder.findDistance(scene.getActivePawn().getParent(), scene.getActivePawn().getRemainingMovement());

                Collection<Terrain> terrainOptionCollection = new ArrayList<>(terrainOptions.keySet());
                terrainOptionCollection.add(scene.getActivePawn().getParent());

                int smallestBlockers = Integer.MAX_VALUE;
                oPath = Optional.empty();

                for (Terrain end : terrainOptionCollection) {
                    AtomicInteger blockers = new AtomicInteger(0);

                    // check ally path
                    attackableTerrain.keySet().stream()
                            .filter(p -> p != scene.getActivePawn())
                            .forEach(p -> attackableTerrain.get(p).values().forEach(path -> {
                                if (path != null &&
                                        path.contains(end) &&
                                        path.getCostTo(end) <= p.getRemainingMovement()) {
                                    blockers.addAndGet(2);
                                    System.out.println("Blocked Zone");
                                }

                            }));
                    attackableUnblockedTerrain.keySet().stream()
                            .filter(p -> p != scene.getActivePawn())
                            .forEach(p -> attackableUnblockedTerrain.get(p).values().forEach(path -> {
                                if (path != null &&
                                        path.contains(end) &&
                                        path.getCostTo(end) <= p.getRemainingMovement()) {
                                    blockers.addAndGet(3);
                                    System.out.println("Unblocked Zone");
                                }
                            }));

                    // check were enemies are
                    getAllEnemyPawns().forEach(e -> {
                        Collection<Terrain> dangerTerrain = enemyAttackTerrain.get(e);

                        if (dangerTerrain != null && dangerTerrain.contains(end)) {
                            blockers.addAndGet(6);
                            System.out.println("Danger Zone");
                        }
                    });

                    System.out.println(end.getMapX() + ", " + end.getMapY() + ": " + blockers.get());

                    if (blockers.get() < smallestBlockers || (scene.getActivePawn().getParent() == end && blockers.get() == smallestBlockers)) {
                        smallestBlockers = blockers.get();
                        Path<Terrain> selectedPath = terrainOptions.get(end);
                        if (selectedPath != null)
                            oPath = Optional.of(selectedPath);
                        else {
                            oPath = Optional.empty();
                        }
                    }
                }

            } else {
                // if it can't move or attack, end
                System.out.println("None");
                oPath = Optional.empty();
            }
        }

        if (!oPath.isPresent()) {
            System.out.println("No Path");
            return;
        }

        // find the furthest it can go
        Path<Terrain> closestPath = oPath.get();
        Terrain end = closestPath.getLast().getNode();
        System.out.println(end.getMapX() + ", " + end.getMapY());

        int dist = -1;
        for (PathAdjacentNode<Terrain> a : closestPath) {
            if (a.getNode().getPawn() == null || a.getNode().getPawn() == scene.getActivePawn()) {
                dist += 1;
                if (dist == scene.getActivePawn().getRemainingMovement()) break;
            } else break;
        }

        System.out.println("D: " + dist + ", " + closestPath.size());
        if (dist < closestPath.size()) {
            closestPath.subList(dist + 1, closestPath.size()).clear();
        }

        if (closestPath.size() == 0) {
            System.out.println("Map Size 0");
            return;
        }

        System.out.println("Map Size " + closestPath.size());

        moving = true;
        currentPath = closestPath;

        System.out.println(moving);
    }

    private void clean() {
        System.out.println("Clean");
        currentPath = null;
        moving = false;
        attack = null;
        select = null;
        attackableTerrain.clear();
        attackableUnblockedTerrain.clear();
        enemyAttackTerrain.clear();
    }

    public void resolve() {
        System.out.println("" + attack + " " + moving + " " + select);
        if (attack != null) {
            scene.setTargetPawn(attack.getPawn());
//            scene.getActivePawn().setReady(false);
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
