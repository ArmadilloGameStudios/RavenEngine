package com.armadillogamestudios.reclaim.data;

import com.armadillogamestudios.engine2d.database.GameData;
import com.armadillogamestudios.engine2d.database.GameDatabase;
import com.armadillogamestudios.engine2d.database.GameDatable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Settlement implements GameDatable {

    private final List<GameData> recruitableUnits;
    private final List<Unit> units = new ArrayList<>();
    private final boolean capital;
    private final String name;
    private final String sprite;
    private final Player owner;
    private Region region;
    private GameData recruiting = null;
    private int recruitTime = 0;
    private List<Region> patrolRegions = null;
    private int maxUnitCount;

    public Settlement(GameData gameData, boolean capital, Player owner) {

        this.name = gameData.getString("name");
        this.sprite = gameData.getString("sprite");
        this.recruitableUnits = gameData.getList("units").stream()
                .map(GameData::asString)
                .distinct()
                .map(gd -> GameDatabase.all("unit").query("name", gd))
                .collect(Collectors.toList());
        this.maxUnitCount = gameData.getInteger("unit_count");

        this.capital = capital;
        this.owner = owner;

        if (capital) {
            owner.setCapital(this);
        }
    }

    @Override
    public GameData toGameData() {
        return null;
    }

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public String getSprite() {
        return sprite;
    }

    public String getName() {
        return name;
    }

    public List<Region> getPatrolRegions() {
        if (patrolRegions == null) {
            patrolRegions = region.getRegionConnections().stream()
                    .flatMap(rc -> rc.getOther(region).getRegionConnections().stream())
                    .flatMap(rc -> rc.asList().stream())
                    .distinct()
                    .collect(Collectors.toList());
        }

        return patrolRegions;
    }

    public void tick() {
        if (recruiting != null) {
            recruitTime += 1;

            if (recruitTime >= recruiting.getInteger("time")) {
                new Unit(this, this.getRegion().getWorld(), recruiting);
                recruiting = null;
                recruitTime = 0;
            }
        }
    }

    public List<GameData> getRecruitableUnits() {
        return recruitableUnits;
    }

    public Player getOwner() {
        return owner;
    }

    public GameData getRecruiting() {
        return recruiting;
    }

    public void setRecruiting(GameData recruiting) {
        this.recruiting = recruiting;
    }

    public int getRecruitTime() {
        return recruitTime;
    }

    public List<Unit> getUnits() {
        return units;
    }

    public int getMaxUnitCount() {
        return maxUnitCount;
    }
}