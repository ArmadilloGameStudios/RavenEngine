package com.raven.breakingsands.scenes.battlescene.map;

import com.raven.engine2d.GameEngine;
import com.raven.engine2d.database.GameData;
import com.raven.engine2d.database.GameDataList;
import com.raven.engine2d.database.GameDatabase;
import com.raven.engine2d.util.Factory;
import org.lwjgl.system.CallbackI;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StructureFactory extends Factory<Structure> {

    private Map map;

    private Structure connectedStructure;
    private StructureEntrance connectedEntrance;

    private GameDataList connectionPossibleNames;
    private boolean closed;

    public StructureFactory(Map map) {
        this.map = map;
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

            while (gdlPossibleStructure.size() > 0) {

                GameData gdStructure = gdlPossibleStructure.getRandom();
                gdlPossibleStructure.remove(gdStructure);

                while (gdStructure.getList("valid entrances").size() > 0) {

                    GameData gdEntrance = gdStructure.getList("valid entrances").getRandom();
                    gdStructure.getList("valid entrances").remove(gdEntrance);

                    int x = 0;
                    int y = 0;

                    int entranceSide = connectedEntrance.getSide();

                    switch (entranceSide) {
                        case 0:
                            y -= gdStructure.getInteger("height");
                            x -= gdStructure.getInteger("width") -
                                    gdEntrance.getInteger("location") -
                                    gdEntrance.getInteger("length") +
                                    connectedEntrance.getLocation();
                            break;
                        case 1:
                            break;
                        case 2:
                            y += connectedStructure.getHeight();
                            x += connectedStructure.getWidth() -
                                    connectedEntrance.getLocation() -
                                    connectedEntrance.getLength() +
                                    gdEntrance.getInteger("location");
                            break;
                        case 3:
                            break;
                    }

                    System.out.println("Entrance Side " + entranceSide);
                    System.out.println(connectedStructure.getName());
                    System.out.println(connectedStructure.getMapX());
                    System.out.println(connectedStructure.getMapY());
                    System.out.println(connectedStructure.getWidth());
                    System.out.println(connectedStructure.getHeight());

                    x += connectedStructure.getMapX();
                    y += connectedStructure.getMapY();

                    Structure s = new Structure(
                            map.getScene(),
                            gdStructure,
                            gdEntrance,
                            x, y);

                    System.out.println(s.getName());
                    System.out.println(s.getMapX());
                    System.out.println(s.getMapY());
                    System.out.println(s.getWidth());
                    System.out.println(s.getHeight());

                    // Check collision
                    List<Structure> structures = map.getStructures();

                    boolean safe = true;

                    for (Structure structure : structures) {

                        if (s.overlaps(structure)) {
                            safe = false;
                            break;
                        }
                    }

                    if (!safe) {
                        System.out.println("Not Safe: " + s.getName());
                        continue;
                    }

                    System.out.println("S Count: " + structures.size());

                    // check if entrances match
                    for (Structure structure : structures) {
                        s.tryConnect(structure);
                    }

                    return s;
                }
            }
        }

        return null;
    }

    private Stream<GameData> getEntrances(GameData data) {
        return data.getList("entrance").stream()
                .filter(d -> d.getInteger("length") == connectedEntrance.getLength());
    }

    @Override
    public void clear() {
        connectedStructure = null;
        connectedEntrance = null;
        closed = false;
    }

    public void setConnection(Structure s, StructureEntrance e) {
        connectedStructure = s;
        connectedEntrance = e;
        connectionPossibleNames = new GameDataList();

        for (GameData con : GameDatabase.all("connections").stream()
                .filter(c -> c.getBoolean("closed") == closed)
                .collect(Collectors.toList())) {
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
    }

    public void setClosed(boolean closed) {
        this.closed = closed;
    }
}
