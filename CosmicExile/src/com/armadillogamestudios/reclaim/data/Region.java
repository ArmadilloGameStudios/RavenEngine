package com.armadillogamestudios.reclaim.data;

import com.armadillogamestudios.engine2d.database.GameData;
import com.armadillogamestudios.engine2d.database.GameDatable;
import com.armadillogamestudios.engine2d.util.pathfinding.PathAdjacentNode;
import com.armadillogamestudios.engine2d.util.pathfinding.PathNode;
import com.armadillogamestudios.reclaim.scene.world.RegionConnection;
import com.armadillogamestudios.tactics.gameengine.scene.map.Tile;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class Region extends Tile implements GameDatable, PathNode<Region, Region.PathFlag> {

    private final int x, y;
    private boolean impassable = false;
    private String name;
    private int travelDifficulty = 1;
    private Biome biome = Biome.Planes;
    private RegionConnection left, right, topLeft, topRight, botLeft, botRight;
    private Building building;

    public Region(int x, int y) {
        this.x = x;
        this.y = y;
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

    public int getTravelDifficulty() {
        if (impassable)
            return travelDifficulty + 1000;

        return travelDifficulty;
    }

    @Override
    public String toString() {
        return name + " (" + x + "," + y + ")";
    }

    public List<RegionConnection> getAdjacentRegions() {
        List<RegionConnection> adjacent = new ArrayList<>();

        addRCIfNotNull(adjacent, left);
        addRCIfNotNull(adjacent, right);
        addRCIfNotNull(adjacent, topLeft);
        addRCIfNotNull(adjacent, topRight);
        addRCIfNotNull(adjacent, botLeft);
        addRCIfNotNull(adjacent, botRight);

        return adjacent;
    }

    @Override
    public List<PathAdjacentNode<Region>> getAdjacentNodes(EnumSet<PathFlag> flags) {
        if (flags.contains(PathFlag.Adjacent)) {
            List<PathAdjacentNode<Region>> adjacent = new ArrayList<>();

            addPANIfNotNull(adjacent, left);
            addPANIfNotNull(adjacent, right);
            addPANIfNotNull(adjacent, topLeft);
            addPANIfNotNull(adjacent, topRight);
            addPANIfNotNull(adjacent, botLeft);
            addPANIfNotNull(adjacent, botRight);

            return adjacent;
        }

        throw new IllegalArgumentException();
    }

    private void addRCIfNotNull(List<RegionConnection> adjacent, RegionConnection r) {
        if (r != null)
            adjacent.add(r);
    }

    private void addPANIfNotNull(List<PathAdjacentNode<Region>> adjacent, RegionConnection r) {
        if (r != null)
            adjacent.add(new PathAdjacentNode<>(r.getOther(this), r.getTravelDifficulty(PathFlag.Adjacent)));
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

        if (building != null) {
            return building.getSprite();
        }

        switch (biome) {
            case Planes -> {
                return "default map tile.png";
            }
            case Forrest -> {
                return "default sand map tile.png";
            }
            case Mountains -> {
                return "default path map tile.png";
            }
            default -> throw new IllegalStateException("Unexpected value: " + biome);
        }
    }

    @Override
    public void handleMouseClick() {

    }

    @Override
    public void handleMouseEnter() {

    }

    @Override
    public void handleMouseLeave() {

    }

    @Override
    public void handleMouseHover(float delta) {

    }

    @Override
    public EnumSet<PathFlag> getEmptyNodeEnumSet() {
        return EnumSet.noneOf(PathFlag.class);
    }

    public void setBiome(Biome biome) {
        this.biome = biome;
    }

    public void setBuilding(Building building) {
        this.building = building;
    }

    public enum PathFlag {
        Adjacent
    }

    public enum Biome {
        Planes, Forrest, Mountains
    }
}
