package com.armadillogamestudios.reclaim.util;

import com.armadillogamestudios.engine2d.util.pathfinding.PathAdjacentNode;
import com.armadillogamestudios.engine2d.util.pathfinding.PathInterpreter;
import com.armadillogamestudios.reclaim.data.Region;
import com.armadillogamestudios.reclaim.data.Unit;

import java.util.List;
import java.util.stream.Collectors;

public class UnitPathInterpreter implements PathInterpreter<Region> {

    private final Unit unit;

    public UnitPathInterpreter(Unit unit) {
        this.unit = unit;
    }

    @Override
    public List<PathAdjacentNode<Region>> getAdjacentNodes(Region node) {
        return node.getRegionConnections().stream()
                .map(rc -> new PathAdjacentNode<>(rc.getOther(node), unit.getTravelDifficulty(node.getBiome().getName())))
                .collect(Collectors.toList());
    }
}
