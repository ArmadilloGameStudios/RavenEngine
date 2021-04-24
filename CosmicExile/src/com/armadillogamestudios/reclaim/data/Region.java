package com.armadillogamestudios.reclaim.data;

import com.armadillogamestudios.engine2d.database.GameData;
import com.armadillogamestudios.engine2d.database.GameDatable;
import com.armadillogamestudios.reclaim.scene.world.RegionConnection;
import com.armadillogamestudios.tactics.gameengine.scene.map.Tile;

import java.util.*;

public class Region extends Tile<Unit> implements GameDatable {

    private final int x, y;
    private final World world;
    private final List<Unit> units = new ArrayList<>();
    private final Map<Player, Integer> lastVisited = new HashMap<>();
    private final int biomeValue;
    private boolean impassable = false, inCognito = true;
    private String name;
    private Biome biome;
    private RegionConnection left, right, topLeft, topRight, botLeft, botRight;
    private Settlement settlement;
    private List<RegionConnection> adjacent;

    public Region(World world, int biomeValue, int x, int y) {
        this.x = x;
        this.y = y;

        this.biomeValue = biomeValue;
        this.world = world;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public GameData toGameData() {
        return null;
    }

    public void setTopLeftAdjacent(Region region) {
        region.botRight = topLeft = new RegionConnection(this, region);
    }

    public void setTopRightAdjacent(Region region) {
        region.botLeft = topRight = new RegionConnection(this, region);
    }

    public void setBottomLeftAdjacent(Region region) {
        region.topRight = botLeft = new RegionConnection(this, region);
    }

    public void setBottomRightAdjacent(Region region) {
        region.topLeft = botRight = new RegionConnection(this, region);
    }

    public void setLeftAdjacent(Region region) {
        region.right = left = new RegionConnection(this, region);
    }

    public void setRightAdjacent(Region region) {
        region.left = right = new RegionConnection(this, region);
    }

    public RegionConnection getTopRightConnection() {
        return topRight;
    }

    public RegionConnection getRightConnection() {
        return right;
    }

    public RegionConnection getBotRightConnection() {
        return botRight;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getBiomeValue() {
        if (impassable)
            return biomeValue + 1000;

        return biomeValue;
    }

    @Override
    public String toString() {
        return name + " (" + x + "," + y + ")";
    }

    public List<RegionConnection> getRegionConnections() {
        if (adjacent == null) {
            adjacent = new ArrayList<>();

            addRCIfNotNull(adjacent, left);
            addRCIfNotNull(adjacent, right);
            addRCIfNotNull(adjacent, topLeft);
            addRCIfNotNull(adjacent, topRight);
            addRCIfNotNull(adjacent, botLeft);
            addRCIfNotNull(adjacent, botRight);
        }

        return adjacent;
    }

    private void addRCIfNotNull(List<RegionConnection> adjacent, RegionConnection r) {
        if (r != null)
            adjacent.add(r);
    }

    public boolean isImpassable() {
        return impassable;
    }

    public void setImpassable(boolean impassable) {
        this.impassable = impassable;
    }

    public RegionConnection getConnection(Region region) {
        if (left != null && left.getOther(this) == region) {
            return left;
        } else if (right != null && right.getOther(this) == region) {
            return right;
        } else if (topLeft != null && topLeft.getOther(this) == region) {
            return topLeft;
        } else if (topRight != null && topRight.getOther(this) == region) {
            return topRight;
        } else if (botLeft != null && botLeft.getOther(this) == region) {
            return botLeft;
        } else if (botRight != null && botRight.getOther(this) == region) {
            return botRight;
        } else {
            throw new IllegalArgumentException();
        }
    }

    public String getSprite() {

        if (settlement != null) {
            return settlement.getSprite();
        }

        return biome.getSprite();
    }

    public Biome getBiome() {
        return biome;
    }

    public void setBiome(Biome biome) {
        this.biome = biome;
    }

    public Settlement getSettlement() {
        return settlement;
    }

    public void setSettlement(Settlement settlement) {
        this.settlement = settlement;
        settlement.setRegion(this);
    }

    public void addUnit(Unit unit) {
        units.add(unit);

        lastVisited.put(unit.getPLayer(), world.getTime());
    }

    @Override
    public List<Unit> getPawns() {
        return units;
    }

    public void removeUnit(Unit unit) {
        units.remove(unit);
    }

    @Override
    public boolean isInCognito() {
        return inCognito;
    }

    public void setInCognito(boolean inCognito) {
        this.inCognito = inCognito;

        if (!inCognito)
            this.getRegionConnections().stream()
                    .map(regionConnection -> regionConnection.getOther(this))
                    .forEach(n -> n.inCognito = false);
    }

    public int getLastTimePlayerVisited(Player player) {
        return lastVisited.getOrDefault(player, 0);
    }

    public void triggerLastTimePlayerVisited(Player player) {
        lastVisited.put(player,  world.getTime());
    }

    public World getWorld() {
        return world;
    }
}
