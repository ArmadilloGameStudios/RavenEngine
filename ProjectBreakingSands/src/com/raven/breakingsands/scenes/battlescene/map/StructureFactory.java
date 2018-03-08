package com.raven.breakingsands.scenes.battlescene.map;

import com.raven.engine.GameEngine;
import com.raven.engine.database.GameData;
import com.raven.engine.database.GameDataList;
import com.raven.engine.database.GameDataQuery;
import com.raven.engine.database.GameDatabase;
import com.raven.engine.util.Factory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StructureFactory extends Factory<Structure> {

    private Map map;

    private Structure connectedStructure;
    private StructureEntrance connectedEntrance;

    private GameDataList connectionPossibleNames;

    public StructureFactory(Map map) {
        this.map = map;
    }

    public void connection(Structure s, StructureEntrance e) {
        connectedStructure = s;
        connectedEntrance = e;
        connectionPossibleNames = new GameDataList();

        for (GameData con : GameDatabase.all("connections")) {
            if (con.getList("a").stream().anyMatch(gd ->
                    gd.getString("name").equals(connectedStructure.getName()) &&
                    gd.getString("entrance").equals(connectedEntrance.getName()))) {
                connectionPossibleNames.addAll(con.getList("b"));
            } else if (con.getList("b").stream().anyMatch(gd ->
                    gd.getString("name").equals(connectedStructure.getName()) &&
                    gd.getString("entrance").equals(connectedEntrance.getName()))) {
                connectionPossibleNames.addAll(con.getList("a"));
            }
        }

        System.out.println(connectionPossibleNames);
    }

    @Override
    public Structure getInstance() {

        if (connectedStructure == null) {
            return new Structure(map.getScene(), 0, 0);
        } else {
            GameDataList gdlPossibleStructure = new GameDataList();

            for (GameData row : GameEngine.getEngine().getGameDatabase().getTable("structure")) {

                List<GameData> namedConnections = connectionPossibleNames.stream()
                        .filter(con ->
                                con.getString("name").equals(row.getString("name")))
                        .collect(Collectors.toList());

                if (namedConnections.size() > 0) {

                    GameDataList entrances = new GameDataList(getEntrances(row)
                            .filter(e -> namedConnections.stream().anyMatch(con ->
                                    con.getString("entrance").equals(e.getString("name"))))
                            .collect(Collectors.toList()));

                    if (entrances.size() > 0) {
                        gdlPossibleStructure.add(row);

                        row.addList("valid entrances", entrances);
                    }
                }

            }

            GameData gdStructure = gdlPossibleStructure.getRandom();
            GameData gdEntrance = gdStructure.getList("valid entrances").getRandom();

            int r = (connectedStructure.getMapRotation());

            int x = 0;
            int y = 0;

            System.out.println(connectedEntrance.getSide());

            int entranceSide = gdEntrance.getInteger("side");

            System.out.println(gdEntrance);

            switch ((r + connectedEntrance.getSide()) % 4) {
                default:
                case 0:
                    x = connectedEntrance.getLocation();

                    if (entranceSide % 2 == 0) {
                        y = -gdStructure.getInteger("height");
                        x -= gdStructure.getInteger("width") -
                                gdEntrance.getInteger("location") -
                                gdEntrance.getInteger("length");
                    } else {
                        y = -gdStructure.getInteger("width");
                        x -= gdStructure.getInteger("height") -
                                gdEntrance.getInteger("location") -
                                gdEntrance.getInteger("length");
                    }
                    break;
                case 1:
                    x = connectedStructure.getWidth();
                    y = connectedEntrance.getLocation();

                    if (entranceSide % 2 == 0) {
                        y -= gdStructure.getInteger("width") -
                                gdEntrance.getInteger("location") -
                                gdEntrance.getInteger("length");
                    } else {
                        y -= gdStructure.getInteger("height") -
                                gdEntrance.getInteger("location") -
                                gdEntrance.getInteger("length");
                    }
                    break;
                case 2:
                    y = connectedStructure.getHeight();
                    x = connectedStructure.getWidth() - connectedEntrance.getLocation() - connectedEntrance.getLength();

                    if (entranceSide % 2 == 0) {
                        x -= gdEntrance.getInteger("location");
                    } else {
                        x -= gdEntrance.getInteger("location");
                    }
                    break;
                case 3:
                    y = connectedStructure.getHeight() - connectedEntrance.getLocation() - connectedEntrance.getLength();

                    if (entranceSide % 2 == 0) {
                        x = -gdStructure.getInteger("height");
                        y -= gdEntrance.getInteger("location");
                    } else {
                        x = -gdStructure.getInteger("width");
                        y -= gdEntrance.getInteger("location");
                    }
                    break;
            }

            x += connectedStructure.getMapX();
            y += connectedStructure.getMapY();

            r = (6 + connectedStructure.getMapRotation() +
                    connectedEntrance.getSide() -
                    gdEntrance.getInteger("side")) % 4;

            System.out.println(r);

            return new Structure(
                    map.getScene(),
                    gdStructure,
                    gdEntrance,
                    r, x, y);
        }
    }

    private Stream<GameData> getEntrances(GameData data) {
        return data.getList("entrance").stream()
                .filter(d -> d.getInteger("length") == connectedEntrance.getLength());
    }

    @Override
    public void clear() {
        connectedStructure = null;
        connectedEntrance = null;
    }
}
