package com.armadillogamestudios.reclaim.scene.world;

import com.armadillogamestudios.engine2d.database.GameData;
import com.armadillogamestudios.engine2d.input.MouseClickHandler;
import com.armadillogamestudios.engine2d.ui.UIFont;
import com.armadillogamestudios.engine2d.ui.UILabel;
import com.armadillogamestudios.engine2d.ui.container.UIContainer;
import com.armadillogamestudios.engine2d.worldobject.Highlight;
import com.armadillogamestudios.reclaim.ReclaimGame;
import com.armadillogamestudios.reclaim.data.*;
import com.armadillogamestudios.tactics.gameengine.scene.map.MapScene;
import com.armadillogamestudios.tactics.gameengine.scene.map.TileMap;

import java.util.ArrayList;
import java.util.List;

public class ReclaimWorldScene extends MapScene<ReclaimWorldScene, ReclaimGame, Region, Unit> {

    private static final int ticksPerDay = 24;
    private final static int speedPixelLength = 40;
    private final ReclaimActiveGameData data;
    private final Highlight activeSpeed = new Highlight(0.3f, 0.4f, 1.0f, 1.0f);
    private final Highlight inactiveSpeed = new Highlight(1.0f, 1.0f, 1.0f, 1.0f);
    private final List<RecruitButton> recruitBtns = new ArrayList<>();
    private TileMap<ReclaimWorldScene, Region, Unit> map;
    private UILabel<ReclaimWorldScene> gold, terrain, building, units, recruiting;
    private UILabel<ReclaimWorldScene> pause, slow, normal, fast, fastest;
    private UILabel<ReclaimWorldScene> dayTxtUI, hourTxtUI;
    private UILabel<ReclaimWorldScene> unitName;
    private int ticksSinceLastDay = 0;
    private Region selectedRegion;
    private Unit selectedUnit;

    public ReclaimWorldScene(ReclaimGame game) {
        super(game);

        data = ReclaimGame.getActiveGameData();
    }

    @Override
    protected void loadUI() {
        map = new TileMap<>(this, data.getWorld().getRegions());
        super.loadUI();

        // Terrain/Building
        UIContainer<ReclaimWorldScene> terrainBuildingDetailsContainer = new UIContainer<>(this, UIContainer.Location.BOTTOM_RIGHT, UIContainer.Layout.VERTICAL);
        addChild(terrainBuildingDetailsContainer);

        terrain = new UILabel<>(this, "", 100, 10);
        terrain.load();
        terrainBuildingDetailsContainer.addChild(terrain);

        UIFont font = terrain.getFont();
        font.setSmall(true);

        terrain.setZ(.5f);

        building = new UILabel<>(this, "", 100, 10);
        building.load();
        terrainBuildingDetailsContainer.addChild(building);

        font = building.getFont();
        font.setSmall(true);

        building.setZ(.5f);

        units = new UILabel<>(this, "", 100, 10);
        units.load();
        terrainBuildingDetailsContainer.addChild(units);

        font = units.getFont();
        font.setSmall(true);

        units.setZ(.5f);

        recruiting = new UILabel<>(this, "", 100, 10);
        recruiting.load();
        terrainBuildingDetailsContainer.addChild(recruiting);

        font = recruiting.getFont();
        font.setSmall(true);

        recruiting.setZ(.5f);

        terrainBuildingDetailsContainer.pack();

        // Recruit
        UIContainer<ReclaimWorldScene> recruitContainer = new UIContainer<>(this, UIContainer.Location.BOTTOM, UIContainer.Layout.HORIZONTAL);
        addChild(recruitContainer);

        for (int i = 0; i < 4; i++) {
            RecruitButton recruit = new RecruitButton(this);
            recruit.load();
            recruitContainer.addChild(recruit);

            recruit.setZ(.5f);

            recruitBtns.add(recruit);
        }

        recruitContainer.pack();

        // Resources
        UIContainer<ReclaimWorldScene> resourcesContainer = new UIContainer<>(this, UIContainer.Location.UPPER_LEFT, UIContainer.Layout.HORIZONTAL);
        addChild(resourcesContainer);

        gold = new UILabel<>(this, "", 100, 10);
        gold.load();
        resourcesContainer.addChild(gold);

        font = gold.getFont();
        font.setY(2);
        font.setX(2);
        font.setSmall(true);

        gold.setZ(.5f);

        resourcesContainer.pack();

        // Controls
        UIContainer<ReclaimWorldScene> controlsContainer = new UIContainer<>(this, UIContainer.Location.UPPER_RIGHT, UIContainer.Layout.HORIZONTAL);
        addChild(controlsContainer);

        pause = new UILabel<>(this, "pause", speedPixelLength, 20);
        pause.load();
        controlsContainer.addChild(pause);

        pause.addMouseHandler((MouseClickHandler) () -> {
            setPausedTick(!getPausedTick());
        });

        font = pause.getFont();
        font.setY(2);
        font.setSmall(true);

        slow = new UILabel<>(this, "slow", speedPixelLength, 20);
        slow.load();
        controlsContainer.addChild(slow);

        slow.addMouseHandler((MouseClickHandler) () -> {
            setSpeed(Speed.Slow);
        });

        font = slow.getFont();
        font.setY(2);
        font.setSmall(true);

        normal = new UILabel<>(this, "normal", speedPixelLength, 20);
        normal.load();
        controlsContainer.addChild(normal);

        normal.addMouseHandler((MouseClickHandler) () -> {
            setSpeed(Speed.Normal);
        });

        font = normal.getFont();
        font.setY(2);
        font.setSmall(true);

        fast = new UILabel<>(this, "fast", speedPixelLength, 20);
        fast.load();
        controlsContainer.addChild(fast);

        fast.addMouseHandler((MouseClickHandler) () -> {
            setSpeed(Speed.Fast);
        });

        font = fast.getFont();
        font.setY(2);
        font.setSmall(true);

        fastest = new UILabel<>(this, "fastest", speedPixelLength, 20);
        fastest.load();
        controlsContainer.addChild(fastest);

        fastest.addMouseHandler((MouseClickHandler) () -> {
            setSpeed(Speed.Fastest);
        });

        font = fastest.getFont();
        font.setY(2);
        font.setSmall(true);

        controlsContainer.pack();

        // Unit
        UIContainer<ReclaimWorldScene> unitContainer = new UIContainer<>(this, UIContainer.Location.BOTTOM_LEFT, UIContainer.Layout.VERTICAL);
        addChild(unitContainer);

        unitName = new UILabel<>(this, "", 100, 10);
        unitName.load();
        unitContainer.addChild(unitName);

        font = unitName.getFont();
        font.setY(2);
        font.setSmall(true);

        unitContainer.pack();


        // Day/Time
        UIContainer<ReclaimWorldScene> dayTimeContainer = new UIContainer<>(this, UIContainer.Location.UPPER, UIContainer.Layout.VERTICAL);
        addChild(terrainBuildingDetailsContainer);

        dayTxtUI = new UILabel<>(this, "", 40, 10);
        dayTxtUI.load();
        dayTimeContainer.addChild(dayTxtUI);

        font = dayTxtUI.getFont();
        font.setY(2);
        font.setSmall(true);

        hourTxtUI = new UILabel<>(this, "", 40, 10);
        hourTxtUI.load();
        dayTimeContainer.addChild(hourTxtUI);

        font = hourTxtUI.getFont();
        font.setY(2);
        font.setSmall(true);

        dayTimeContainer.pack();

        // Init
        updatePlayPause();
        updateUI();

        // Load Units/Tiles
        data.getWorld().getUnits().forEach(unit -> unit.load(this));

        // Focus
        focus(data.getPlayers().get(0).getCapital().getRegion());
    }

    @Override
    protected void updateUI() {

        Player active = data.getPlayers().get(0);
        gold.setText(Integer.toString(active.getGold()));
        gold.load();

        World world = data.getWorld();
        dayTxtUI.setText("Day " + world.getDay());
        dayTxtUI.load();

        hourTxtUI.setText("Hour " + world.getHour());
        hourTxtUI.load();

        updateSettlementUI();
        updateUnitUI();
    }

    private void updateSettlementUI() {
        if (selectedRegion == null) {
            terrain.setVisibility(false);
            building.setVisibility(false);
            units.setVisibility(false);
            recruiting.setVisibility(false);

            recruitBtns.forEach(recruitButton -> recruitButton.setVisibility(false));

            return;
        }

        terrain.setVisibility(true);
        terrain.setText(selectedRegion.getBiome().getName());
        terrain.load();

        if (selectedRegion.getSettlement() != null) {
            building.setText(selectedRegion.getSettlement().getName());
            building.setVisibility(true);
            building.load();

            units.setText("Units " +
                    selectedRegion.getSettlement().getUnits().size() +
                    "/" +
                    selectedRegion.getSettlement().getMaxUnitCount());
            units.setVisibility(true);
            units.load();

            GameData recruitGd = selectedRegion.getSettlement().getRecruiting();
            if (recruitGd != null) {
                recruiting.setText(recruitGd.getString("name") +
                        ": " +
                        selectedRegion.getSettlement().getRecruitTime() +
                        "/" +
                        recruitGd.getInteger("time"));

                recruiting.setVisibility(true);
                recruiting.load();
            } else {
                recruiting.setVisibility(false);
            }


            List<GameData> units = selectedRegion.getSettlement().getRecruitableUnits();

            for (int i = 0; i < recruitBtns.size(); i++) {
                if (i < units.size()) {
                    recruitBtns.get(i).setRecruit(selectedRegion, units.get(i));
                    recruitBtns.get(i).setVisibility(true);

                    if (Unit.recruitRequirementsMet(units.get(i), selectedRegion.getSettlement()))
                        recruitBtns.get(i).setHighlight(RecruitButton.CanAfford);
                    else
                        recruitBtns.get(i).setHighlight(RecruitButton.CanNotAfford);

                } else {
                    recruitBtns.get(i).setVisibility(false);
                }
                recruitBtns.get(i).load();
            }

        } else {
            building.setVisibility(false);
            recruiting.setVisibility(false);
            units.setVisibility(false);

            for (int i = 0; i < recruitBtns.size(); i++) {
                recruitBtns.get(i).setVisibility(false);
                recruitBtns.get(i).load();
            }
        }
        building.load();
    }

    private void updateUnitUI() {
        if (selectedUnit == null) {
            unitName.setVisibility(false);

            return;
        }

        unitName.setVisibility(true);
        unitName.setText(selectedUnit.getName());
        unitName.load();
    }

    @Override
    protected void updatePlayPause() {
        slow.setHighlight(inactiveSpeed);
        normal.setHighlight(inactiveSpeed);
        fast.setHighlight(inactiveSpeed);
        fastest.setHighlight(inactiveSpeed);

        if (getPausedTick()) {
            pause.setHighlight(activeSpeed);
        } else {
            pause.setHighlight(inactiveSpeed);

            switch (getSpeed()) {
                case Slow -> {
                    slow.setHighlight(activeSpeed);
                }
                case Normal -> {
                    normal.setHighlight(activeSpeed);
                }
                case Fast -> {
                    fast.setHighlight(activeSpeed);
                }
                case Fastest -> {
                    fastest.setHighlight(activeSpeed);
                }
            }
        }
    }

    @Override
    protected void tick() {
        data.getWorld().tick();

        updateTick();

        ticksSinceLastDay += 1;
        if (ticksSinceLastDay >= ticksPerDay) {
            updateNightly();

            ticksSinceLastDay = 0;
        }

        updateUI();
    }

    private void updateTick() {
        // Movement, Fights, Spawn, etc
        data.getWorld().getUnits().forEach(Unit::tick);
    }

    private void updateNightly() {
        // Economy
        data.getPlayers().forEach(player -> {
            player.addGold(10);

            player.getCapital().tick();
        });
    }

    @Override
    protected synchronized TileMap<ReclaimWorldScene, Region, Unit> getTileMap() {
        return map;
    }

    @Override
    public void handleTileClick(Region tile) {
        selectedRegion = tile;
        selectedUnit = null;

        updateSettlementUI();
        updateUnitUI();
    }

    @Override
    public void handlePawnClick(Unit unit) {
        selectedRegion = null;
        selectedUnit = unit;

        updateSettlementUI();
        updateUnitUI();
    }
}
