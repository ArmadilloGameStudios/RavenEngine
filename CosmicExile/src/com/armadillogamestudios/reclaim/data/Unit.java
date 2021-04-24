package com.armadillogamestudios.reclaim.data;

import com.armadillogamestudios.engine2d.database.GameData;
import com.armadillogamestudios.engine2d.database.GameDatable;
import com.armadillogamestudios.engine2d.util.pathfinding.Path;
import com.armadillogamestudios.engine2d.util.pathfinding.PathFinder;
import com.armadillogamestudios.reclaim.ReclaimGame;
import com.armadillogamestudios.reclaim.scene.world.RegionConnection;
import com.armadillogamestudios.reclaim.util.UnitPathInterpreter;
import com.armadillogamestudios.tactics.gameengine.scene.map.Pawn;

import java.util.*;
import java.util.stream.Collectors;

public class Unit extends Pawn implements GameDatable {

    private final Settlement base;
    private final String name;
    private final Player player;
    private final StanceManager stanceManager;
    private final String stance;
    private final Map<String, Integer> travelDifficulty = new HashMap<>();
    private final UnitPathInterpreter interpreter;
    private Region region;

    public Unit(Settlement settlement, World world, GameData gameData) {

        name = gameData.getString("name");
        stance = gameData.getString("stance");

        gameData.getData("speed").asMap().forEach((k, v) -> {
            travelDifficulty.put(k, v.asInteger());
        });

        base = settlement;
        settlement.getUnits().add(this);

        this.region = settlement.getRegion();
        region.addUnit(this);
        region.setInCognito(false);

        this.player = settlement.getOwner();
        player.addUnit(this);

        world.addUnit(this);

        switch (stance) {
            case "patrol" -> stanceManager = new PatrolStanceManager();
            case "explore" -> stanceManager = new RandomWanderStanceManager();
            default -> throw new IllegalStateException("Unexpected stance value: " + stance);
        }

        interpreter = new UnitPathInterpreter(this);
    }

    public static boolean recruitRequirementsMet(GameData gameData, Settlement settlement) {
        return settlement.getUnits().size() < settlement.getMaxUnitCount() &&
                gameData.getInteger("cost") <= settlement.getOwner().getGold() &&
                settlement.getRecruiting() == null;
    }

    public String getSprite() {
        return "unit base.png";
    }

    @Override
    public GameData toGameData() {
        return null;
    }

    public void tick() {
        stanceManager.tick();
    }

    public Player getPLayer() {
        return player;
    }

    public String getName() {
        return name;
    }

    public int getTravelDifficulty(String biome) {
        if (travelDifficulty.containsKey(biome))
            return travelDifficulty.get(biome);

        throw new IllegalArgumentException();
    }

    private abstract static class StanceManager {

        public abstract void tick();
    }

    private class RandomWanderStanceManager extends StanceManager {

        private int steps = 0;
        private Region targetRegion;
        private int speed = 0;

        public void tick() {
            if (targetRegion == null) {
                List<RegionConnection> adj = region.getRegionConnections();

                targetRegion = adj.get(ReclaimGame.RANDOM.nextInt(adj.size())).getOther(region);
                speed = travelDifficulty.get(targetRegion.getBiome().getName());
            }

            steps++;

            if (steps >= speed) {
                region.removeUnit(Unit.this);
                region = targetRegion;
                region.addUnit(Unit.this);
                region.setInCognito(false);

                targetRegion = null;
                steps = 0;
            }
        }
    }

    private class PatrolStanceManager extends StanceManager {

        // make more uniform

        private final PathFinder<Region> pathFinder = new PathFinder<>();

        private int steps = 0, pathIndex = 1;
        private Region targetRegion, nextRegion;
        private Path<Region> path;
        private int speed = 0;

        public void tick() {
            if (targetRegion == null) {
                List<Region> potential = base.getPatrolRegions().stream()
                        .filter(r -> !r.equals(region))
                        .sorted(
                                Comparator.comparingInt((Region r) -> r.getLastTimePlayerVisited(player))
                                        .thenComparingInt((Region r) -> pathFinder.findTarget(r, Collections.singletonList(Unit.this.region), interpreter).getCost()))
                        .collect(Collectors.toList());

                targetRegion = potential.get(0);

                path = pathFinder.findTarget(region, Collections.singletonList(targetRegion), interpreter);

                path.forEach(n -> n.getNode().triggerLastTimePlayerVisited(Unit.this.getPLayer()));

                nextRegion = path.get(pathIndex).getNode();
                speed = travelDifficulty.get(nextRegion.getBiome().getName());
            }

            steps++;

            if (steps >= speed) {

                region.removeUnit(Unit.this);
                region = nextRegion;
                region.addUnit(Unit.this);
                region.setInCognito(false);

                if (targetRegion == nextRegion) {
                    targetRegion = null;
                    pathIndex = 1;
                } else {
                    pathIndex++;
                    nextRegion = path.get(pathIndex).getNode();
                }

                steps = 0;
            }
        }
    }
}
