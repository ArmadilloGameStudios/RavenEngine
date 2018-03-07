package com.raven.breakingsands.scenes.battlescene.map;

import com.raven.engine.GameEngine;
import com.raven.engine.database.GameData;
import com.raven.engine.database.GameDataQuery;
import com.raven.engine.util.Factory;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StructureFactory extends Factory<Structure> {

    private Map map;

    private Structure connectedStructure;
    private StructureEntrance connectedEntrance;

    public StructureFactory(Map map) {
        this.map = map;
    }

    public void connection(Structure s, StructureEntrance e) {
        connectedStructure = s;
        connectedEntrance = e;
    }

    @Override
    public Structure getInstance() {
        System.out.println("getInstance()");

        if (connectedStructure == null) {
            return new Structure(map.getScene(), 0, 0);
        } else {
            GameData gdStructure = GameEngine.getEngine().getGameDatabase().getTable("structure").queryRandom(
                    new GameDataQuery() {
                        @Override
                        public boolean matches(GameData row) {
                            Optional<GameData> entrance = getEntrances(row).findAny();

                            return entrance.isPresent();
                        }
                    }
            );

            List<GameData> list = getEntrances(gdStructure).collect(Collectors.toList());

            int i = map.getScene().getRandom().nextInt(list.size());
            GameData gdEntrance = list.get(i);

            int r = (connectedStructure.getMapRotation());

            System.out.println("Struct Rot: " + r);

            int x = 0;
            int y = 0;

            System.out.println(connectedEntrance.getSide());

            int entranceSide = gdEntrance.getInteger("side");

            System.out.println(gdEntrance);

            switch ((r + connectedEntrance.getSide()) % 4) {
                default:
                case 0:
                    System.out.println("Case 0");
                    x = connectedEntrance.getLocation();

                    System.out.println("X: " + x + ", Y: " + y);

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
                    System.out.println("Case 1");
                    x = connectedStructure.getWidth();
                    y = connectedEntrance.getLocation();

                    System.out.println("X: " + x + ", Y: " + y);

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
                    System.out.println("Case 2");
                    y = connectedStructure.getHeight();
                    x = connectedStructure.getWidth() - connectedEntrance.getLocation() - connectedEntrance.getLength();

                    System.out.println("X: " + x + ", Y: " + y);

                    if (entranceSide % 2 == 0) {
                        x -= gdEntrance.getInteger("location");
                    } else {
                        x -= gdEntrance.getInteger("location");
                    }
                    break;
                case 3:
                    System.out.println("Case 3");
                    y = connectedStructure.getHeight() - connectedEntrance.getLocation() - connectedEntrance.getLength();

                    System.out.println("X: " + x + ", Y: " + y);

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

            System.out.println("X: " + x + ", Y: " + y);

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
