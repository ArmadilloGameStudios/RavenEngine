package com.raven.breakingsands.scenes.battlescene.map;

import com.raven.engine2d.database.GameData;
import com.raven.engine2d.util.Factory;
import com.raven.engine2d.util.math.Vector2i;

import java.util.List;
import java.util.stream.Collectors;

public class StructureFactory extends Factory<Structure> {

    private Map map;

    private Structure connectedStructure;
    private boolean terminal;

    public StructureFactory(Map map) {
        this.map = map;
    }

    @Override
    public Structure getInstance() {

        if (connectedStructure == null) {
            return new Structure(map.getScene(), 0, 0);
        } else {
            // get random entrance
            int eCount = connectedStructure.getEntrances().length;
            StructureEntrance connectedEntrance = connectedStructure.getEntrances()[map.getScene().getRandom().nextInt(eCount)];

            // get potential structs
            List<PotentialStructure> potentialStructures = connectedEntrance.getPotentialStructures().stream()
                    .filter(ps -> ps.isTerminal() == terminal)
                    .collect(Collectors.toList());

            while (potentialStructures.size() > 0) {

                // get and remove potential
                int psCount = potentialStructures.size();
                PotentialStructure potentialStructure = potentialStructures.get(map.getScene().getRandom().nextInt(psCount));
                potentialStructures.remove(potentialStructure);

                GameData gdStructure = potentialStructure.getStructure();
                GameData gdEntrance = potentialStructure.getEntrance();


                // skip if it isn't a valid side
                // TODO move to potential structure generation
                switch (gdEntrance.getInteger("side")) {
                    case 0:
                        if (connectedEntrance.getSide() != 2) continue;
                        break;
                    case 1:
                        if (connectedEntrance.getSide() != 3) continue;
                        break;
                    case 2:
                        if (connectedEntrance.getSide() != 0) continue;
                        break;
                    case 3:
                        if (connectedEntrance.getSide() != 1) continue;
                        break;
                }


                Vector2i conPos = StructureEntrance.getEntrancePosition(
                        connectedEntrance.getSide(),
                        connectedEntrance.getLocation(),
                        connectedStructure.getWidth(),
                        connectedStructure.getHeight());

                Vector2i gdPos = StructureEntrance.getEntrancePosition(
                        gdEntrance.getInteger("side"),
                        gdEntrance.getInteger("location"),
                        gdStructure.getInteger("width"),
                        gdStructure.getInteger("height"));

                int len = connectedEntrance.getLength();

                Vector2i offset = StructureEntrance.getEntranceOffset(
                        connectedEntrance.getSide(),
                        len,
                        conPos,
                        gdPos);

//                System.out.println("Entrance Side: " + entranceSide);
//                System.out.println(connectedStructure.getName());
//                System.out.println(connectedStructure.getMapX());
//                System.out.println(connectedStructure.getMapY());
//                System.out.println(connectedStructure.getWidth());
//                System.out.println(connectedStructure.getHeight());

                offset.x += connectedStructure.getMapX();
                offset.y += connectedStructure.getMapY();

                Structure s = new Structure(
                        map.getScene(),
                        gdStructure,
                        gdEntrance,
                        offset.x, offset.y);

//                System.out.println(s.getName());
//                System.out.println(s.getMapX());
//                System.out.println(s.getMapY());
//                System.out.println(s.getWidth());
//                System.out.println(s.getHeight());

                // Check collision
                List<Structure> structures = map.getStructures();

                boolean safe = true;

                for (Structure structure : structures) {

                    if (s.overlaps(structure)) {
                        safe = false;

                        System.out.println(structure.getName());
                        System.out.println(structure.getMapX());
                        System.out.println(structure.getMapY());
                        System.out.println(structure.getWidth());
                        System.out.println(structure.getHeight());
                        System.out.println(s.getName());
                        System.out.println(s.getMapX());
                        System.out.println(s.getMapY());
                        System.out.println(s.getWidth());
                        System.out.println(s.getHeight());

                        break;
                    }
                }

                if (!safe) {
                    System.out.println("Not Safe: " + s.getName());

                    continue;
                }

                // check if entrances match
                for (Structure structure : structures) {
                    s.tryConnect(structure);
                }

                return s;

            }
        }

        return null;
    }

    @Override
    public void clear() {
        connectedStructure = null;
        terminal = false;
    }

    public void setConnection(Structure s) {
        connectedStructure = s;
    }

    public void setTerminal(boolean terminal) {
        this.terminal = terminal;
    }
}
