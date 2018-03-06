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
            GameData entranceData = list.get(i);

            int r = connectedStructure.getMapRotation() +
                    connectedEntrance.getSide() -
                    entranceData.getInteger("side");

            r = connectedStructure.getMapRotation() +
                    connectedEntrance.getSide() +
                    entranceData.getInteger("side");

            if (r < 0) {
                r += 4;
            } else if (r >= 4) {
                r -= 4;
            }

            if (r < 0) {
                r += 4;
            } else if (r >= 4) {
                r -= 4;
            }

            System.out.println(r);

            int x = 0;
            int y = 0;

            System.out.println(connectedEntrance.getSide());

            int entranceSide = entranceData.getInteger("side");

            System.out.println(entranceSide);

            switch (connectedEntrance.getSide()) {
                default:
                case 0:
                    x = connectedEntrance.getLocation() -
                            entranceData.getInteger("location");

                    if (entranceSide == 1 || entranceSide == 3)
                        y = connectedStructure.getWidth();
                    else
                        y = connectedStructure.getHeight();
                    break;
                case 1:
                    if (entranceSide == 1 || entranceSide == 3)
                        x = connectedStructure.getWidth();
                    else
                        x = connectedStructure.getHeight();

                    y = connectedEntrance.getLocation() -
                            entranceData.getInteger("location");
                    break;
                case 2:
                    x = connectedEntrance.getLocation() -
                            entranceData.getInteger("location");

                    if (entranceSide == 1 || entranceSide == 3)
                        y = -gdStructure.getInteger("width");
                    else
                        y = -gdStructure.getInteger("height");
                    break;
                case 3:
                    if (entranceSide == 1 || entranceSide == 3)
                        x = -gdStructure.getInteger("width");
                    else
                        x = -gdStructure.getInteger("height");

                    y = connectedEntrance.getLocation() -
                            entranceData.getInteger("location");
                    break;
            }

            System.out.println("X: " + x + ", Y: " + y);

            switch (r) {
                default:
                case 0:
                    return new Structure(
                            map.getScene(),
                            gdStructure,
                            r, x, y);
                case 1:
                    return new Structure(
                            map.getScene(),
                            gdStructure,
                            r, x, y);
                case 2:
                    return new Structure(
                            map.getScene(),
                            gdStructure,
                            r, x, y);
                case 3:
                    return new Structure(
                            map.getScene(),
                            gdStructure,
                            r, x, y);
            }
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
